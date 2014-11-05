/**
 * 
 */
package com.hehua.item.purchase.restriction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhihua
 *
 */
@Service
public class PurchaseRestrictionService {

    @Autowired
    private PurchaseRestrictionPolicyManager purchaseRestrictionPolicyManager;

    @Autowired
    private PurchaseRestrictionPolicyHandlerManager purchaseRestrictionPolicyHandlerManager;

    public PurchaseRestrictionPolicyCheckResult checkRestrictionPolicies(int userId, int flashId,
            int itemId, int skuId, int quantity, PurchaseOperation operation) {
        return checkRestrictionPolicies(userId, flashId, itemId, skuId, quantity, operation, null,
                null, null);

    }

    public PurchaseRestrictionPolicyCheckResult checkRestrictionPolicies(int userId, int flashId,
            int itemId, int skuId, int quantity, PurchaseOperation operation,
            String receiverMobile, String clientId, String deviceId) {

        PurchaseRestrictionPolicy policy = purchaseRestrictionPolicyManager
                .getItemPurchaseRestrictionPolicy(itemId);
        if (policy != null) {
            return checkRestrictionPolicy(userId, flashId, itemId, skuId, quantity, operation,
                    policy, receiverMobile, clientId, deviceId);
        }
        return new PurchaseRestrictionPolicyCheckResult(true, null);
    }

    /**
     * @param userId
     * @param flashId
     * @param itemId
     * @param skuId
     * @param policy
     */
    public PurchaseRestrictionPolicyCheckResult checkRestrictionPolicy(int userId, int flashId,
            int itemId, int skuId, int quantity, PurchaseOperation operation,
            PurchaseRestrictionPolicy policy, String receiverMobile, String clientId,
            String deviceId) {
        PurchaseRestrictionPolicyHandler purchaseRestrictionPolicyHandler = purchaseRestrictionPolicyHandlerManager
                .getPurchaseRestrictionPolicyHandler(policy.getType());
        return purchaseRestrictionPolicyHandler.check(userId, itemId, flashId, skuId, quantity,
                operation, policy, receiverMobile, clientId, deviceId);
    }
}
