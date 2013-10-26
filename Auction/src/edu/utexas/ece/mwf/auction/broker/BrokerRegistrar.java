package edu.utexas.ece.mwf.auction.broker;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: yamini
 * Date: 10/22/13
 * Time: 1:44 AM
 * To change this template use File | Settings | File Templates.
 */

/**
 * Broker register class is used to assign the port numbers .It also has the
 * responsibility of the creating a new broker when a port has two clients
 * connected REGISTRATION_PORT is the main socket port that manages the
 * connections between the client to broker and also to broker to broker
 */
public class BrokerRegistrar {

    public static final int MAX_CHILD_PER_PARENT = 3;
    public static final int REGISTRATION_PORT = 4445;
    private static int BROKER_START_PORT = 4444;
    private static int SELF_START_PORT = 6000;
    private static Map<Integer, Integer> brokerPorts = new HashMap<Integer, Integer>();
    private static Map<Broker, Integer> parentBrokerMap = new HashMap<Broker, Integer>();
    private static List<Broker> childBrokerList = new ArrayList<Broker>();

    public static void main(String[] args) throws IOException {
        System.out.println("Successfully started BrokerRegistrar at port " + REGISTRATION_PORT);
        ServerSocket serverSocket = new ServerSocket(REGISTRATION_PORT);
        listen(serverSocket);
    }

    private static void listen(ServerSocket serverSocket) throws IOException {
        while (true) {
            Socket socket = null;
            OutputStream outputStream = null;
            ObjectOutputStream objectOutputStream = null;
            try {
                socket = serverSocket.accept();
                BrokerConnection brokerConnection = getBrokerConnection();
                if (brokerPorts.get(BROKER_START_PORT) == 1) {
                    createBroker(brokerConnection);
                }
                outputStream = socket.getOutputStream();
                objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(brokerConnection);
                objectOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                outputStream.close();
                socket.close();
            }
        }
    }

    /**
     * Construction of the Hierarchial structuct for establishing the broker to
     * broker connection
     */

    private static void createBroker(BrokerConnection brokerConnection) {
        Broker parentBroker = null;
        Integer parentBrokerPort = null;
        if (parentBrokerMap.keySet() != null && !parentBrokerMap.keySet().isEmpty()) {
            for (Broker broker : parentBrokerMap.keySet()) {
                if (parentBrokerMap.get(broker) < MAX_CHILD_PER_PARENT) {
                    parentBroker = broker;
                    parentBrokerPort = broker.getPort();
                    parentBroker.addChildBrokerPort(brokerConnection.getBrokerPort());
                    break;
                }
            }

            if (parentBroker == null) {
                parentBrokerMap.clear();
                for (Broker childBroker : childBrokerList) {
                    parentBrokerMap.put(childBroker, 0);
                }
                parentBroker = parentBrokerMap.keySet().iterator().next();
                parentBrokerPort = parentBroker.getPort();
                parentBroker.addChildBrokerPort(brokerConnection.getBrokerPort());
            }
        }

        Broker currentBroker = new Broker(brokerConnection.getBrokerPort(), parentBrokerPort);
        if (parentBroker == null) {
            parentBrokerMap.put(currentBroker, 0);
        } else {
            parentBrokerMap.put(parentBroker, parentBrokerMap.get(parentBroker) + 1);
            childBrokerList.add(currentBroker);
        }
        new Thread(currentBroker).start();
    }

    /**
     * Creating a new broker dynamically when a broker has already two clients
     * connected
     */
    private static synchronized BrokerConnection getBrokerConnection() {
        Integer clientSize = brokerPorts.get(BROKER_START_PORT);
        if (clientSize == null) {
            brokerPorts.put(BROKER_START_PORT, 1);
        } else if (clientSize == 1) {
            brokerPorts.put(BROKER_START_PORT, 2);
        } else if (clientSize > 1) {
            BROKER_START_PORT--;
            brokerPorts.put(BROKER_START_PORT, 1);
        }
        BrokerConnection brokerConnection = new BrokerConnection(BROKER_START_PORT, --SELF_START_PORT);
        return brokerConnection;
    }

}
