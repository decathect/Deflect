package network;

import server.Server;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class ServerSock extends Network {
    private Server server;

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
        switch (stateBuffer.get()) {
            case CONNECT:
                System.err.println("connection packet received");
                server.addClient((InetSocketAddress) statePacket.getSocketAddress());
                break;
            case UPDATE:
                //System.err.println("update packet received");
                server.updateClient((InetSocketAddress) statePacket.getSocketAddress(),
                        stateBuffer.getInt(), stateBuffer.getInt(), stateBuffer.getInt());
                break;
            case DISCONNECT:
                System.err.println("disconnection packet received");
                server.removeClient((InetSocketAddress) statePacket.getSocketAddress());
                break;
            default:
                System.err.println("unknown packet received");
                break;
        }
    }
}
