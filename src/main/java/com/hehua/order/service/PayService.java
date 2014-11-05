package com.hehua.order.service;

import com.alibaba.fastjson.JSON;
import com.hehua.commons.model.CommonMetaCode;
import com.hehua.commons.model.ResultView;
import com.hehua.framework.web.HehuaRequestContext;
import com.hehua.item.domain.ItemSku;
import com.hehua.item.service.ItemSkuService;
import com.hehua.order.dao.DeliveryInfoDAO;
import com.hehua.order.dao.OrderItemsDAO;
import com.hehua.order.dao.OrdersDAO;
import com.hehua.order.enums.OrdersErrorEnum;
import com.hehua.order.exceptions.NoSuchPayConfigException;
import com.hehua.order.model.DeliveryInfoModel;
import com.hehua.order.model.OrderItemsModel;
import com.hehua.order.model.OrdersModel;
import com.hehua.order.pay.BasePayProvider;
import com.hehua.order.pay.PayManager;
import com.hehua.order.service.params.PayServicePayParam;
import com.hehua.order.service.params.PayServicePayRetParam;
import com.hehua.user.domain.Address;
import com.hehua.user.service.AddressService;
import org.apache.log4j.Logger;
import org.springframework.core.annotation.Order;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Created by liuweiwei on 14-7-30.
 */
@Named
public class PayService {

    static Logger log = Logger.getLogger(PayService.class.getName());

    @Inject
    private OrdersDAO ordersDAO;

    @Inject
    private OrderItemsDAO orderItemsDAO;

    @Inject
    private DeliveryInfoDAO deliveryInfoDAO;

    @Inject
    private PayManager payManager;

    @Inject
    private AddressService addressService;

    @Inject
    private DeliveryService deliveryService;

    @Inject
    private ItemSkuService itemSkuService;

    /**
     * 待付款状态订单支付
     * @param param
     * @return
     */
    public ResultView<PayServicePayRetParam> pay(PayServicePayParam param) {
        if (!(param.getOrderid() != 0 && param.getPayType() != 0)) {
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

        OrdersModel order = ordersDAO.getById(param.getOrderid());
        if (order == null) {
            return new ResultView<>(OrdersErrorEnum.ORDER_NOT_EXIST);
        }
        if (!order.canPay()) {
            return new ResultView<>(OrdersErrorEnum.ORDER_HAS_PAYED);
        }
        long userid = HehuaRequestContext.getUserId();
        if (order.getUserid() != userid) {
            return new ResultView<>(OrdersErrorEnum.ORDER_NOT_YOURS);
        }
        ResultView<PayServicePayRetParam> deliveryResult = this.updateDeliveryInfo(param);
        if (deliveryResult.getMeta().getCode() != CommonMetaCode.Success) {
            return deliveryResult;
        }
        List<OrderItemsModel> orderItemsModels = orderItemsDAO.getListByOrderId(order.getId());
        order.setOrderitems(orderItemsModels);
        //检查商品库存
        for (OrderItemsModel orderItemsModel : orderItemsModels) {
            ItemSku itemSku = itemSkuService.getItemSkuById(orderItemsModel.getSkuid());
            if (itemSku.getQuantity() - orderItemsModel.getQuantity() < 0) {
                log.info("超过限制时间未付款, orderid=" + order.getId());
                return new ResultView<>(OrdersErrorEnum.SKU_QUANTITY_ERROR);
            }
        }

        String url = payObj.getPayUrl(order);
        if (url.equalsIgnoreCase(BasePayProvider.FAIL_CODE)) {
            log.error("pay error");
            return new ResultView<>(OrdersErrorEnum.ORDER_PAY_ERROR);
        }

        PayServicePayRetParam response = new PayServicePayRetParam();
        response.setPayUrl(url);
        response.setOrderid(order.getId());
        return new ResultView<>(CommonMetaCode.Success, response);
    }

    /**
     * 更新物流、发票信息
     * @param param
     * @return
     */
    public ResultView<PayServicePayRetParam> updateDeliveryInfo(PayServicePayParam param) {
        if (param.getOrderid() == 0) {
            return new ResultView<>(OrdersErrorEnum.ORDER_NOT_EXIST);
        }
        DeliveryInfoModel deliveryInfoModel = deliveryInfoDAO.getByOrderId(param.getOrderid());
        if (deliveryInfoModel == null) {
            return new ResultView<>(OrdersErrorEnum.ORDER_DELIVERYINFO_NOT_EXITS);
        }
        Address address = addressService.getAddressById(param.getAddressid());
        //没有传地址ID
        if (address == null) {
            return new ResultView<>(CommonMetaCode.Success);
        }
        if (address.getUid() != HehuaRequestContext.getUserId()) {
            return new ResultView<>(OrdersErrorEnum.ADDRESS_NOT_YOURS);
        }
        deliveryInfoModel.setAddress(JSON.toJSONString(deliveryService.getAddressInfo(address)));

        if (!org.apache.commons.lang3.StringUtils.isBlank(param.getInvoiceComment())) {
            deliveryInfoModel.setInvoicecomment(param.getInvoiceComment());
        }

        if (DeliveryInfoModel.invoiceDesc.get(param.getInvoiceType()) != null) {
            deliveryInfoModel.setInvoicetype(param.getInvoiceType());
        }
        deliveryInfoDAO.update(deliveryInfoModel);
        return new ResultView<>(CommonMetaCode.Success);
    }
}
