package com.hehua.order.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Function;
import com.google.common.collect.ListMultimap;
import com.hehua.commons.Transformers;
import com.hehua.commons.collection.CollectionUtils;
import com.hehua.commons.model.CommonMetaCode;
import com.hehua.commons.model.ResultView;
import com.hehua.commons.time.DateUtils;
import com.hehua.commons.utils.IPUtil;
import com.hehua.event.EventDispatcher;
import com.hehua.event.impl.SmsEvent;
import com.hehua.framework.config.DatabaseConfigManager;
import com.hehua.framework.image.ImageService;
import com.hehua.framework.image.domain.Image;
import com.hehua.framework.web.HehuaRequestContext;
import com.hehua.item.domain.Item;
import com.hehua.item.domain.ItemSku;
import com.hehua.item.model.ItemLite;
import com.hehua.item.model.ItemPropertyView;
import com.hehua.item.purchase.restriction.PurchaseOperation;
import com.hehua.item.purchase.restriction.PurchaseRestrictionPolicyCheckResult;
import com.hehua.item.purchase.restriction.PurchaseRestrictionService;
import com.hehua.item.service.ItemPropertyRender;
import com.hehua.item.service.ItemService;
import com.hehua.item.service.ItemSkuService;
import com.hehua.order.constants.OrderConfig;
import com.hehua.order.dao.CartDAO;
import com.hehua.order.dao.DeliveryCompanyDAO;
import com.hehua.order.dao.DeliveryInfoDAO;
import com.hehua.order.dao.OrderItemsDAO;
import com.hehua.order.dao.OrdersDAO;
import com.hehua.order.enums.OrdersErrorEnum;
import com.hehua.order.exceptions.NoSuchPayConfigException;
import com.hehua.order.info.AddressInfo;
import com.hehua.order.info.ItemInfo;
import com.hehua.order.info.ItemTypeInfo;
import com.hehua.order.info.OrderInfo;
import com.hehua.order.info.OrderItemFeedbackInfo;
import com.hehua.order.info.RefundInfo;
import com.hehua.order.info.SkuInfo;
import com.hehua.order.model.CartModel;
import com.hehua.order.model.DeliveryInfoModel;
import com.hehua.order.model.FeedbackModel;
import com.hehua.order.model.OrderItemsModel;
import com.hehua.order.model.OrderTraceModel;
import com.hehua.order.model.OrdersModel;
import com.hehua.order.pay.BasePayProvider;
import com.hehua.order.pay.PayManager;
import com.hehua.order.service.params.OrderServiceConfirmParam;
import com.hehua.order.service.params.OrderServiceConfirmRetParam;
import com.hehua.order.service.params.OrdersServiceCreateOrderParam;
import com.hehua.order.service.params.PayServicePayRetParam;
import com.hehua.order.utils.DecimalUtil;
import com.hehua.user.domain.Address;
import com.hehua.user.domain.OrderRcRecord;
import com.hehua.user.domain.User;
import com.hehua.user.service.AddressService;
import com.hehua.user.service.RiskControlService;
import com.hehua.user.service.UserManager;

/**
 * Created by liuweiwei on 14-7-25.
 */
@Named
public class OrdersService {

    static Logger log = Logger.getLogger(OrdersService.class.getName());

    @Inject
    private OrdersDAO ordersDAO;

    @Inject
    private OrderItemsDAO orderItemsDAO;

    @Inject
    private DeliveryInfoDAO deliveryInfoDAO;

    @Inject
    private RefundService refundService;

    @Inject
    private DeliveryService deliveryService;

    @Inject
    private DeliveryCompanyDAO deliveryCompanyDAO;

    @Inject
    private FeedbackService feedbackService;

    @Inject
    private AddressService addressService;

    @Inject
    private ItemService itemService;

    @Inject
    private ItemSkuService itemSkuService;

    @Inject
    private CartDAO cartDAO;

    @Inject
    private ItemPropertyRender itemPropertyRender;

    @Inject
    private PayManager payManager;

    @Inject
    private OrderTraceService orderTraceService;

    @Inject
    private ImageService imageService;

    @Inject
    private PurchaseRestrictionService purchaseRestrictionService;

    @Inject
    private DatabaseConfigManager databaseConfigManager;

    @Inject
    private EventDispatcher eventDispatcher;

    @Inject
    private UserManager userManager;

