/**
 * 
 */
package com.hehua.item.purchase.restriction;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehua.framework.subscribe.ZookeeperPubSubService;

/**
 * @author zhihua
 *
 */
@Component
public class PurchaseRestrictionPolicyManager {

    @Autowired
    private PurchaseRestrictionPolicyLocalCache purchaseRestrictionPolicyLocalCache;

    @Autowired
    private PurchaseRestrictionPolicyDAO purchaseRestrictionPolicyDAO;

    public PurchaseRestrictionPolicy getItemPurchaseRestrictionPolicy(int itemId) {
        return purchaseRestrictionPolicyLocalCache.get().get(itemId);
    }

    public void addPurchaseRestrictionPolicy(PurchaseRestrictionPolicy policy) {
        purchaseRestrictionPolicyDAO.insert(policy);
        ZookeeperPubSubService.getInstance().post(purchaseRestrictionPolicyLocalCache.key(),
                "reload");
    }

    public void delPurchaseRestrictionPolicy(int itemId) {
        purchaseRestrictionPolicyDAO.del(itemId);
        ZookeeperPubSubService.getInstance().post(purchaseRestrictionPolicyLocalCache.key(),
                "reload");
    }

    public Map<Integer, PurchaseRestrictionPolicy> getAll() {
        return purchaseRestrictionPolicyLocalCache.get();
    }
}
