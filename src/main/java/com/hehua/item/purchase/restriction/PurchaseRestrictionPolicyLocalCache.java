/**
 * 
 */
package com.hehua.item.purchase.restriction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehua.framework.localcache.AbstractLocalCache;

/**
 * @author zhihua
 *
 */
@Component
public class PurchaseRestrictionPolicyLocalCache extends
        AbstractLocalCache<Map<Integer, PurchaseRestrictionPolicy>> {

    @Autowired
    private PurchaseRestrictionPolicyDAO purchaseRestrictionPolicyDAO;

    @Override
    public Map<Integer, PurchaseRestrictionPolicy> load() {

        List<PurchaseRestrictionPolicy> policiesList = purchaseRestrictionPolicyDAO.getAll();
        Map<Integer, PurchaseRestrictionPolicy> policiesMap = new HashMap<>(policiesList.size());
        for (PurchaseRestrictionPolicy policy : policiesList) {
            policiesMap.put(policy.getItemid(), policy);
        }
        return policiesMap;
    }

    @Override
    public String key() {
        return "purchaseRestrictionPolicies";
    }

}
