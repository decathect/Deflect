package server;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Random;

public class Server {
    private static final byte NUM_MOTES = 100;
    private static final byte NUM_PLAYERS = 8;

    private static Random r;

    private ArrayList<InetSocketAddress> clientList;
    private DatagramSocket socket;
    private DatagramPacket packet;

    private ByteBuffer buffer;
    private byte[] pos;

    private DatagramSocket inputSocket;
    private DatagramPacket inputPacket;
    private byte[] inputBuffer;

    public static void main(String[] args) {
        new Server();
    }

    public Server() {
        clientList = new ArrayList<InetSocketAddress>(NUM_PLAYERS);
        inputBuffer = new byte[8192];
        inputPacket = new DatagramPacket(inputBuffer, 8192);
        waitForConnection();

        /*
        address = new InetSocketAddress("127.0.0.1", 5001);


        r = new Random();
        pos = new byte[((NUM_MOTES + NUM_PLAYERS) * 2 * 4) + (NUM_PLAYERS * 4)]; // xy coords + player rotation
        address = new InetSocketAddress("127.0.0.1", 5000);
        buffer = ByteBuffer.wrap(pos);

        try {
            socket = new DatagramSocket(5000);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        while(true) {
            buffer.clear();
            for (int i = 0; i < (pos.length) / 4; i++) {
                buffer.putFloat(r.nextFloat());
            }

            try {
                packet = new DatagramPacket(pos, pos.length, address);
                socket.send(packet);
                System.out.println("sent.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    }

    private void waitForConnection() throws IOException {
        while (true) {
            inputSocket.receive(inputPacket);

            if (inputBuffer[0] == 1) {
                clientList.add(new InetSocketAddress(inputPacket.getAddress(), inputPacket.getPort()));
            }
        }
    }
}
