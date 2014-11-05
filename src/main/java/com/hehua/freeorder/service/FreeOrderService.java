package com.hehua.freeorder.service;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.hehua.commons.Filters;
import com.hehua.commons.Transformers;
import com.hehua.commons.collection.CollectionUtils;
import com.hehua.commons.model.CommonMetaCode;
import com.hehua.commons.model.ResultView;
import com.hehua.framework.config.DatabaseConfigManager;
import com.hehua.framework.image.ImageService;
import com.hehua.framework.image.domain.Image;
import com.hehua.freeorder.action.*;
import com.hehua.freeorder.action.params.ApplyGetParam;
import com.hehua.freeorder.action.params.ApplyParam;
import com.hehua.freeorder.action.params.DeliveryParam;
import com.hehua.freeorder.action.params.ResultParam;
import com.hehua.freeorder.dao.FreeOrderDAO;
import com.hehua.freeorder.enums.FreeOrderEnums;
import com.hehua.freeorder.exceptions.NoSuchFreeOrderActionException;
import com.hehua.freeorder.info.ApplyTipInfo;
import com.hehua.freeorder.info.FreeFlashInfo;
import com.hehua.freeorder.info.FreeOrderInfo;
import com.hehua.freeorder.model.FreeOrderModel;
import com.hehua.freeorder.value.FreeOrderValue;
import com.hehua.item.domain.FreeFlash;
import com.hehua.item.domain.Item;
import com.hehua.item.model.FreeFlashList;
import com.hehua.item.service.FreeFlashService;
import com.hehua.item.service.ItemAppraiseService;
import com.hehua.item.service.ItemService;
import com.hehua.order.info.AddressInfo;
import com.hehua.order.model.OrderTraceModel;
import com.hehua.order.service.DeliveryService;
import com.hehua.order.service.OrderTraceService;
import com.hehua.order.service.OrdersService;
import com.hehua.user.domain.Address;
import com.hehua.user.domain.User;
import com.hehua.user.service.AddressService;
import com.hehua.user.service.UserService;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

/**
 * Created by liuweiwei on 14-10-6.
 */
@Named
public class FreeOrderService {

    @Inject
    private FreeOrderDAO freeOrderDAO;

    @Inject
    private AddressService addressService;

    @Inject
    private DeliveryService deliveryService;

    @Inject
    private OrderTraceService orderTraceService;

    @Inject
    private ItemService itemService;

    @Inject
    private ImageService imageService;

    @Inject
    private FreeFlashService freeFlashService;

    @Inject
    private OrdersService ordersService;

    @Inject
    private DatabaseConfigManager databaseConfigManager;

    @Inject
    private ActionManager actionManager;

    @Inject
    private UserService userService;

    @Inject
    ItemAppraiseService itemAppraiseService;

    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(FreeOrderService.class);

    private static final Map<String, String> helpConfig = new HashMap<String, String>() {
        {
            put("free_order_apply_k1", "free_order_apply_v1");
            put("free_order_apply_k2", "free_order_apply_v2");
            put("free_order_apply_k3", "free_order_apply_v3");
        }
    };

    private static final String[] HELPKEYS = {"free_order_apply_k1", "free_order_apply_k2", "free_order_apply_k3"};
    private static final String APPLY_POST_RESULT = "free_order_apply_result";

    /**
     * 是否可申请评测
     *
     * @param userid
     * @return
     */
    public boolean canApply(long userid) {
        FreeOrderModel order = freeOrderDAO.existGoing(userid);
        if (order != null) {
            return false;
        }
        return true;
    }

    public FreeOrderModel getApplyAppraise(long userid) {
        return freeOrderDAO.getCanWriteAppraiseBy(userid);
    }

    public List<FreeOrderValue> getFreeUserInfoByItemId(int itemId, int offset, int limit) {
        List<FreeOrderModel> freeOrderModelList = freeOrderDAO.getListByItemIdByPage(itemId, offset, limit);
        if (CollectionUtils.isEmpty(freeOrderModelList)) {
            return Collections.emptyList();
        }

        return convertFreeOrderModelToFreeOrderInfo(freeOrderModelList);
    }

