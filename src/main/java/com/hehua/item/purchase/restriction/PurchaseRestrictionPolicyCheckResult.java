package com.hehua.item.purchase.restriction;

/**
 * @author zhihua
 *
 */
public class PurchaseRestrictionPolicyCheckResult {

    private boolean accept;

    private String rejectMessage;

    /**
     * 
     */
    public PurchaseRestrictionPolicyCheckResult() {
        super();
    }

    /**
     * @param accept
     * @param rejectMessage
     */
    public PurchaseRestrictionPolicyCheckResult(boolean accept, String rejectMessage) {
        super();
        this.accept = accept;
        this.rejectMessage = rejectMessage;
    }

    public boolean isAccept() {
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }

    public String getRejectMessage() {
        return rejectMessage;
    }

    public void setRejectMessage(String rejectMessage) {
        this.rejectMessage = rejectMessage;
    }

}