    @Inject
    private RiskControlService riskControlService;

    @Inject
    private OrderService orderService;

    public static final String KEY_ORDER_TIPS = "order_tips";

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(OrdersService.class);

    /**
     * 下单并支付
     * 
     * @param param
     * @return
     */
    public ResultView<PayServicePayRetParam> createOrderAndPay(OrdersServiceCreateOrderParam param) {

        long userid = param.getUserid();
        log.info(param.toString());
        if (log.isDebugEnabled()) {
            System.out.println(param.toString());
        }

        if (!(param.getItems() != null && param.getIp() != null && param.getAddressid() != 0 && param
                .getPayType() != 0)) {
            log.error("param error");
            return new ResultView<>(OrdersErrorEnum.PARAM_ERROR);
        }

        BasePayProvider payObj = null;
        try {
            payObj = payManager.getPayObj(param.getPayType());
        } catch (NoSuchPayConfigException e) {
            log.error("paytype error", e);
            return new ResultView<>(OrdersErrorEnum.PAY_TYPE_ERROR);
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
            return new ResultView<>(CommonMetaCode.Error);
        }

        Address address = addressService.getAddressById(param.getAddressid());
        if (address == null) {
            return new ResultView<>(OrdersErrorEnum.ADDRESS_NOT_EXIST);
        }
        if (address.getUid() != userid) {
            return new ResultView<>(OrdersErrorEnum.ADDRESS_NOT_YOURS);
        }

        ArrayList<OrderItemsModel> orderitems = new ArrayList<OrderItemsModel>();
        BigDecimal orderfee = new BigDecimal(0);
        ArrayList<SkuInfo> skuInfos = (ArrayList<SkuInfo>) param.getItems();
        for (SkuInfo skuInfo : skuInfos) {
            if (skuInfo.getSkuid() == 0 || skuInfo.getQuantity() == 0) {
                return new ResultView<>(OrdersErrorEnum.PARAM_ERROR);
            }
            ItemSku itemSku = itemSkuService.getItemSkuById(skuInfo.getSkuid());
            if (itemSku == null) {
                return new ResultView<>(OrdersErrorEnum.SKU_NOT_EXIST);
            }
            if (skuInfo.getQuantity() < 0 || (itemSku.getQuantity() - skuInfo.getQuantity() < 0)) {
                return new ResultView<>(OrdersErrorEnum.QUANTITY_ERROR);
            }
            if (!itemService.isSellable(itemSku.getItemid())) {
                return new ResultView<>(OrdersErrorEnum.ITEM_CAN_NOT_BUY);
            }
            Item item = itemService.getItemById(itemSku.getItemid());
            if (item == null) {
                log.error("item not exist,skuid=" + itemSku.getId());
                return new ResultView<>(OrdersErrorEnum.ITEM_NOT_EXIST);
            }
            CartModel cartModel = cartDAO.getByUserIdAndSkuId(userid, itemSku.getId());
            if (cartModel == null) {
                return new ResultView<PayServicePayRetParam>(OrdersErrorEnum.ITEM_NOT_FROM_CART);
            }

            PurchaseRestrictionPolicyCheckResult checkResult = purchaseRestrictionService
                    .checkRestrictionPolicies((int) userid, 0, (int) itemSku.getItemid(),
                            (int) itemSku.getId(), skuInfo.getQuantity(),
                            PurchaseOperation.CreateOrder, address.getMobile(),
                            HehuaRequestContext.getClientId(), HehuaRequestContext.getDeviceId());

            if (!checkResult.isAccept()) {
                return new ResultView<>(OrdersErrorEnum.QUANTITY_RESTRICTION);
            }
            OrderItemsModel orderItem = new OrderItemsModel();
            orderItem.setUserid(userid);
            orderItem.setItemid(itemSku.getItemid());
            orderItem.setQuantity(skuInfo.getQuantity());
            orderItem.setSkuid(itemSku.getId());
            orderItem.setBuyprice(new BigDecimal(0));
            orderItem.setSaleprice(new BigDecimal(itemSku.getRealprice()));
            orderitems.add(orderItem);

            //将商品从购物车种删除
            cartDAO.deleteById(cartModel.getId());
            orderfee = orderfee.add(orderItem.getSaleprice().multiply(
                    new BigDecimal(orderItem.getQuantity())));
        }

        //计算物流费
        BigDecimal totalfee = new BigDecimal(0);
        BigDecimal deliveryfee = this.calcDeliveryFee(orderitems);
        totalfee = totalfee.add(deliveryfee).add(orderfee);

        OrdersModel order = new OrdersModel();
        order.setDeliveryfee(DecimalUtil.toStandard(deliveryfee));
        order.setOrderfee(DecimalUtil.toStandard(orderfee));
        order.setTotalfee(DecimalUtil.toStandard(totalfee));
        //order.setTotalfee(DecimalUtil.toStandard(new BigDecimal(0.01)));
        order.setOrdertime(DateUtils.unix_timestamp());
        order.setOrderip(IPUtil.ip2long(param.getIp()));
        order.setUserid(userid);
        order.setPaytype(param.getPayType());
        order.setMobile(param.getMobile() == null ? "" : param.getMobile());
        order.initWhenAdd();

        ordersDAO.createOrder(order);
        for (OrderItemsModel orderitem : orderitems) {
            orderitem.setOrderid(order.getId());
            orderItemsDAO.createOrderItems(orderitem);
        }

        //////////////////////设置收货地址、发票信息//////////////////////
        DeliveryInfoModel deliveryInfoModel = new DeliveryInfoModel();
        deliveryInfoModel.setOrderid(order.getId());
        deliveryInfoModel.setAddress(JSON.toJSONString(deliveryService.getAddressInfo(address)));

        if (!org.apache.commons.lang3.StringUtils.isBlank(param.getInvoiceComment())) {
            deliveryInfoModel.setInvoicecomment(param.getInvoiceComment());
        } else {
            deliveryInfoModel.setInvoicecomment("");
        }

        if (DeliveryInfoModel.invoiceDesc.get(param.getInvoiceType()) != null) {
            deliveryInfoModel.setInvoicetype(param.getInvoiceType());
        } else {
            deliveryInfoModel.setInvoicetype(0);
        }

        if (param.getInvoiceType() == DeliveryInfoModel.TYPE_INVOICE_COMPANY) {
            if (org.apache.commons.lang3.StringUtils.isBlank(deliveryInfoModel.getInvoicecomment())) {
                return new ResultView<>(OrdersErrorEnum.INVOICE_NOT_PRESENT);
            }
        }

        if (!org.apache.commons.lang3.StringUtils.isBlank(param.getDeliveryComment())) {
            deliveryInfoModel.setDeliverycomment(param.getDeliveryComment());
        } else {
            deliveryInfoModel.setDeliverycomment("");
        }
        if (log.isDebugEnabled()) {
            System.out.println("\n" + deliveryInfoModel.toString() + "\n");
        }
        deliveryInfoDAO.add(deliveryInfoModel);
        ////////////////////////////////////////////////////////

        //////////////////////处理支付//////////////////////////
        order.setOrderitems(orderitems);
        String url = payObj.getPayUrl(order);
        if (url.equalsIgnoreCase(BasePayProvider.FAIL_CODE)) {
            log.error("pay error");
            return new ResultView<>(OrdersErrorEnum.ORDER_PAY_ERROR);
        }

        // 写入风控记录
        User user = userManager.getUserById(userid);
        for (OrderItemsModel orderitem : orderitems) {
            try {
                OrderRcRecord orderRcRecord = createOrderRcRecord(user, orderitem, address);
                riskControlService.addOrderRecord(orderRcRecord);
            } catch (Exception e) {
                log.error("ops", e);
            }
        }

        PayServicePayRetParam response = new PayServicePayRetParam();
        response.setPayUrl(url);
        response.setOrderid(order.getId());
        return new ResultView<>(CommonMetaCode.Success, response);
    }

