package edu.utexas.ece.mwf.auction.subscription;

/**
 * Created with IntelliJ IDEA.
 * User: yamini
 * Date: 10/24/13
 * Time: 1:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class ItemInterestSubscription extends Subscription {

    private String name;
    private String type;
    private String tag;
    private Double minimumBid;
    private Boolean manual;
    private Double bidIncrement;
    private Double maximumBidAmount;

    public ItemInterestSubscription() {
    }

    public ItemInterestSubscription(String name, String type, String tag, Double minimumBid, Boolean manual, Double bidIncrement, Double maximumBidAmount) {
        this.name = name;
        this.type = type;
        this.tag = tag;
        this.minimumBid = minimumBid;
        this.manual = manual;
        this.bidIncrement = bidIncrement;
        this.maximumBidAmount = maximumBidAmount;
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

    public Boolean getManual() {
        return manual;
    }

    public void setManual(Boolean manual) {
        this.manual = manual;
    }

    public Double getBidIncrement() {
        return bidIncrement;
    }

    public void setBidIncrement(Double bidIncrement) {
        this.bidIncrement = bidIncrement;
    }

    public Double getMaximumBidAmount() {
        return maximumBidAmount;
    }

    public void setMaximumBidAmount(Double maximumBidAmount) {
        this.maximumBidAmount = maximumBidAmount;
    }
}
