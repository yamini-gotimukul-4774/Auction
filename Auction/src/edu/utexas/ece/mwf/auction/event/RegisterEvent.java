package edu.utexas.ece.mwf.auction.event;

import edu.utexas.ece.mwf.auction.enums.ClientType;

/**
 * Created with IntelliJ IDEA.
 * User: yamini
 * Date: 10/21/13
 * Time: 6:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class RegisterEvent extends Event {

    private Enum<ClientType> type;
    private int portNumber;

    public RegisterEvent(Enum<ClientType> type) {
        this.type = type;
    }

    public RegisterEvent(Enum<ClientType> type, int portNumber) {
        this.type = type;
        this.portNumber = portNumber;
    }

    public Enum<ClientType> getType() {
        return type;
    }

    public void setType(Enum<ClientType> type) {
        this.type = type;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }
}