    private OrderRcRecord createOrderRcRecord(User user, OrderItemsModel orderitem, Address address) {
        OrderRcRecord orderRcRecord = new OrderRcRecord();

        orderRcRecord.setUid(orderitem.getUserid());
        orderRcRecord.setMobile(user.getAccount());
        orderRcRecord.setIp(HehuaRequestContext.getCurrentIpInLong());
        orderRcRecord.setClientid(HehuaRequestContext.getClientId());
        orderRcRecord.setDeviceid(HehuaRequestContext.getDeviceId());

        orderRcRecord.setReceiver(address.getReceiver());
        orderRcRecord.setReceivermobile(address.getMobile());

        orderRcRecord.setCounty(address.getCounty());
        orderRcRecord.setProvince(address.getProvince());
        orderRcRecord.setTown(address.getTown());
        orderRcRecord.setCity(address.getCity());

        orderRcRecord.setPostcode(address.getPostcode());
        orderRcRecord.setDetail(address.getDetail());

        orderRcRecord.setItemid(orderitem.getItemid());
        orderRcRecord.setSkuid(orderitem.getSkuid());
        orderRcRecord.setQuantity(orderitem.getQuantity());
        return orderRcRecord;
    }

    /**
     * 更新项目销量、库存
     * 
     * @param orderid
     * @param increase 增加还是减少
     * @return
     */
    public boolean updateItemQuantity(long orderid, boolean increase) {
        List<OrderItemsModel> orderItems = orderItemsDAO.getListByOrderId(orderid);
        for (OrderItemsModel orderItem : orderItems) {
            //增加库存，用于订单规定时间内未支付，销量暂时不减
            if (increase) {
                if (!itemSkuService.incrSkuQuantity((int) orderItem.getSkuid(),
                        orderItem.getQuantity())) {
                    log.error("increase sku quantity error, skuid=" + orderItem.getSkuid()
                            + ", quantity=" + orderItem.getQuantity());
                    return false;
                }
                log.info("increase sku quantity,, skuid=" + orderItem.getSkuid() + ", quantity="
                        + orderItem.getQuantity());
            } else {
                // 减库存，加销量，用于下单成功之后
                if (!itemSkuService.decrSkuQuantity((int) orderItem.getSkuid(),
                        orderItem.getQuantity())) {
                    log.error("decrease sku quantity error, skuid=" + orderItem.getSkuid()
                            + ", quantity=" + orderItem.getQuantity());
                    return false;
                }
                if (!itemService
                        .incrItemSales((int) orderItem.getItemid(), orderItem.getQuantity())) {
                    log.error("increase item sales error, skuid=" + orderItem.getSkuid()
                            + ", quantity=" + orderItem.getQuantity());
                    return false;
                }
                log.info("decrease sku quantity, skuid=" + orderItem.getSkuid() + ", quantity="
                        + orderItem.getQuantity());
            }
        }
        return true;
    }

