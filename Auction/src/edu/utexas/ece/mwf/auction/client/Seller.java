package edu.utexas.ece.mwf.auction.client;

import java.io.IOException;
import java.util.Date;

import edu.utexas.ece.mwf.auction.enums.BidType;
import edu.utexas.ece.mwf.auction.enums.ClientType;
import edu.utexas.ece.mwf.auction.event.BidEvent;
import edu.utexas.ece.mwf.auction.event.ItemAvailableEvent;
import edu.utexas.ece.mwf.auction.event.SaleEvent;
import edu.utexas.ece.mwf.auction.listener.SellerListener;
import edu.utexas.ece.mwf.auction.subscription.BidSubscription;
import edu.utexas.ece.mwf.auction.userInterface.SellerFrame;

/**
 * Created with IntelliJ IDEA. User: yamini Date: 10/23/13 Time: 3:47 PM To
 * change this template use File | Settings | File Templates.
 */
public class Seller extends Client {

    private SellerFrame sellerFrame;

    public void initialize(SellerFrame sellerFrame) throws IOException {
        retrieveBrokerConnection();
        registerWithBroker(ClientType.SELLER);
        listenForEvents(new SellerListener(this));
        this.sellerFrame = sellerFrame;
    }

    /**
     * This method add's a bid to the available item and sends a bid update
     * event
     *
     * @param bidEvent
     */
    public void addBid(BidEvent bidEvent) {
        /* Check if the new bid is greater than the existing bid */
        BidEvent existingBidEvent = getNewBidEventMap().get(bidEvent.getItemId());
        if (existingBidEvent == null || existingBidEvent.getPrice() == null || bidEvent.getPrice() > existingBidEvent.getPrice()) {
            getNewBidEventMap().put(bidEvent.getItemId(), bidEvent);
            publishBidUpdateEvent(bidEvent);
        }
    }

    /**
     * This method published the Item available Event
     *
     * @param name
     * @param type
     * @param tag
     * @param minimumBid
     * @return
     */
    public ItemAvailableEvent publishItemAvailableEvent(String name, String type, String tag, Double minimumBid) {
        ItemAvailableEvent itemAvailableEvent = new ItemAvailableEvent(new Date().getTime(), name, type, tag, minimumBid);
        publishEvent(itemAvailableEvent);
        getItemAvailableEventMap().put(itemAvailableEvent.getItemId(), itemAvailableEvent);
        BidSubscription bidSubscription = new BidSubscription(itemAvailableEvent.getItemId(), BidType.NEW);
        subscribe(bidSubscription);
        return itemAvailableEvent;
    }

    /**
     * This method publishes the sale event
     *
     * @param itemId
     * @param buyerId
     * @param price
     */
    public void publishSaleEvent(Long itemId, Integer buyerId, Double price) {
        SaleEvent saleEvent = new SaleEvent(itemId, buyerId, price);
        publishEvent(saleEvent);
        getSaleEventMap().put(saleEvent.getItemId(), saleEvent);
        getItemAvailableEventMap().remove(itemId);
        sellerFrame.setTableData(this);
    }

    private void publishBidUpdateEvent(BidEvent bidEvent) {
        BidEvent bidUpdateEvent = new BidEvent(bidEvent.getItemId(), bidEvent.getPrice(), BidType.UPDATE, bidEvent.getBidderId());
        publishEvent(bidUpdateEvent);
        getUpdateBidEventMap().put(bidEvent.getItemId(), bidEvent);
        sellerFrame.setTableData(this);
    }

}
