/**
 * 
 */
package com.hehua.item.purchase.restriction;

/**
 * @author zhihua
 *
 */
public interface PurchaseRestrictionPolicyHandler {

    public PurchaseRestrictionPolicyType getType();

    public PurchaseRestrictionPolicyCheckResult check(int userId, int itemId, int flashId,
            int skuId, int quantity, PurchaseOperation operation,
            PurchaseRestrictionPolicy restriction, String receiverMobile, String clientId,
            String deviceId);
}
