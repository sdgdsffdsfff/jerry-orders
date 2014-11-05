package com.hehua.order.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Function;
import com.hehua.commons.Transformers;
import com.hehua.commons.model.CommonMetaCode;
import com.hehua.commons.model.ResultView;
import com.hehua.commons.time.DateUtils;
import com.hehua.framework.image.ImageService;
import com.hehua.framework.image.domain.Image;
import com.hehua.framework.web.HehuaRequestContext;
import com.hehua.item.model.ItemLite;
import com.hehua.item.service.ItemService;
import com.hehua.order.constants.PaiyouConfig;
import com.hehua.order.constants.RefundReasonConfig;
import com.hehua.order.dao.*;
import com.hehua.order.enums.OrdersErrorEnum;
import com.hehua.order.enums.RefundErrorEnum;
import com.hehua.order.exceptions.HttpStatusCodeException;
import com.hehua.order.exceptions.NoSuchRefundActionException;
import com.hehua.order.httpclient.MyHttpClient;
import com.hehua.order.info.*;
import com.hehua.order.info.paiyou.PaiuRefundInfo;
import com.hehua.order.listener.SyncOrderToPaiuListener;
import com.hehua.order.model.*;
import com.hehua.order.pay.PayManager;
import com.hehua.order.refund.*;
import com.hehua.order.refund.params.ApplyParam;
import com.hehua.order.refund.params.ResultParam;
import com.hehua.order.refund.params.ReturngoodsParam;
import com.hehua.order.service.params.RefundServiceApplyParam;
import com.hehua.order.service.params.RefundServiceConfirmParam;
import com.hehua.order.service.params.RefundServiceConfirmRetParam;
import com.hehua.order.utils.DecimalUtil;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by liuweiwei on 14-8-9.
 */
@Named
public class RefundService {

    private Logger log = Logger.getLogger(RefundService.class.getName());

    @Inject
    private RefundDAO refundDAO;

    @Inject
    private RefundLogDAO refundLogDAO;

    @Inject
    private OrdersDAO ordersDAO;

    @Inject
    private OrderItemsDAO orderItemsDAO;

    @Inject
    private DeliveryCompanyDAO deliveryCompanyDAO;

    @Inject
    private NotifySuccessLogDAO notifySuccessLogDAO;

    @Inject
    private DeliveryInfoDAO deliveryInfoDAO;

    @Inject
    private PayManager payManager;

    @Inject
    private ItemService itemService;

    @Inject
    private ImageService imageService;

    @Inject
    private RefundItemsDAO refundItemsDAO;

    @Inject
    private OrdersService ordersService;

    @Inject
    private RefundActionManager refundActionManager;

    public ResultView<String> updateApply(RefundServiceApplyParam param) {
        /**
         * TODO
         * 1、计算可退金额，目前默认是订单支付金额
         * 2、获取退款商品信息
         */
        if (!(param.getOrderid() != 0 && param.getDeliveryComPinyin() != null && param.getDeliveryNum() != null)) {
            return new ResultView<>(RefundErrorEnum.PARAM_ERROR);
        }
        OrdersModel order = ordersDAO.getById(param.getOrderid());
        if (order == null) {
            return new ResultView<>(RefundErrorEnum.ORDER_NOT_EXIST);
        }
        long userid = HehuaRequestContext.getUserId();
        if (!order.canRefund() || userid != order.getUserid()) {
            return new ResultView<>(RefundErrorEnum.CAN_NOT_APPLY);
        }

        DeliveryCompanyModel deliveryCompanyModel = deliveryCompanyDAO.getByPinyin(param.getDeliveryComPinyin());
        if (deliveryCompanyModel == null) {
            return new ResultView<>(OrdersErrorEnum.DELIVERY_COMPANY_NOT_EXISTS);
        }

        RefundModel refund = new RefundModel();
        refund.setUserid(userid);
        refund.setOrderid(order.getId());
        refund.setMoney(order.getOrderfee());
        refund.setComment(param.getComment() == null ? "" : param.getComment());

        HashMap<String, String> deliveryInfo = new HashMap<String, String>();
        deliveryInfo.put("deliveryComPinyin", param.getDeliveryComPinyin());
        deliveryInfo.put("deliveryNum", param.getDeliveryNum());
        refund.setDeliveryInfo(JSON.toJSONString(deliveryInfo));
        refund.initWhenAdd();
        refundDAO.add(refund);

        RefundLogModel refundlog = new RefundLogModel();
        refundlog.setRefundid(refund.getId());
        refundlog.setType(RefundLogModel.TYPE_ADD);
        refundlog.setOpid(0);
        refundlog.setComment("用户申请");
        refundlog.setAddtime((int)(System.currentTimeMillis() / 1000L));
        refundLogDAO.add(refundlog);

        ordersDAO.updateStatus(order.getId(), OrdersModel.STATUS_REFUNDING);
        return new ResultView<>(CommonMetaCode.Success, "success");
    }