    /**
     * 获取用户订单列表，不包含已经关闭的
     * 
     * @param userid
     * @param status
     * @param offset
     * @param limit
     * @return
     */
    public List<OrderInfo> getListByUserId(long userid, int status, int offset, int limit) {
        List<OrdersModel> orders = new ArrayList<>();
        if (OrdersModel.statusDesc.get(status) == null) {
            orders = ordersDAO.getAllByUserId(userid, offset, limit);
        } else {
            orders = ordersDAO.getListByUserId(userid, status, offset, limit);
        }
        List<OrderInfo> result = new ArrayList<>();
        for (OrdersModel order : orders) {
            if (order.getStatus() == OrdersModel.STATUS_CLOSED) {
                continue;
            }
            result.add(this.genOrderInfo(order));
        }
        return result;
    }

    /**
     * 批量获取订单信息
     *
     * @param orderModelList
     * @return
     */
    public Map<Long, OrderInfo> batchGenOrderInfo(List<OrdersModel> orderModelList) {
        if (CollectionUtils.isEmpty(orderModelList)) {
            return Collections.emptyMap();
        }

        List<Long> orderIds = Transformers.transformList(orderModelList,
                new Function<OrdersModel, Long>() {

                    @Override
                    public Long apply(OrdersModel input) {
                        return input.getId();
                    }
                });

        List<DeliveryInfoModel> deliveryInfoModelList = deliveryInfoDAO.getListByOrderIds(orderIds);
        Map<Long, DeliveryInfoModel> deliveryInfoMap = Transformers.transformAsOneToOneMap(
                deliveryInfoModelList, new Function<DeliveryInfoModel, Long>() {

                    @Override
                    public Long apply(DeliveryInfoModel input) {
                        return input.getOrderid();
                    }
                });
        ListMultimap<Long, OrderItemsModel> multiOrderItemList = orderService
                .getOrderItemByOrderId(orderIds);
        Set<Integer> itemIds = new HashSet<>();
        for (Long orderId : multiOrderItemList.keySet()) {
            multiOrderItemList.get(orderId);
            itemIds.addAll(Transformers.transformList(multiOrderItemList.get(orderId),
                    new Function<OrderItemsModel, Integer>() {

                        @Override
                        public Integer apply(OrderItemsModel input) {
                            return Integer.valueOf(String.valueOf(input.getItemid()));
                        }
                    }));
        }
        Map<Integer, ItemLite> itemMap = itemService.getItemLitesByIds(itemIds);

        List<OrderInfo> retOrderInfo = new ArrayList<>();
        for (OrdersModel ordersModel : orderModelList) {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderid(ordersModel.getId());
            orderInfo.setStatus(ordersModel.getStatus());
            orderInfo.setTotalFee(ordersModel.getTotalfee());
            orderInfo.setOrderFee(ordersModel.getOrderfee());
            orderInfo.setDeliveryFee(ordersModel.getDeliveryfee());
            orderInfo.setPayType(ordersModel.getPaytype());
            DeliveryInfoModel deliveryInfoModel = deliveryInfoMap.get(ordersModel.getId());
            if (deliveryInfoModel != null) {
                orderInfo.setInvoiceType(deliveryInfoModel.getInvoicetype());
                orderInfo.setInvoiceComment(deliveryInfoModel.getInvoicecomment());
                AddressInfo addressInfo = JSON.parseObject(deliveryInfoModel.getAddress(),
                        AddressInfo.class);
                orderInfo.setAddress(addressInfo);
            } else {
                logger.error("orderId=" + ordersModel.getId() + " is not exist DeliveryInfoModel");
            }
            orderInfo.setOrderTime(ordersModel.getOrdertime());
            orderInfo.setPayTime(ordersModel.getPaytime());

            List<ItemInfo> itemInfos = new ArrayList<>(2);
            for (OrderItemsModel orderitem : multiOrderItemList.get(ordersModel.getId())) {
                ItemInfo itemInfo = new ItemInfo();
                itemInfo.setSkuid(orderitem.getSkuid());
                itemInfo.setItemid(orderitem.getItemid());
                itemInfo.setPrice(orderitem.getSaleprice());
                ItemLite itemLite = itemMap.get(Integer.valueOf(String.valueOf(orderitem
                        .getItemid())));
                if (itemLite != null) {
                    itemInfo.setTitle(itemLite.getName());
                } else {
                    logger.error("itemid=" + orderitem.getItemid() + " is not exist");
                }
                itemInfo.setQuantity(orderitem.getQuantity());
                itemInfo.setTypes(this.genItemTypeInfo(orderitem.getSkuid()));
                itemInfos.add(itemInfo);
            }

            orderInfo.setItemInfo(itemInfos);

            retOrderInfo.add(orderInfo);
        }

        return Transformers.transformAsOneToOneMap(retOrderInfo, new Function<OrderInfo, Long>() {

            @Override
            public Long apply(OrderInfo input) {
                return input.getOrderid();
            }
        });
    }

