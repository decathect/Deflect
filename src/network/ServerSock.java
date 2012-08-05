package network;

import server.Server;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class ServerSock extends Network {
    private Server server;
    private InetSocketAddress address;

    public ServerSock(Server s) {
        server = s;
        try {
            socket = new DatagramSocket(SERVER_PORT);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        System.err.println("listening on " + socket.getLocalSocketAddress());
    }

    void process() {
        stateBuffer.clear();
        address = (InetSocketAddress) statePacket.getSocketAddress();
        switch (stateBuffer.get()) {
            case CONNECT:
                System.err.println("connection packet received");
                server.addClient(address);
                break;
            case UPDATE:
                //System.err.println("update packet received");
                server.updateClient(address, stateBuffer.getInt(), stateBuffer.getInt());
                break;
            case DISCONNECT:
                System.err.println("disconnection packet received");
                server.removeClient(address);
                break;
            default:
                System.err.println("unknown packet received");
                break;
        }
    }
}
