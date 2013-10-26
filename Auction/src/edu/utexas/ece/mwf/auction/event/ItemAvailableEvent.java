package edu.utexas.ece.mwf.auction.event;

/**
 * Created with IntelliJ IDEA.
 * User: yamini
 * Date: 10/21/13
 * Time: 5:41 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * Sets and retrieves various parameter of the items available
 */
public class ItemAvailableEvent extends Event {

    private Long itemId;
    private String name;
    private String type;
    private String tag;
    private Double minimumBid;

    public ItemAvailableEvent() {
    }

    public ItemAvailableEvent(Long itemId, String name, String type, String tag, Double minimumBid) {
        this.itemId = itemId;
        this.name = name;
        this.type = type;
        this.tag = tag;
        this.minimumBid = minimumBid;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Double getMinimumBid() {
        return minimumBid;
    }

    public void setMinimumBid(Double minimumBid) {
        this.minimumBid = minimumBid;
    }
}
