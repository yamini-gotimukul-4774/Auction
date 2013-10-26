package edu.utexas.ece.mwf.auction.broker;

import edu.utexas.ece.mwf.auction.enums.ClientType;
import edu.utexas.ece.mwf.auction.event.*;
import edu.utexas.ece.mwf.auction.subscription.BidSubscription;
import edu.utexas.ece.mwf.auction.subscription.ItemInterestSubscription;
import edu.utexas.ece.mwf.auction.subscription.ItemSoldSubscription;
import edu.utexas.ece.mwf.auction.subscription.Subscription;
import edu.utexas.ece.mwf.auction.utils.SocketUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA. User: yamini Date: 10/22/13 Time: 1:53 AM To
 * change this template use File | Settings | File Templates.
 */
public class Broker implements Runnable {

    private Integer port;
    private Integer parentPort;
    private List<Integer> childPorts = new ArrayList<Integer>();
    private Map<Integer, List<Subscription>> sellerMap = new HashMap<Integer, List<Subscription>>();
    private Map<Integer, List<Subscription>> buyerMap = new HashMap<Integer, List<Subscription>>();
    private ServerSocket serverSocket;

    /**
     * creating a new broker after every two client request(either buyer or
     * seller)
     */

    public Broker(Integer port, Integer parentPort) {
        this.port = port;
        this.parentPort = parentPort;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used to
     * create a thread, starting the thread causes the object's <code>run</code>
     * method to be called in that separately executing thread.
     * <p/>
     * The general contract of the method <code>run</code> is that it may take
     * any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

        while (true) {
            Socket socket = null;
            InputStream inputStream = null;
            ObjectInputStream objectInputStream = null;
            try {
                socket = serverSocket.accept();
                inputStream = socket.getInputStream();
                objectInputStream = new ObjectInputStream(inputStream);
                Object object = objectInputStream.readObject();
                if (object instanceof Subscription) {
                    Subscription subscription = (Subscription) object;
                    if (buyerMap.get(subscription.getSubscriberPort()) != null) {
                        buyerMap.get(subscription.getSubscriberPort()).add(subscription);
                    } else {
                        sellerMap.get(subscription.getSubscriberPort()).add(subscription);
                    }
                } else if (object instanceof Event) {
                    Event event = (Event) object;
                    if (object.getClass().equals(RegisterEvent.class)) {
                        RegisterEvent registerEvent = (RegisterEvent) object;
                        register(registerEvent);
                    } else {
                        List<Integer> portsList = findSubscribingPorts(event);
                        portsList.addAll(findConnectedBrokers(event));
                        for (Integer port : portsList) {
                            SocketUtils.transmit(port, event);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    objectInputStream.close();
                    inputStream.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * This is a registering event between the Buyer and the Seller
     */
    private void register(RegisterEvent registerEvent) {
        if (registerEvent != null) {
            if (registerEvent.getType().equals(ClientType.SELLER)) {
                sellerMap.put(registerEvent.getPortNumber(), new ArrayList<Subscription>());
            } else if (registerEvent.getType().equals(ClientType.BUYER)) {
                buyerMap.put(registerEvent.getPortNumber(), new ArrayList<Subscription>());
            }
        }
    }

    /**
     * Finding the Subscribed client buyer
     */
    private List<Integer> findSubscribingPorts(Event event) {
        List<Integer> ports = new ArrayList<Integer>();
        if (buyerMap.keySet() != null && !buyerMap.keySet().isEmpty()) {
            for (Integer client : buyerMap.keySet()) {
                for (Subscription subscription : buyerMap.get(client)) {
                    if (isMatching(subscription, event)) {
                        ports.add(client);
                    }
                }
            }
        }
        if (sellerMap.keySet() != null && !sellerMap.keySet().isEmpty()) {
            for (Integer client : sellerMap.keySet()) {
                for (Subscription subscription : sellerMap.get(client)) {
                    if (isMatching(subscription, event)) {
                        ports.add(client);
                    }
                }
            }
        }
        return ports;
    }

    /**
     * Constructing the hierarchial statergy for connecting brokers at different
     * levels.Connecting the brokers in a tree structure
     */
    private List<Integer> findConnectedBrokers(Event event) {
        List<Integer> connectedBrokersPort = new ArrayList<Integer>();
        if (parentPort != null) {
            connectedBrokersPort.add(parentPort);
        }
        connectedBrokersPort.addAll(childPorts);
        if (event.getRelayPort() != null) {
            connectedBrokersPort.remove(event.getRelayPort());
        }
        event.setRelayPort(port);
        return connectedBrokersPort;
    }

    /**
     * Matching the event with the subscripition
     */
    private boolean isMatching(Subscription subscription, Event event) {
        boolean matches = false;
        if (event.getClass().equals(ItemAvailableEvent.class) && subscription.getClass().equals(ItemInterestSubscription.class)) {
            ItemAvailableEvent itemAvailableEvent = (ItemAvailableEvent) event;
            ItemInterestSubscription itemInterestSubscription = (ItemInterestSubscription) subscription;
            matches = isMatchingItem(itemAvailableEvent, itemInterestSubscription);
        } else if (event.getClass().equals(BidEvent.class) && subscription.getClass().equals(BidSubscription.class)) {
            BidEvent bidEvent = (BidEvent) event;
            BidSubscription bidSubscription = (BidSubscription) subscription;
            if (bidSubscription.getItemId().equals(bidEvent.getItemId())) {
                matches = true;
            }
        } else if (event.getClass().equals(SaleEvent.class) && subscription.getClass().equals(ItemSoldSubscription.class)) {
            SaleEvent saleEvent = (SaleEvent) event;
            ItemSoldSubscription itemSoldSubscription = (ItemSoldSubscription) subscription;
            if (itemSoldSubscription.getItemId().equals(saleEvent.getItemId())) {
                matches = true;
            }
        }
        return matches;
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

    public void addChildBrokerPort(Integer childPort) {
        childPorts.add(childPort);
    }

    public Integer getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Broker broker = (Broker) o;

        if (!port.equals(broker.port))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return port.hashCode();
    }
}
