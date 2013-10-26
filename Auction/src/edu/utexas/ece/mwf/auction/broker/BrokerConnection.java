package edu.utexas.ece.mwf.auction.broker;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: yamini
 * Date: 10/22/13
 * Time: 3:20 AM
 * To change this template use File | Settings | File Templates.
 */

/**
 * This is a class that maintains a broker port and a port called self port
 * which is port at which the clients are connected at.
 */
public class BrokerConnection implements Serializable {

    private Integer brokerPort;
    private Integer selfPort;

    public BrokerConnection(Integer brokerPort, Integer selfPort) {
        super();
        this.brokerPort = brokerPort;
        this.selfPort = selfPort;
    }

    public Integer getBrokerPort() {
        return brokerPort;
    }

    public void setBrokerPort(Integer brokerPort) {
        this.brokerPort = brokerPort;
    }

    public Integer getSelfPort() {
        return selfPort;
    }

    public void setSelfPort(Integer selfPort) {
        this.selfPort = selfPort;
    }

}
