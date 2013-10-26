package edu.utexas.ece.mwf.auction.client;

import edu.utexas.ece.mwf.auction.enums.BidType;
import edu.utexas.ece.mwf.auction.enums.ClientType;
import edu.utexas.ece.mwf.auction.event.BidEvent;
import edu.utexas.ece.mwf.auction.event.ItemAvailableEvent;
import edu.utexas.ece.mwf.auction.event.SaleEvent;
import edu.utexas.ece.mwf.auction.listener.BuyerListener;
import edu.utexas.ece.mwf.auction.subscription.BidSubscription;
import edu.utexas.ece.mwf.auction.subscription.ItemInterestSubscription;
import edu.utexas.ece.mwf.auction.subscription.ItemSoldSubscription;
import edu.utexas.ece.mwf.auction.userInterface.BuyerFrame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA. User: yamini Date: 10/24/13 Time: 3:22 AM To
 * change this template use File | Settings | File Templates.
 */

/**
 * This class represents the different methods required for defining the buyer
 * actions
 */

public class Buyer extends Client {

    private BuyerFrame buyerFrame;
    private Map<ItemInterestSubscription, List<ItemAvailableEvent>> itemInterestSubscriptionMap = new HashMap<ItemInterestSubscription, List<ItemAvailableEvent>>();

    /**
     * Initialize method establishes a broker connection with a broker Registers
     * the buyer with the broker Start a listner thread for the buyer to listnen
     * on to different event triggers
     */
    public void initialize(BuyerFrame buyerFrame) throws IOException {
        retrieveBrokerConnection();
        registerWithBroker(ClientType.BUYER);
        listenForEvents(new BuyerListener(this));
        this.buyerFrame = buyerFrame;
    }

    /**
     * A method the process of matching the interests with that of the items
     * available for sale (pulished by the seller)
     */
    public void subscribeForAvailableItem(String name, String type, String tag, Double minimumBid, Boolean manual, Double bidIncrement, Double maximumBidAmount) {
        ItemInterestSubscription subscription = new ItemInterestSubscription(name, type, tag, minimumBid, manual, bidIncrement, maximumBidAmount);
        subscribe(subscription);
        itemInterestSubscriptionMap.put(subscription, new ArrayList<ItemAvailableEvent>());
    }

    /**
     * Registering the subcribed item and saving the subscription details for
     * future use
     */
    public void addItemAvailableEvent(ItemAvailableEvent itemAvailableEvent) {
        getItemAvailableEventMap().put(itemAvailableEvent.getItemId(), itemAvailableEvent);
        subscribeForBidUpdate(itemAvailableEvent.getItemId());
        subscribeForItemSale(itemAvailableEvent.getItemId());
        buyerFrame.setTableData(this);
        for (ItemInterestSubscription subscription : itemInterestSubscriptionMap.keySet()) {
            if (isMatchingItem(itemAvailableEvent, subscription)) {
                itemInterestSubscriptionMap.get(subscription).add(itemAvailableEvent);
            }
        }
    }

    public void subscribeForBidUpdate(Long itemId) {
        BidSubscription subscription = new BidSubscription(itemId, BidType.UPDATE);
        subscribe(subscription);
    }

    public void subscribeForItemSale(Long itemId) {
        ItemSoldSubscription subscription = new ItemSoldSubscription(itemId);
        subscribe(subscription);
    }

    /**
     * This method Captures a bid event the based on the mode of buying selected
     * (User mode / automatic mode) publishes a bid update or performs a rebid
     * when ever the current bid on the item is greater that the previous bid
     * item
     */
    public void addBidUpdateEvent(BidEvent bidEvent) {
        getUpdateBidEventMap().put(bidEvent.getItemId(), bidEvent);
        buyerFrame.setTableData(this);
        if (!getBrokerConnection().getSelfPort().equals(bidEvent.getBidderId())) {
            for (ItemInterestSubscription subscription : itemInterestSubscriptionMap.keySet()) {
                List<ItemAvailableEvent> itemAvailableEventList = itemInterestSubscriptionMap.get(subscription);
                for (ItemAvailableEvent itemAvailableEvent : itemAvailableEventList) {
                    if (!subscription.getManual()) {
                        if (itemAvailableEvent.getItemId().equals(bidEvent.getItemId()) && subscription.getMaximumBidAmount() > (bidEvent.getPrice() + subscription.getBidIncrement())) {
                            publishBidEvent(bidEvent.getItemId(), bidEvent.getPrice() + subscription.getBidIncrement());
                        }
                    }

                }
            }
        }
    }

    public void addSaleEvent(SaleEvent saleEvent) {
        getSaleEventMap().put(saleEvent.getItemId(), saleEvent);
        getItemAvailableEventMap().remove(saleEvent.getItemId());
        for (ItemInterestSubscription subscription : itemInterestSubscriptionMap.keySet()) {
            List<ItemAvailableEvent> itemAvailableEventList = itemInterestSubscriptionMap.get(subscription);
            List<ItemAvailableEvent> updatedItemAvailableEventList = new ArrayList<ItemAvailableEvent>();
            for (ItemAvailableEvent itemAvailableEvent : itemAvailableEventList) {
                if (!saleEvent.getItemId().equals(itemAvailableEvent.getItemId())) {
                    updatedItemAvailableEventList.add(itemAvailableEvent);
                }
            }
            itemInterestSubscriptionMap.put(subscription, updatedItemAvailableEventList);
        }
        buyerFrame.setTableData(this);
    }

    public void publishBidEvent(Long itemId, Double price) {
        BidEvent bidEvent = new BidEvent(itemId, price, BidType.NEW, getBrokerConnection().getSelfPort());
        publishEvent(bidEvent);
        getNewBidEventMap().put(itemId, bidEvent);
        buyerFrame.setTableData(this);
    }

    /**
     * Defining a Matching function to publish the matched interests to the a
     * buyer who has requested for a similar interest
     */
    private boolean isMatchingItem(ItemAvailableEvent itemAvailableEvent, ItemInterestSubscription itemInterestSubscription) {
        boolean nameMatches = isBlank(itemInterestSubscription.getName());
        boolean tagMatches = isBlank(itemInterestSubscription.getTag());
        boolean typeMatches = isBlank(itemInterestSubscription.getType());
        boolean minimumBidMatches = (itemInterestSubscription.getMinimumBid() == null || itemInterestSubscription.getMinimumBid().equals(Double.valueOf(0)));

        if (!nameMatches && itemInterestSubscription.getName().trim().equalsIgnoreCase(itemAvailableEvent.getName())) {
            nameMatches = true;
        }

        if (!tagMatches && itemInterestSubscription.getTag().trim().equalsIgnoreCase(itemAvailableEvent.getTag())) {
            tagMatches = true;
        }

        if (!typeMatches && itemInterestSubscription.getType().trim().equalsIgnoreCase(itemAvailableEvent.getType())) {
            typeMatches = true;
        }

        if (!minimumBidMatches && itemInterestSubscription.getMinimumBid().equals(itemAvailableEvent.getMinimumBid())) {
            minimumBidMatches = true;
        }

        return (nameMatches || tagMatches || typeMatches || minimumBidMatches);
    }

    private boolean isBlank(String text) {
        return (text == null || text.trim().equals(""));
    }

}