    public List<RefundInfo> getRefundInfoByOrderId(long orderid) {
        List<RefundModel> refunds = refundDAO.getListByOrderId(orderid);
        if (refunds == null) {
            return new ArrayList<>();
        }
        List<RefundInfo> result = new ArrayList<>();
        for (RefundModel refund : refunds) {
            RefundInfo refundInfo = new RefundInfo();
            refundInfo.setId(refund.getId());
            refundInfo.setStatus(refund.getStatus());
            refundInfo.setStatusName(RefundModel.descStatus.get(refund.getStatus()));
            refundInfo.setMoney(DecimalUtil.toStandard(refund.getMoney()));
            List<RefundEventInfo> refundEventInfos = new ArrayList<>();
            List<RefundLogModel> refundlogs = refundLogDAO.getListByRefundId(refund.getId());
            for (RefundLogModel refundlog : refundlogs) {
                RefundEventInfo refundEventInfo = new RefundEventInfo();
                refundEventInfo.setType(refundlog.getType());
                refundEventInfo.setTypeName(RefundLogModel.descType.get(refundlog.getType()));
                refundEventInfo.setTimestamp(refundlog.getAddtime());
                refundEventInfo.setComment(refundlog.getComment());
                refundEventInfo.setMessage(RefundLogModel.typeMsg.get(refundlog.getType()));
                refundEventInfos.add(refundEventInfo);
            }
            refundInfo.setEvent(refundEventInfos);
            result.add(refundInfo);
        }
        return result;
    }

    public boolean updateSendRefundToPaiu(RefundModel refund) {
        Logger log = Logger.getLogger(SyncOrderToPaiuListener.class.getName());
        PaiuRefundInfo paiuRefundInfo = new PaiuRefundInfo();
        paiuRefundInfo.setOrderCode(String.valueOf(refund.getOrderid()));
        paiuRefundInfo.setType("0"); //全部退款
        JSONObject deliveryInfo = JSONObject.parseObject(refund.getDeliveryinfo());
        paiuRefundInfo.setExpressNO(deliveryInfo.getString("deliveryNum"));
        //全部退款条形码可不填
        paiuRefundInfo.setBarCode("");
        paiuRefundInfo.setReason(refund.getComment());

        String strRefundInfo = JSON.toJSONString(paiuRefundInfo);
        String body = "appkey=" + PaiyouConfig.APPKEY + "&appsecret=" + PaiyouConfig.APPSECKET + "&data=" + strRefundInfo;
        log.info("send refund to paiu, body:" + body);
        if (log.isDebugEnabled()) {
            System.out.println("send refund to paiu, body:" + body);
        }
        MyHttpClient myHttpClient = new MyHttpClient();
        String response = null;
        try {
            response = myHttpClient.httpPost(PaiyouConfig.getReturnGoodsUrl(), body);
        } catch (IOException e) {
            log.error("io exception", e);
            return false;
        } catch (HttpStatusCodeException e) {
            log.error("network error", e);
            return false;
        }
        log.info("send refund to paiu response:" + response);
        if (log.isDebugEnabled()) {
            System.out.println("send refund to paiu response:" + response);
        }
        JSONObject jo = JSON.parseObject(response);
        if (!jo.get("state").equals("true")) {
            log.error("send refund to paiu error:" + response);
            return false;
        }
        refund.setSended2Paiu();
        refundDAO.setSended2Paiu(refund);
        return true;
    }

