/**
 * 
 */
package com.hehua.item.purchase.restriction;

/**
 * @author zhihua
 *
 */
public enum PurchaseRestrictionPolicyType {

    QuantityRestrictionPerItemPerUser(1),

    ;

    private final int code;

    /**
     * @param code
     */
    private PurchaseRestrictionPolicyType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