    /**
     * 给客户端返回订单基本信息
     * 
     * @param order
     * @return
     */
    public OrderInfo genOrderInfo(OrdersModel order) {
        DeliveryInfoModel deliveryInfoModel = deliveryInfoDAO.getByOrderId(order.getId());
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderid(order.getId());
        orderInfo.setStatus(order.getStatus());
        orderInfo.setTotalFee(order.getTotalfee());
        orderInfo.setOrderFee(order.getOrderfee());
        orderInfo.setDeliveryFee(order.getDeliveryfee());
        orderInfo.setPayType(order.getPaytype());
        orderInfo.setInvoiceType(deliveryInfoModel.getInvoicetype());
        orderInfo.setInvoiceComment(deliveryInfoModel.getInvoicecomment());
        orderInfo.setOrderTime(order.getOrdertime());
        orderInfo.setPayTime(order.getPaytime());

        /**
         * TODO 1、若已发货，则获取发货信息
         */
        AddressInfo addressInfo = JSON.parseObject(deliveryInfoModel.getAddress(),
                AddressInfo.class);
        orderInfo.setAddress(addressInfo);
        if (order.hasDeliveried()) {
            orderInfo.setDeliveryInfo(orderTraceService.updateGenDeliveryInfo(order.getId(),
                    OrderTraceModel.TYPE_DEFAULT));
        }

        //if (order.hasRefund()) {
        List<RefundInfo> refundInfos = refundService.getRefundInfoByOrderId(order.getId());
        orderInfo.setRefundInfo(refundInfos);
        //}

        List<ItemInfo> itemInfos = new ArrayList<>();
        for (OrderItemsModel orderitem : orderItemsDAO.getListByOrderId(order.getId())) {
            ItemInfo itemInfo = new ItemInfo();
            itemInfo.setSkuid(orderitem.getSkuid());
            itemInfo.setItemid(orderitem.getItemid());
            itemInfo.setPrice(orderitem.getSaleprice());
            Item item = itemService.getItemById(orderitem.getItemid());
            itemInfo.setTitle(item.getName());
            Image image = imageService.getImageById(Long.parseLong(item.getImage()));
            if (image != null) {
                itemInfo.setImage(image.getUrl());
            }
            itemInfo.setQuantity(orderitem.getQuantity());
            itemInfo.setTypes(this.genItemTypeInfo(orderitem.getSkuid()));
            itemInfos.add(itemInfo);
        }

        orderInfo.setItemInfo(itemInfos);
        return orderInfo;
    }