    public Integer getCountByItemId(int itemId) {
        return freeOrderDAO.getCountByItemId(itemId);
    }

    public boolean isExsitApplyByUserIdAndFreeId(long userId, int freeId) {
       return freeOrderDAO.isExsitApplyByUserIdAndFreeId(freeId, userId) > 0 ? true : false;
    }

    private List<Integer> getNotApplyItemByItemIdsAndUserId(long userId, List<Integer> itemIds){
        if (CollectionUtils.isEmpty(itemIds)) {
            return Collections.emptyList();
        }

        final List<Integer>  applyItemIds = freeOrderDAO.getsByItemIdsAndUserId(itemIds, userId);
        if (CollectionUtils.isEmpty(applyItemIds)) {
            return applyItemIds;
        }

        if (applyItemIds.size() == itemIds.size()) {
            return itemIds;
        }

        return applyItemIds;
    }

    /**
     * 设置用户是否申请过众测商品
     *
     * @param
     * @return
     */
    public List<FreeFlash> setApplyFreeFlashByUserId(List<FreeFlash> flashList, long userId) {
        List<Integer>  itemIds = Transformers.transformList(flashList, FreeFlash.itemIdExtractor);
        final List<Integer> applyItemIds = getNotApplyItemByItemIdsAndUserId(userId, itemIds);
        for (FreeFlash freeFlash : flashList) {
            if (applyItemIds.contains(Integer.valueOf(freeFlash.getItemid())))
                freeFlash.setApply(true);
            else
                freeFlash.setApply(false);
        }
        return flashList;
    }

    public void resetApplyFreeFlashList(List<FreeFlash> flashList) {
        for (FreeFlash freeFlash : flashList) {
           freeFlash.setApply(false);
        }
    }

    public Integer getPassAuitCountByItemId(int itemId) {
        return freeOrderDAO.getCountByStatusAndItemId(itemId, FreeOrderModel.STATUS_APPRVOE1);
    }

    public Integer getSignedCountByItemId(int itemId) {
        return freeOrderDAO.getCountByStatusAndItemId(itemId, FreeOrderModel.STATUS_SIGN);
    }

    public List<FreeOrderValue> getAduitPassUserInfoByItemId(int itemId) {
        List<FreeOrderModel> freeOrderModelList = freeOrderDAO.getListByItemIdNotContaionReject(itemId, FreeOrderModel.STATUS_REJECT1);
        if (CollectionUtils.isEmpty(freeOrderModelList)) {
            return Collections.emptyList();
        }

        return convertFreeOrderModelToFreeOrderInfo(freeOrderModelList);
    }

    private List<FreeOrderValue> convertFreeOrderModelToFreeOrderInfo(List<FreeOrderModel> freeOrderModelList) {
        List<Long> userIds = Transformers.transformList(freeOrderModelList, FreeOrderModel.userIdExtractor);
        List<User> userList = userService.getUserListByIds(userIds);
        Map<Long, User> userMap = Transformers.transformAsOneToOneMap(userList, new Function<User, Long>() {
            @Override
            public Long apply(User input) {
                return input.getId();
            }
        });
        List<FreeOrderValue> freeOrderInfoList = new ArrayList<>(freeOrderModelList.size());
        for (FreeOrderModel freeOrderModel : freeOrderModelList) {
            FreeOrderValue freeOrderValue = new FreeOrderValue();
            freeOrderValue.setUser(userMap.get(Long.valueOf(freeOrderModel.getUserid())));
            freeOrderValue.setAddress(JSON.parseObject(freeOrderModel.getAddressInfo(), AddressInfo.class));
            freeOrderValue.setStatus(freeOrderModel.getStatus());
            freeOrderValue.setFreeOrderid(freeOrderModel.getId());
            freeOrderInfoList.add(freeOrderValue);
        }

        return freeOrderInfoList;
    }


