package edu.utexas.ece.mwf.auction.subscription;

import edu.utexas.ece.mwf.auction.enums.BidType;

/**
 * Created with IntelliJ IDEA.
 * User: yamini
 * Date: 10/24/13
 * Time: 2:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class BidSubscription extends Subscription {

    private Long itemId;
    private BidType type;

    public BidSubscription() {
    }

    public BidSubscription(Long itemId, BidType type) {
        this.itemId = itemId;
        this.type = type;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public BidType getType() {
        return type;
    }

    public void setType(BidType type) {
        this.type = type;
    }
}