    public List<ItemTypeInfo> genItemTypeInfo(long skuid) {
        ItemSku itemSku = itemSkuService.getItemSkuById(skuid);
        List<ItemPropertyView> itemPropertyViews = itemPropertyRender.renderProp(itemSku);
        Iterator iter = itemPropertyViews.iterator();
        List<ItemTypeInfo> itemTypeInfos = new ArrayList<>();
        for (ItemPropertyView itemPropertyView : itemPropertyViews) {
            ItemTypeInfo itemTypeInfo = new ItemTypeInfo();
            itemTypeInfo.setValue(itemPropertyView.getPropertyValue().getName());
            itemTypeInfo.setName(itemPropertyView.getProperty().getName());
            itemTypeInfos.add(itemTypeInfo);
        }
        return itemTypeInfos;
    }

    /**
     * 获取单个订单信息
     * 
     * @param orderid
     * @return
     */
    public ResultView<OrderInfo> getById(long orderid) {
        /**
         * TODO 验证订单是否属于用户
         */
        OrdersModel order = ordersDAO.getById(orderid);
        if (order == null) {
            return new ResultView<>(OrdersErrorEnum.ORDER_NOT_EXIST);
        }
        if (order.getStatus() == OrdersModel.STATUS_CLOSED) {
            return new ResultView<>(OrdersErrorEnum.ORDER_CLOSED_ERROR);
        }
        long userid = HehuaRequestContext.getUserId();
        if (order.getUserid() != userid) {
            return new ResultView<>(OrdersErrorEnum.ORDER_NOT_YOURS);
        }

        return new ResultView<>(CommonMetaCode.Success, this.genOrderInfo(order));

    }

    /**
     * 还没下单，获取订单信息
     * 
     * @param param
     * @return
     */
    public ResultView<OrderServiceConfirmRetParam> confirmBeforeOrder(OrderServiceConfirmParam param) {
        long userid = param.getUserid();
        if (param.getItems() == null) {
            return new ResultView<>(OrdersErrorEnum.PARAM_ERROR);
        }
        OrderServiceConfirmRetParam result = new OrderServiceConfirmRetParam();
        BigDecimal orderFee = new BigDecimal(0);
        List<OrderItemsModel> orderItems = new ArrayList<>();
        for (SkuInfo skuInfo : param.getItems()) {
            if (skuInfo.getSkuid() == 0 || skuInfo.getQuantity() == 0) {
                return new ResultView<>(OrdersErrorEnum.PARAM_ERROR);
            }
            ItemSku itemSku = itemSkuService.getItemSkuById(skuInfo.getSkuid());
            if (itemSku == null) {
                return new ResultView<>(OrdersErrorEnum.SKU_NOT_EXIST);
            }
            if (skuInfo.getQuantity() < 0 || (itemSku.getQuantity() - skuInfo.getQuantity() < 0)) {
                return new ResultView<>(OrdersErrorEnum.QUANTITY_ERROR);
            }
            if (!itemService.isSellable(itemSku.getItemid())) {
                return new ResultView<>(OrdersErrorEnum.ITEM_CAN_NOT_BUY);
            }

            PurchaseRestrictionPolicyCheckResult checkResult = purchaseRestrictionService
                    .checkRestrictionPolicies((int) userid, 0, (int) itemSku.getItemid(),
                            (int) itemSku.getId(), skuInfo.getQuantity(),
                            PurchaseOperation.ConfirmBeforeOrder);
            if (!checkResult.isAccept()) {
                return new ResultView<>(OrdersErrorEnum.QUANTITY_RESTRICTION);
            }
            /**
             * TODO 1、获取用户默认支付方式
             */
            OrderItemsModel orderItem = new OrderItemsModel();
            orderItem.setItemid(itemSku.getItemid());
            orderItem.setSaleprice(new BigDecimal(itemSku.getRealprice()));
            orderItem.setQuantity(skuInfo.getQuantity());
            orderItems.add(orderItem);

            orderFee = orderFee.add(new BigDecimal(itemSku.getRealprice() * skuInfo.getQuantity()));
        }
        BigDecimal deliveryFee = this.calcDeliveryFee(orderItems);
        result.setDeliveryFee(DecimalUtil.toStandard(deliveryFee));
        BigDecimal totalFee = orderFee.add(deliveryFee);
        result.setOrderFee(DecimalUtil.toStandard(orderFee));
        result.setTotalFee(DecimalUtil.toStandard(totalFee));
        result.setItems(param.getItems());

        result.setDefaultPayType(1);
        result.setPayTypes(BasePayProvider.getSupportedPayTypes());

        Address defaultAddress = addressService.getDefaultAddress(userid);
        if (defaultAddress == null) {
            defaultAddress = addressService.getRecentAddress(userid);
        }
        if (defaultAddress != null) {
            AddressInfo addressInfo = deliveryService.getAddressInfo(defaultAddress);
            result.setDefaultAddress(addressInfo);
        }

        String tips = databaseConfigManager.getString(KEY_ORDER_TIPS);
        result.setTips(tips);

        return new ResultView<>(CommonMetaCode.Success, result);
    }