    public ResultView<ApplyGetParam> applyConfirm(long userid, int flashid) {
        if (!this.canApply(userid)) {
            return new ResultView<>(FreeOrderEnums.ORDER_NOT_FINISHED);
        }
        ApplyGetParam param = new ApplyGetParam();
        Address defaultAddress = addressService.getDefaultAddress(userid);
        if (defaultAddress == null) {
            defaultAddress = addressService.getRecentAddress(userid);
        }
        if (defaultAddress != null) {
            AddressInfo addressInfo = deliveryService.getAddressInfo(defaultAddress);
            param.setAddress(addressInfo);
        }
        List<ApplyTipInfo> tips = new ArrayList<>();
        for (String key : HELPKEYS) {
            ApplyTipInfo info = new ApplyTipInfo();
            info.setKey(databaseConfigManager.getString(key));
            info.setValue(databaseConfigManager.getString(helpConfig.get(key)));
            tips.add(info);
        }
        param.setTips(tips);
        return new ResultView<>(CommonMetaCode.Success, param);
    }

    public ResultView<String> apply(ApplyParam param) {
        ActionApply actionApply = null;
        try {
            actionApply = (ActionApply) actionManager.getAction(ActionBase.ACTION_APPLY);
        } catch (NoSuchFreeOrderActionException e) {
            return new ResultView<>(CommonMetaCode.Error);
        }
        FreeOrderModel order = new FreeOrderModel();
        order.initWhenAdd();
        ResultParam<String> result = new ResultParam<>(CommonMetaCode.Success);
        if (!actionApply.doAction(order, param, result)) {
            return new ResultView<>(result.getMetaCode());
        }
        return new ResultView<>(CommonMetaCode.Success, databaseConfigManager.getString(APPLY_POST_RESULT));
    }

    public boolean setReject(FreeOrderModel freeOrderModel) {
        ActionReject1 actionReject1 = null;
        try {
            actionReject1 = (ActionReject1) actionManager.getAction(ActionBase.ACTION_REJECT1);
        } catch (NoSuchFreeOrderActionException e) {
            logger.error("", e);
            return false;
        }
        ResultParam<String> result = new ResultParam<>(CommonMetaCode.Success);

        if (!actionReject1.doAction(freeOrderModel, null, result)) {
            logger.error("订单审核拒绝 error by orderId=" + freeOrderModel.getId() + " result=" + result.getMetaCode().getMessage());
            return false;
        }
        return true;
    }

    public boolean setApprove(FreeOrderModel freeOrderModel) {
        ActionApprove1 actionApprove1 = null;
        try {
            actionApprove1 = (ActionApprove1) actionManager.getAction(ActionBase.ACTION_APPROVE1);
        } catch (NoSuchFreeOrderActionException e) {
            logger.error("", e);
            return false;
        }
        ResultParam<String> result = new ResultParam<>(CommonMetaCode.Success);

        if (!actionApprove1.doAction(freeOrderModel, null, result)) {
            logger.error("订单审核通过 error by orderId=" + freeOrderModel.getId() + " result=" + result.getMetaCode().getMessage());
            return false;
        }
        return true;
    }

    public boolean setDelivery(FreeOrderModel freeOrderModel, DeliveryParam deliveryParam) {
        ActionDelivery actionDelivery = null;
        try {
            actionDelivery = (ActionDelivery) actionManager.getAction(ActionBase.ACTION_DELIVERY);
        } catch (NoSuchFreeOrderActionException e) {
            logger.error("", e);
            return false;
        }
        ResultParam<String> result = new ResultParam<>(CommonMetaCode.Success);

        if (!actionDelivery.doAction(freeOrderModel, deliveryParam, result)) {
            logger.error("商品已经发货is error by orderId=" + freeOrderModel.getId() + " result=" + result.getMetaCode().getMessage());
            return false;
        }
        return true;
    }

