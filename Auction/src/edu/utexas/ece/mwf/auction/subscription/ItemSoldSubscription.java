package edu.utexas.ece.mwf.auction.subscription;

/**
 * Created with IntelliJ IDEA. User: yamini Date: 10/24/13 Time: 2:04 AM To
 * change this template use File | Settings | File Templates.
 */
public class ItemSoldSubscription extends Subscription {

    private Long itemId;

    public ItemSoldSubscription(Long itemId) {
        super();
        this.itemId = itemId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
}
