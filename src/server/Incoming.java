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
    private static final int PACKET_SIZE = 10240;

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
                System.err.println("waiting for new client");
                inputSocket.receive(inputPacket);
                System.err.println("client connection packet received");
            } catch (IOException e) {
                e.printStackTrace();
            }

            switch (inputBuffer.getChar()) {
                case 'n':
                    server.addClient(new InetSocketAddress(inputPacket.getAddress(), inputBuffer.getInt()));
                    break;
                case 'u':
                    //updateState(inputBuffer);
                    break;
                default:
                    break;
            }
        }
    }
}
