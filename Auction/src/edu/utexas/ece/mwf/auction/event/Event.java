package edu.utexas.ece.mwf.auction.event;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: yamini
 * Date: 10/21/13
 * Time: 5:41 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * The base class for the events
 * This maintains the event origination port as well as relaying port
 */
public abstract class Event implements Serializable {
    private Integer originatingPort;
    private Integer relayPort;

    public Integer getOriginatingPort() {
        return originatingPort;
    }

    public void setOriginatingPort(Integer originatingPort) {
        this.originatingPort = originatingPort;
    }

    public Integer getRelayPort() {
        return relayPort;
    }

    public void setRelayPort(Integer relayPort) {
        this.relayPort = relayPort;
    }
}
