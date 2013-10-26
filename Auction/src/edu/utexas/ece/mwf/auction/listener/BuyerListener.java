package edu.utexas.ece.mwf.auction.listener;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import edu.utexas.ece.mwf.auction.client.Buyer;
import edu.utexas.ece.mwf.auction.event.BidEvent;
import edu.utexas.ece.mwf.auction.event.ItemAvailableEvent;
import edu.utexas.ece.mwf.auction.event.SaleEvent;

/**
 * Created with IntelliJ IDEA. User: yamini Date: 10/24/13 Time: 3:34 AM To
 * change this template use File | Settings | File Templates.
 */
public class BuyerListener extends Listener {

    private Buyer buyer;

    public BuyerListener(Buyer buyer) {
        this.buyer = buyer;
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
        try {
            ServerSocket serverSocket = new ServerSocket(buyer.getBrokerConnection().getSelfPort());
            while (true) {
                Socket socket = null;
                InputStream inputStream = null;
                ObjectInputStream objectInputStream = null;
                try {
                    socket = serverSocket.accept();
                    inputStream = socket.getInputStream();
                    objectInputStream = new ObjectInputStream(inputStream);
                    Object event = objectInputStream.readObject();
                    if (event.getClass().equals(ItemAvailableEvent.class)) {
                        buyer.addItemAvailableEvent((ItemAvailableEvent) event);
                    } else if (event.getClass().equals(BidEvent.class)) {
                        buyer.addBidUpdateEvent((BidEvent) event);
                    } else {
                        buyer.addSaleEvent((SaleEvent) event);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    inputStream.close();
                    socket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