    /**
     * 找到符合条件的支付记录
     * @param refundid
     * @return
     */
    public NotifySuccessLogModel getNotifySuccessLogByRefundId(int refundid) {
        RefundModel refund = refundDAO.getById(refundid);
        if (refund == null) {
            return null;
        }
        List<NotifySuccessLogModel> notifySuccessLogModels = notifySuccessLogDAO.getByOrderId(refund.getOrderid());
        NotifySuccessLogModel result = null;
        for (NotifySuccessLogModel notifySuccessLogModel : notifySuccessLogModels) {
            BigDecimal moneyCanRefund = notifySuccessLogModel.getMoney().subtract(notifySuccessLogModel.getRefund2third());
            if (moneyCanRefund.subtract(refund.getMoney()).doubleValue() >= 0) {
                result = notifySuccessLogModel;
                break;
            }
        }
        return result;
    }

    /**
     * 处理支付宝退款通知
     * @param params
     * @return
     */
    public boolean updateProcessRefundNotify(Map<String, String> params) {
        String batchNo = params.get("batch_no");
        String resultDetails = params.get("result_details");
        String[] result = resultDetails.split("\\^");
        if (result.length < 3) {
            log.error("param error:" + resultDetails + " length:" + result.length);
            return false;
        }
        String tradeno = result[0];
        RefundModel refund = refundDAO.getByTradenoAndOutno(tradeno, batchNo);
        if (refund == null) {
            log.error("refund not exists:" + params.toString());
            return false;
        }
        if (refund.getStatus() != RefundModel.STATUS_WAITING) {
            log.error("status error:refunid=" + refund.getId());
        }
        String[] isSuccess = result[2].split("\\$");
        //result_details格式：交易号^退款金额^处理结果$退费账号^退费账户ID^退费金额^处理结果。
        if (isSuccess.length < 2) {
            log.info("no account info:" + params.toString());
        }
        RefundLogModel refundlog = new RefundLogModel();
        refundlog.initWhenAdd();
        refundlog.setRefundid(refund.getId());
        if (isSuccess[0].equalsIgnoreCase("success")) {
            refund.setStatus(RefundModel.STATUS_SUCCESS);
            refund.setSucctime(DateUtils.unix_timestamp());
            refundlog.setType(RefundLogModel.TYPE_SUCCESS);
        } else {
            refund.setStatus(RefundModel.STATUS_FAIL);
            refund.setFailtime(DateUtils.unix_timestamp());
            refundlog.setType(RefundLogModel.TYPE_FAIL);
            refundlog.setComment(isSuccess[0]);
        }
        refund.setModtime(DateUtils.unix_timestamp());

        OrdersModel order = ordersDAO.getById(refund.getOrderid());
        if (order == null) {
            log.error("get order error, orderid=" + refund.getOrderid());
            return false;
        }
        if (order.getStatus() != OrdersModel.STATUS_REFUNDING) {
            log.error("order status error, orderid=" + order.getId());
            return false;
        }
        refundDAO.refundNotify(refund);
        refundLogDAO.add(refundlog);
        ordersDAO.updateStatus(refund.getOrderid(), OrdersModel.STATUS_REFUND);
        log.info("process refund notifylog success:" + params.toString());
        return true;
    }

