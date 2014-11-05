/**
 * 
 */
package com.hehua.item.purchase.restriction.policy;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hehua.item.purchase.restriction.PurchaseOperation;
import com.hehua.item.purchase.restriction.PurchaseRestrictionPolicy;
import com.hehua.item.purchase.restriction.PurchaseRestrictionPolicyCheckResult;
import com.hehua.item.purchase.restriction.PurchaseRestrictionPolicyHandler;
import com.hehua.item.purchase.restriction.PurchaseRestrictionPolicyType;
import com.hehua.item.service.ItemService;
import com.hehua.item.service.ItemSkuService;
import com.hehua.order.info.OrderStatusCountInfo;
import com.hehua.order.model.OrdersModel;
import com.hehua.order.service.CartService;
import com.hehua.order.service.OrderService;
import com.hehua.order.service.OrdersService;
import com.hehua.user.service.RiskControlService;
import com.hehua.user.service.RiskLevel;

/**
 * @author zhihua
 *
 */
@Component
public class QuantityRestrictionPerItemPerUserPolicyHandler implements
        PurchaseRestrictionPolicyHandler {

    private static final Log logger = LogFactory.getLog("riskcontrol");

    public static final String DATA_KEY_LIMIT_QUANTITY = "limits";

    @Autowired
    private CartService cartService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemSkuService skuService;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderService orderService;

    @Inject
    private RiskControlService riskControlService;

    @Override
    public PurchaseRestrictionPolicyType getType() {
        return PurchaseRestrictionPolicyType.QuantityRestrictionPerItemPerUser;
    }

    private boolean isFirstPayUser(int userId) {
        List<OrderStatusCountInfo> orderCountByStatus = orderService.getCountByStatus(userId);

        int payedOrders = 0;
        for (OrderStatusCountInfo orderCount : orderCountByStatus) {
            // 注意这里的逻辑
            if (orderCount.getStatus() > OrdersModel.STATUS_CLOSED) {
                payedOrders += orderCount.getCount();
            }
        }
        return payedOrders <= 0;
    }

    @Override
    public PurchaseRestrictionPolicyCheckResult check(int userId, int itemId, int flashId,
            int skuId, int quantity, PurchaseOperation operation,
            PurchaseRestrictionPolicy restriction, String receiverMobile, String clientId,
            String deviceId) {

        JSONObject jsonObject = JSON.parseObject(restriction.getData());

        Integer limits = (Integer) jsonObject.get(DATA_KEY_LIMIT_QUANTITY);
        if (limits != null && limits > 0) {

            //TODO 是否首次支付
            if (!isFirstPayUser(userId)) {
                return new PurchaseRestrictionPolicyCheckResult(false, "该商品只有新用户才能购买");
            }

            if (quantity > limits) {
                return new PurchaseRestrictionPolicyCheckResult(false, "该商品只能购买" + limits + "件哦");
            }

            int count = 0;
            if (operation == PurchaseOperation.AddToCart) {
                count += getItemCountInCart(userId, itemId);
            }

            count += getItemCountInOrders(userId, itemId);

            if (count + quantity > limits) {
                return new PurchaseRestrictionPolicyCheckResult(false, "该商品只能购买" + limits + "件哦");
            }

            if (operation == PurchaseOperation.CreateOrder) {
                RiskLevel riskLevel = riskControlService.getRiskLevel(itemId, skuId,
                        receiverMobile, clientId, deviceId);
                if (riskLevel != RiskLevel.Safe) {
                    logger.error(String
                            .format("reject risk order: itemId = %s, skuId = %s, userId = %s, receiverMobile = %s, clientId = %s, deviceId = %s",
                                    itemId, skuId, userId, receiverMobile, clientId, deviceId));
                    return new PurchaseRestrictionPolicyCheckResult(false, "该商品只能购买" + limits
                            + "件哦");
                }
            }
        }

        return new PurchaseRestrictionPolicyCheckResult(true, null);
    }

    public int getItemCountInCart(int userId, int itemId) {
        //        int itemCount = 0;
        //        List<CartsInfo> cartItemList = cartService.getListByUserId(userId);
        //        for (CartsInfo cartItem : cartItemList) {
        //            if ((int) cartItem.getItemid() == itemId) {
        //                itemCount += cartItem.getQuantity();
        //            }
        //        }
        //        return itemCount;
        return cartService.getItemCountInCart(userId, itemId);
    }

    public int getItemCountInOrders(int userId, int itemId) {
        return orderService.getItemCountInUncloseOrders(userId, itemId);
    }

}