    public boolean setSign(FreeOrderModel freeOrderModel) {
        ActionSign actionSign = null;
        try {
            actionSign = (ActionSign) actionManager.getAction(ActionBase.ACTION_SIGN);
        } catch (NoSuchFreeOrderActionException e) {
            logger.error("", e);
            return false;
        }
        ResultParam<String> result = new ResultParam<>(CommonMetaCode.Success);

        if (!actionSign.doAction(freeOrderModel, null, result)) {
            logger.error("商品签收is error by orderId=" + freeOrderModel.getId() + " result=" + result.getMetaCode().getMessage());
            return false;
        }
        return true;
    }

    public boolean setAppraiseCommit(FreeOrderModel freeOrderModel) {
        ActionCommit actionCommit = null;
        try {
            actionCommit = (ActionCommit) actionManager.getAction(ActionBase.ACTION_COMMIT);
        } catch (NoSuchFreeOrderActionException e) {
            logger.error("", e);
            return false;
        }
        ResultParam<String> result = new ResultParam<>(CommonMetaCode.Success);

        if (!actionCommit.doAction(freeOrderModel, null, result)) {
            logger.error("评测提交is error by orderId=" + freeOrderModel.getId() + " result=" + result.getMetaCode().getMessage());
            return false;
        }
        return true;
    }


    public boolean setAppraiseReject2(FreeOrderModel freeOrderModel) {
        ActionReject2 actionReject2 = null;
        try {
            actionReject2 = (ActionReject2) actionManager.getAction(ActionBase.ACTION_REJECT2);
        } catch (NoSuchFreeOrderActionException e) {
            logger.error("", e);
            return false;
        }
        ResultParam<String> result = new ResultParam<>(CommonMetaCode.Success);

        if (!actionReject2.doAction(freeOrderModel, null, result)) {
            logger.error("评测审核不通过is error by orderId=" + freeOrderModel.getId() + " result=" + result.getMetaCode().getMessage());
            return false;
        }
        return true;
    }

    public boolean setAppraiseApprove2(FreeOrderModel freeOrderModel) {
        ActionApprove2 actionApprove2 = null;
        try {
            actionApprove2 = (ActionApprove2) actionManager.getAction(ActionBase.ACTION_APPROVE2);
        } catch (NoSuchFreeOrderActionException e) {
            logger.error("", e);
            return false;
        }
        ResultParam<String> result = new ResultParam<>(CommonMetaCode.Success);

        if (!actionApprove2.doAction(freeOrderModel, null, result)) {
            logger.error("评测审核通过is error by orderId=" + freeOrderModel.getId() + " result=" + result.getMetaCode().getMessage());
            return false;
        }
        return true;
    }


    public List<FreeOrderInfo> getListByUserId(long userid, int status, int offset, int limit) {
        List<FreeOrderModel> orders = new ArrayList<>();
        if (FreeOrderModel.descStatus.get(status) == null) {
            orders = freeOrderDAO.getAllByUserId(userid, offset, limit);
        } else {
            orders = freeOrderDAO.getListByUserId(userid, status, offset, limit);
        }
        List<FreeOrderInfo> result = new ArrayList<>();
        for (FreeOrderModel order : orders) {
            result.add(this.genFreeOrderInfo(order));
        }
        return result;
    }

    public FreeOrderModel getById(int freeOrderId) {
        return freeOrderDAO.getById(freeOrderId);

    }

    public FreeOrderValue getFreeOrderByFreeOrderId(int orderid) {
        FreeOrderModel freeOrderModel = freeOrderDAO.getById(orderid);
        FreeOrderInfo freeOrderInfo = genFreeOrderInfo(freeOrderModel);
        FreeOrderValue freeOrderValue = new FreeOrderValue();
        freeOrderValue.setFreeOrderInfo(freeOrderInfo);
        Item item = itemService.getItem_(freeOrderModel.getItemid());
        freeOrderValue.setItem(item);
        freeOrderValue.setItemAppraise(itemAppraiseService.getItemAppraiseByOrderId(freeOrderInfo.getFreeOrderid()));
        return freeOrderValue;
    }