    /**
     * 处理卖超了自动退
     * @param notify
     * @return
     */
    public boolean updatePayFailApply(NotifySuccessLogModel notify) {
        //新增退款
        RefundModel refund = new RefundModel();
        refund.initWhenAdd();
        refund.setType(RefundModel.TYPE_LESS);
        refund.setUserid(notify.getUserid());
        refund.setOrderid(notify.getOrderid());
        refund.setMoney(notify.getMoney());
        refund.setComment("卖超自动退");
        refund.setDeliveryInfo("");
        refundDAO.add(refund);
        RefundLogModel refundlog = new RefundLogModel();
        refundlog.initWhenAdd();
        refundlog.setRefundid(refund.getId());
        refundlog.setType(RefundLogModel.TYPE_ADD);
        refundlog.setComment("用户申请");
        refundLogDAO.add(refundlog);

        //增加系统自动审核通过日志
        refundlog = new RefundLogModel();
        refundlog.initWhenAdd();
        refundlog.setRefundid(refund.getId());
        refundlog.setType(RefundLogModel.TYPE_APPROVE);
        refundlog.setComment("系统自动审核通过");
        refundLogDAO.add(refundlog);

        //生成退款批次号、交易号
        refund.setPaytype(notify.getPaytype());
        refund.setDirect(refund.getMoney());
        refund.setTradeno(notify.getTradeno());
        refund.setSendtime(DateUtils.unix_timestamp());
        refund.setOutno(RefundModel.genOutno(refund));
        refund.setStatus(RefundModel.STATUS_WAITING);
        refundDAO.refund2third(refund);
        refundlog = new RefundLogModel();
        refundlog.initWhenAdd();
        refundlog.setRefundid(refund.getId());
        refundlog.setType(RefundLogModel.TYPE_WAITING);
        refundlog.setComment("生成退款信息");
        refundLogDAO.add(refundlog);

        ordersDAO.updateStatus(notify.getOrderid(), OrdersModel.STATUS_REFUNDING);
        return true;
    }

    /**
     * 生成订单退款信息
     * @param orderid
     * @return
     */
    public List<RefundItemInfo> getRefundItemInfos(long orderid) {
        List<OrderItemsModel> orderItemsModels = orderItemsDAO.getListByOrderId(orderid);
        Collection<Integer> ids = Transformers.transformList(orderItemsModels, OrderItemsModel.itemIdExtractor);
        Map<Integer, ItemLite> itemIdItemLiteMap = itemService.getItemLitesByIds(ids);
        List<RefundItemInfo> refundItemInfos = new ArrayList<>();
        for (OrderItemsModel orderItemsModel : orderItemsModels) {
            RefundItemInfo refundItemInfo = new RefundItemInfo();
            refundItemInfo.setSkuid(orderItemsModel.getSkuid());
            refundItemInfo.setItemid(orderItemsModel.getItemid());
            refundItemInfo.setPrice(orderItemsModel.getSaleprice());
            refundItemInfo.setTitle(itemIdItemLiteMap.get((int)orderItemsModel.getItemid()).getName());
            Image image = imageService.getImageById(Long.parseLong(itemIdItemLiteMap.get((int)orderItemsModel.getItemid()).getImage()));
            if (image != null) {
                refundItemInfo.setImage(image.getUrl());
            }
            refundItemInfo.setCanRefundAmount(orderItemsModel.getQuantity());
            refundItemInfo.setTypes(ordersService.genItemTypeInfo(orderItemsModel.getSkuid()));
            refundItemInfos.add(refundItemInfo);
        }
        Map<Long, RefundItemInfo> skuIdRefundItemInfoMap = Transformers.transformAsOneToOneMap(refundItemInfos, RefundItemInfo.skuIdExtrator);
        List<RefundItemsModel> refundItemsModels = refundItemsDAO.getListByOrderIdAndStatus(orderid, RefundItemsModel.STATUS_SUCCESS);
        for (RefundItemsModel refundItemsModel : refundItemsModels) {
            RefundItemInfo info = skuIdRefundItemInfoMap.get(refundItemsModel.getSkuid());
            info.setCanRefundAmount(info.getCanRefundAmount() - refundItemsModel.getQuantity());
        }
        return refundItemInfos;
    }

