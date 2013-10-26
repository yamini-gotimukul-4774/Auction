package edu.utexas.ece.mwf.auction.client;

import edu.utexas.ece.mwf.auction.broker.BrokerConnection;
import edu.utexas.ece.mwf.auction.broker.BrokerRegistrar;
import edu.utexas.ece.mwf.auction.enums.ClientType;
import edu.utexas.ece.mwf.auction.event.*;
import edu.utexas.ece.mwf.auction.listener.Listener;
import edu.utexas.ece.mwf.auction.subscription.Subscription;
import edu.utexas.ece.mwf.auction.utils.SocketUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: yamini
 * Date: 10/24/13
 * Time: 3:23 AM
 * To change this template use File | Settings | File Templates.
 */

/**
 * This is a base class for both the buyers and the sellers
 * Establishes the connection of the broker
 * Defines the actual registration process methods with the broker
 * Defines publish events methods
 * Defines subscription events method
 * Defines a listner event method
 */
public abstract class Client {
    private BrokerConnection brokerConnection;
    private Map<Long, ItemAvailableEvent> itemAvailableEventMap = new HashMap<Long, ItemAvailableEvent>();
    private Map<Long, BidEvent> newBidEventMap = new HashMap<Long, BidEvent>();
    private Map<Long, BidEvent> updateBidEventMap = new HashMap<Long, BidEvent>();
    private Map<Long, SaleEvent> saleEventMap = new HashMap<Long, SaleEvent>();

    protected void retrieveBrokerConnection() throws IOException {
        Socket socket = null;
        InputStream inputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            socket = new Socket(InetAddress.getLocalHost(), BrokerRegistrar.REGISTRATION_PORT);
            inputStream = socket.getInputStream();
            objectInputStream = new ObjectInputStream(inputStream);
            brokerConnection = (BrokerConnection) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            inputStream.close();
            socket.close();
        }
    }

    protected void registerWithBroker(ClientType clientType) throws IOException {
        RegisterEvent registerEvent = new RegisterEvent(clientType, getBrokerConnection().getSelfPort());
        SocketUtils.transmit(getBrokerConnection().getBrokerPort(), registerEvent);
    }

    protected void publishEvent(Event event) {
        event.setOriginatingPort(getBrokerConnection().getSelfPort());
        SocketUtils.transmit(getBrokerConnection().getBrokerPort(), event);
    }

    protected void subscribe(Subscription subscription) {
        subscription.setSubscriberPort(getBrokerConnection().getSelfPort());
        SocketUtils.transmit(getBrokerConnection().getBrokerPort(), subscription);
    }

    protected void listenForEvents(Listener listener) throws IOException {
        new Thread(listener).start();
    }

    public BrokerConnection getBrokerConnection() {
        return brokerConnection;
    }

    public Map<Long, ItemAvailableEvent> getItemAvailableEventMap() {
        return itemAvailableEventMap;
    }

    public Map<Long, BidEvent> getNewBidEventMap() {
        return newBidEventMap;
    }

    public Map<Long, BidEvent> getUpdateBidEventMap() {
        return updateBidEventMap;
    }

    public Map<Long, SaleEvent> getSaleEventMap() {
        return saleEventMap;
    }
}