    /**
     * 下单之后，支付之前，获取订单信息
     * 
     * @param orderid
     * @return
     */
    public ResultView<OrderServiceConfirmRetParam> confirmAfterOrder(long orderid) {
        OrdersModel order = ordersDAO.getById(orderid);
        if (order == null) {
            return new ResultView<>(OrdersErrorEnum.ORDER_NOT_EXIST);
        }
        if (!order.canPay()) {
            return new ResultView<>(OrdersErrorEnum.ORDER_STATUS_ERROR);
        }
        long userid = HehuaRequestContext.getUserId();
        if (order.getUserid() != userid) {
            return new ResultView<>(OrdersErrorEnum.ORDER_NOT_YOURS);
        }
        OrderServiceConfirmRetParam result = new OrderServiceConfirmRetParam();
        result.setTotalFee(order.getTotalfee());
        result.setOrderFee(order.getOrderfee());
        result.setDeliveryFee(order.getDeliveryfee());
        List<OrderItemsModel> orderItems = orderItemsDAO.getListByOrderId(orderid);
        List<SkuInfo> skuInfos = new ArrayList<>();
        for (OrderItemsModel orderItem : orderItems) {
            SkuInfo skuInfo = new SkuInfo();
            skuInfo.setSkuid(orderItem.getSkuid());
            skuInfo.setQuantity(orderItem.getQuantity());
            skuInfos.add(skuInfo);
        }
        result.setItems(skuInfos);

        /**
         * TODO 获取用户默认支付方式
         */
        result.setDefaultPayType(1);
        result.setPayTypes(BasePayProvider.getSupportedPayTypes());

        DeliveryInfoModel deliveryInfo = deliveryInfoDAO.getByOrderId(orderid);
        result.setInvoiceType(deliveryInfo.getInvoicetype());
        result.setInvoiceComment(deliveryInfo.getInvoicecomment());

        AddressInfo defaultAddressInfo = JSON.parseObject(deliveryInfo.getAddress(),
                AddressInfo.class);
        Address defaultAddress = addressService.getAddressById(defaultAddressInfo.getId());
        if (defaultAddress == null) {
            defaultAddress = addressService.getRecentAddress(userid);
        }
        if (defaultAddress != null) {
            result.setDefaultAddress(deliveryService.getAddressInfo(defaultAddress));
        }

        String tips = databaseConfigManager.getString(KEY_ORDER_TIPS);
        result.setTips(tips);

        return new ResultView<>(CommonMetaCode.Success, result);
    }