    /**
     * 退款确认
     * @param param
     * @return
     */
    public ResultView<RefundServiceConfirmRetParam> refundConfirm(RefundServiceConfirmParam param) {
        RefundServiceConfirmRetParam result = new RefundServiceConfirmRetParam();
        if (param.getSkus() == null) {
            return new ResultView<>(OrdersErrorEnum.PARAM_ERROR);
        }
        OrdersModel order = ordersDAO.getById(param.getOrderid());
        if (order == null) {
            return new ResultView<>(OrdersErrorEnum.ORDER_NOT_EXIST);
        }
        if (order.getUserid() != param.getUserid()) {
            return new ResultView<>(OrdersErrorEnum.ORDER_NOT_YOURS);
        }
        if (!order.canRefund()) {
            return new ResultView<>(RefundErrorEnum.CAN_NOT_APPLY);
        }
        List<RefundItemInfo> refundItemInfos = this.getRefundItemInfos(param.getOrderid());
        Map<Long, RefundItemInfo> skuIdRefundItemInfoMap = Transformers.transformAsOneToOneMap(refundItemInfos, new Function<RefundItemInfo, Long>() {
            @Override
            public Long apply(RefundItemInfo input) {
                return input.getSkuid();
            }
        });
        BigDecimal totalCanRefund = new BigDecimal(0);
        List<RefundItemInfo> selectedRefundItem = new ArrayList<>();
        for (SkuInfo skuInfo : param.getSkus()) {
            RefundItemInfo refundItemInfo = skuIdRefundItemInfoMap.get(skuInfo.getSkuid());
            if (refundItemInfo == null) {
                return new ResultView<>(RefundErrorEnum.SKU_NOT_MATCH_ORDER);
            }
            if (refundItemInfo.getCanRefundAmount() < skuInfo.getQuantity()) {
                return new ResultView<>(RefundErrorEnum.REFUND_NUM_ERROR);
            }
            totalCanRefund = totalCanRefund.add(refundItemInfo.getPrice().multiply(new BigDecimal(skuInfo.getQuantity())));
            selectedRefundItem.add(refundItemInfo);
        }
        result.setCanRefundMoney(DecimalUtil.toStandard(totalCanRefund).doubleValue());
        result.setItems(selectedRefundItem);
        result.setSelectedSkus(param.getSkus());
        result.setRefundReasons(RefundReasonConfig.genReasonInfo());

        DeliveryInfoModel deliveryInfoModel = deliveryInfoDAO.getByOrderId(order.getId());
        AddressInfo addressInfo = JSON.parseObject(deliveryInfoModel.getAddress(), AddressInfo.class);
        UserInfo userInfo = new UserInfo();
        userInfo.setName(addressInfo.getName());
        userInfo.setMobile(addressInfo.getPhone());

        result.setUserInfo(userInfo);

        return new ResultView<>(CommonMetaCode.Success, result);
    }

    public ResultView<List<RefundInfo>> actionApply(ApplyParam param) {
        ApplyAction action = null;
        try {
            action = (ApplyAction) refundActionManager.getAction(RefundBase.ACTION_APPLY);
        } catch (NoSuchRefundActionException e) {
            return new ResultView<>(CommonMetaCode.Error);
        }
        RefundModel refund = new RefundModel();
        ResultParam<List<RefundInfo>> result = new ResultParam<>(CommonMetaCode.Success);
        if (!action.doAction(refund, param, result)) {
            return new ResultView<>(result.getMetaCode());
        }
        return new ResultView<>(CommonMetaCode.Success, this.getRefundInfoByOrderId(refund.getOrderid()));
    }

    public ResultView<List<RefundInfo>> actionCancel(int refundid, long userid) {
        CancelAction action = null;
        try {
            action = (CancelAction) refundActionManager.getAction(RefundBase.ACTION_CANCEL);
        } catch (NoSuchRefundActionException e) {
            return new ResultView<>(CommonMetaCode.Error);
        }
        RefundModel refund = refundDAO.getById(refundid);
        ResultParam<List<RefundInfo>> result = new ResultParam<>(CommonMetaCode.Success);
        if (!action.doAction(refund, userid, result)) {
            return new ResultView<>(result.getMetaCode());
        }
        return new ResultView<>(CommonMetaCode.Success, this.getRefundInfoByOrderId(refund.getOrderid()));
    }

    public ResultView<List<RefundInfo>> actionReturngoods(ReturngoodsParam param) {
        ReturngoodsAction action = null;
        try {
            action = (ReturngoodsAction) refundActionManager.getAction(RefundBase.ACTION_RETURNGOODS);
        } catch (NoSuchRefundActionException e) {
            return new ResultView<>(CommonMetaCode.Error);
        }
        RefundModel refund = refundDAO.getById(param.getRefundid());
        ResultParam<List<RefundInfo>> result = new ResultParam<>(CommonMetaCode.Success);
        if (!action.doAction(refund, param, result)) {
            return new ResultView<>(result.getMetaCode());
        }
        return new ResultView<>(CommonMetaCode.Success, this.getRefundInfoByOrderId(refund.getOrderid()));
    }
}
