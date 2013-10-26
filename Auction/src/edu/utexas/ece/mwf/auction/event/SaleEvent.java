package edu.utexas.ece.mwf.auction.event;

/**
 * Created with IntelliJ IDEA.
 * User: yamini
 * Date: 10/24/13
 * Time: 1:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class SaleEvent extends Event {

    private Long itemId;
    private Integer buyerId;
    private Double price;

    public SaleEvent() {
    }

    public SaleEvent(Long itemId, Integer buyerId, Double price) {
        this.itemId = itemId;
        this.buyerId = buyerId;
        this.price = price;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Integer getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Integer buyerId) {
        this.buyerId = buyerId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