    /**
     * 获取订单商品评价信息
     * 
     * @param orderid
     * @return
     */
    public List<OrderItemFeedbackInfo> getOrderItemsFeedbackInfo(long orderid) {
        HashMap<Long, FeedbackModel> skuIdFeedbackMap = feedbackService.getListByOrderId(orderid);
        List<OrderItemsModel> orderItemsModels = orderItemsDAO.getListByOrderId(orderid);
        List<OrderItemFeedbackInfo> result = new ArrayList<>();
        for (OrderItemsModel orderitem : orderItemsModels) {
            ItemSku itemSku = itemSkuService.getItemSkuById(orderitem.getSkuid());
            Item item = itemService.getItemById(orderitem.getItemid());
            OrderItemFeedbackInfo param = new OrderItemFeedbackInfo();
            ItemInfo itemInfo = new ItemInfo();
            itemInfo.setSkuid(orderitem.getSkuid());
            itemInfo.setItemid(orderitem.getItemid());
            itemInfo.setPrice(new BigDecimal(itemSku.getRealprice()));
            itemInfo.setTitle(item.getName());
            Image image = imageService.getImageById(Long.parseLong(item.getImage()));
            if (image != null) {
                itemInfo.setImage(image.getUrl());
            }
            itemInfo.setQuantity(itemSku.getQuantity());

            itemInfo.setTypes(this.genItemTypeInfo(orderitem.getSkuid()));
            param.setItemInfo(itemInfo);
            param.setCanFeedback(true);
            if (skuIdFeedbackMap.containsKey(orderitem.getSkuid())) {
                FeedbackModel feedback = skuIdFeedbackMap.get(orderitem.getSkuid());
                param.setComment(feedback.getComment());
                param.setTime(feedback.getModtime());
            }
            result.add(param);
        }
        return result;
    }

    /**
     * 计算物流费
     * 
     * @param orderItems
     * @return
     */
    public BigDecimal calcDeliveryFee(List<OrderItemsModel> orderItems) {
        return new BigDecimal(0);
        /*
        BigDecimal deliveryFee = new BigDecimal(8);
        BigDecimal costMoney = new BigDecimal(0);
        BigDecimal fee158 = new BigDecimal(158);

        for (OrderItemsModel orderItem : orderItems) {
            Item item = itemService.getItemById(orderItem.getItemid());
            int postageType = item.getPostagetype();
            //0：免邮,当前没有免邮的商品 1：满158免邮 2：无条件8元邮费
            if (postageType != 1) {
                continue;
            }
            costMoney = costMoney.add(orderItem.getSaleprice().multiply(new BigDecimal(orderItem.getQuantity())));
        }
        if (costMoney.subtract(fee158).doubleValue() > 0.001) {
            deliveryFee = new BigDecimal(0);
        }
        return deliveryFee;
        */
    }

    /**
     * 标记订单为已发货
     * 
     * @param order
     */
    public boolean setDeliveried(OrdersModel order) {
        order.setDeliveried();
        ordersDAO.setDeliveried(order);

        User user = userManager.getUserById(order.getUserid());
        String template = databaseConfigManager.getString(OrderConfig.DELIVERY_SMSNOTIFY_TEMPLATE);
        String msg = template.replace("USERNAME", user.getName()).replace("ORDERID",
                String.valueOf(order.getId()));
        eventDispatcher.post(new SmsEvent(user.getAccount(), msg, new Date()));
        log.info("send deliveried sms, mobile=" + user.getAccount() + ", msg=" + msg);
        return true;
    }

    /**
     * 设为已签收
     * 
     * @param order
     */
    public boolean setSigned(OrdersModel order) {
        order.setSigned();
        ordersDAO.updateStatus(order.getId(), order.getStatus());

        User user = userManager.getUserById(order.getUserid());
        String template = databaseConfigManager.getString(OrderConfig.SIGN_SMSNOTIFY_TEMPLATE);
        String msg = template.replace("USERNAME", user.getName()).replace("ORDERID",
                String.valueOf(order.getId()));
        eventDispatcher.post(new SmsEvent(user.getAccount(), msg, new Date()));
        log.info("send signed sms, mobile=" + user.getAccount() + ", msg=" + msg);
        return true;
    }

    /**
     * 标记订单为已发送至paiu
     * 
     * @param order
     */
    public void setSendedToPaiu(OrdersModel order) {
        order.setSendedToPaiu();
        ordersDAO.updateAttr(order);
    }

    public OrdersModel getByOrderId(long id) {
        return ordersDAO.getById(id);
    }
}
