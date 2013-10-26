package edu.utexas.ece.mwf.auction.event;

import edu.utexas.ece.mwf.auction.enums.BidType;

/**
 * User: yamini Date: 10/24/13 Time: 1:35 AM
 * <p/>
 * Store the parameters of a bid event
 */
public class BidEvent extends Event {

    private Long itemId;
    private Double price;
    private BidType type;
    private Integer bidderId;

    public BidEvent() {
    }

    public BidEvent(Long itemId, Double price, BidType type, Integer bidderId) {
        this.itemId = itemId;
        this.price = price;
        this.type = type;
        this.bidderId = bidderId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public BidType getType() {
        return type;
    }

    public void setType(BidType type) {
        this.type = type;
    }

    public Integer getBidderId() {
        return bidderId;
    }

    public void setBidderId(Integer bidderId) {
        this.bidderId = bidderId;
    }

}
