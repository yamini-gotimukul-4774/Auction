package edu.utexas.ece.mwf.auction.subscription;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: yamini
 * Date: 10/24/13
 * Time: 1:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class Subscription implements Serializable {

    private Integer subscriberPort;

    public Integer getSubscriberPort() {
        return subscriberPort;
    }

    public void setSubscriberPort(Integer subscriberPort) {
        this.subscriberPort = subscriberPort;
    }
}
