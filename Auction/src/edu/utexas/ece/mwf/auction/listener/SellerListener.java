package edu.utexas.ece.mwf.auction.listener;

import edu.utexas.ece.mwf.auction.client.Seller;
import edu.utexas.ece.mwf.auction.event.BidEvent;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: yamini
 * Date: 10/23/13
 * Time: 5:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class SellerListener extends Listener {

    private Seller seller;

    public SellerListener(Seller seller) {
        this.seller = seller;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p/>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(seller.getBrokerConnection().getSelfPort());
            while (true) {
                Socket socket = null;
                InputStream inputStream = null;
                ObjectInputStream objectInputStream = null;
                try {
                    socket = serverSocket.accept();
                    inputStream = socket.getInputStream();
                    objectInputStream = new ObjectInputStream(inputStream);
                    BidEvent bidEvent = (BidEvent) objectInputStream.readObject();
                    seller.addBid(bidEvent);
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
