package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class Incoming implements Runnable {
    private static final InetSocketAddress SERVER = new InetSocketAddress("127.0.0.1", 5000);
    private static final int SERVER_PORT = 5000;
    private static final int PACKET_SIZE = 49152;

    private Server server;
    private DatagramSocket inputSocket;
    private DatagramPacket inputPacket;
    private byte[] inputArray;
    private ByteBuffer inputBuffer;

    public Incoming(Server s) {
        server = s;
        inputArray = new byte[PACKET_SIZE];
        inputPacket = new DatagramPacket(inputArray, PACKET_SIZE);
        inputBuffer = ByteBuffer.wrap(inputArray);

        try {
            inputSocket = new DatagramSocket(SERVER);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            inputBuffer.clear();
            try {
                //System.err.println("waiting for new packet");
                inputSocket.receive(inputPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            switch (inputBuffer.getChar()) {
                case 'n':
                    System.err.println("connection packet received");
                    server.addClient(new InetSocketAddress(inputPacket.getAddress(), inputBuffer.getInt()));
                    break;
                case 'u':
                    //System.err.println("update packet received");
                    server.updateClient(new InetSocketAddress(inputPacket.getAddress(), inputBuffer.getInt()),
                            inputBuffer.getInt(), inputBuffer.getInt(), inputBuffer.getInt(), inputBuffer.getInt());
                    break;
                case 'd':
                    System.err.println("disconnection packet received");
                    server.removeClient(new InetSocketAddress(inputPacket.getAddress(), inputBuffer.getInt()));
                    break;
                default:
                    System.err.println("unknown packet received");
                    break;
            }
        }
    }
}