    public boolean updateOrderStatus(int freeOrderId, int status) {
        FreeOrderModel freeOrderModel = freeOrderDAO.getById(freeOrderId);
        if (freeOrderModel == null) {
            return false;
        }
        if (status == FreeOrderModel.STATUS_APPRVOE1) {
            return setApproved1(freeOrderModel);
        }
        if (status == FreeOrderModel.STATUS_REJECT1) {
            return setReject1(freeOrderModel);
        }
        return false;
    }

    public FreeOrderInfo genFreeOrderInfo(FreeOrderModel order) {
        FreeOrderInfo orderInfo = new FreeOrderInfo();
        orderInfo.setAddress(JSON.parseObject(order.getAddressInfo(), AddressInfo.class));
        List<FreeFlashInfo> freeFlashInfos = new ArrayList<>();
        FreeFlash freeFlash = freeFlashService.getFreeFlash(order.getFreeFlashId());
        FreeFlashInfo freeFlashInfo = new FreeFlashInfo();
        freeFlashInfo.setFreeFlashid(order.getFreeFlashId());
        freeFlashInfo.setApplyNum(freeFlash.getApplynum());
        freeFlashInfo.setTotalNum(freeFlash.getFreequantity());
        Item item = itemService.getItemById(order.getItemid());
        Image image = imageService.getImageById(Long.parseLong(item.getImage()));
        if (image != null) {
            freeFlashInfo.setImage(image.getUrl());
        }
        freeFlashInfo.setItemid(item.getId());
        freeFlashInfo.setPrice(item.getOriginprice());
        freeFlashInfo.setSkuid(order.getSkuid());
        freeFlashInfo.setTitle(item.getName());
        freeFlashInfo.setTypes(ordersService.genItemTypeInfo(order.getSkuid()));
        freeFlashInfos.add(freeFlashInfo);
        orderInfo.setFreeFlashInfo(freeFlashInfos);

        if (order.hasDeliveried()) {
            orderInfo.setDeliveryInfo(orderTraceService.updateGenDeliveryInfo(order.getId(), OrderTraceModel.TYPE_FREE));
        }

        orderInfo.setApplyTime(order.getAddtime());
        orderInfo.setStatus(order.getStatus());
        orderInfo.setFreeOrderid(order.getId());

        return orderInfo;
    }

    public boolean setApproved1(FreeOrderModel order) {
        order.setApproved1();
        if (freeOrderDAO.updateStatus(order) == 1) {
            return true;
        }
        return false;
    }

    public boolean setReject1(FreeOrderModel order) {
        order.setReject1();
        if (freeOrderDAO.updateStatus(order) == 1) {
            return true;
        }
        return false;
    }

    public boolean setDeliveried(FreeOrderModel order) {
        order.setDeliveried();
        if (freeOrderDAO.setDeliveried(order) == 1) {
            return true;
        }
        return false;
    }

    public boolean setSigned(FreeOrderModel order) {
        order.setSigned();
        if (freeOrderDAO.updateStatus(order) == 1) {
            return true;
        }
        return false;
    }

    public boolean setApproved2(FreeOrderModel order) {
        order.setApproved2();
        if (freeOrderDAO.updateStatus(order) == 1) {
            return true;
        }
        return false;
    }

    public boolean setReject2(FreeOrderModel order) {
        order.setReject2();
        if (freeOrderDAO.updateStatus(order) == 1) {
            return true;
        }
        return false;
    }

    public boolean setEdit(FreeOrderModel order) {
        order.setEdit();
        if (freeOrderDAO.updateStatus(order) == 1) {
            return true;
        }
        return false;
    }

    public boolean setCommit(FreeOrderModel order) {
        order.setCommit();
        if (freeOrderDAO.updateStatus(order) == 1) {
            return true;
        }
        return false;
    }

    public List<User> getNewApplyUser() {
        List<Long> userIds = freeOrderDAO.getUseridOfNewApply();
        return userService.getUserListByIds(userIds);
    }
}
