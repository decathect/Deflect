package client;

import entities.EntityManager;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class Comm implements Runnable {
    private static final InetSocketAddress SERVER = new InetSocketAddress("127.0.0.1", 5000);
    private static final int PACKET_SIZE = 49152;
    private static final byte[] CONNECTED = {'c'};

    private DatagramSocket sendSocket;
    private DatagramSocket receiveSocket;
    private DatagramPacket sendPacket;
    private DatagramPacket receivePacket;
    private byte[] sendArray;
    private byte[] receiveArray;
    private ByteBuffer sendBuffer;

    private View view;
    private EntityManager em;

    private ByteArrayInputStream inputArray;
    private ObjectInputStream inputStream;

    private boolean running;

    public Comm(View v) {
        view = v;

        sendArray = new byte[PACKET_SIZE];
        receiveArray = new byte[PACKET_SIZE];
        sendBuffer = ByteBuffer.wrap(sendArray);

        try {
            sendSocket = new DatagramSocket();
            receiveSocket = new DatagramSocket();
            sendPacket = new DatagramPacket(sendArray, PACKET_SIZE);
            receivePacket = new DatagramPacket(receiveArray, PACKET_SIZE);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        System.err.println("comm initialized");
    }

    public void run() {
        running = true;
        if (!connect()) System.err.println("couldn't connect");
        System.err.println("connected to server");

        while (running) {
            try {
                receiveSocket.receive(receivePacket);
                inputArray = new ByteArrayInputStream(receivePacket.getData());
                inputStream = new ObjectInputStream(new BufferedInputStream(inputArray));
                try {
                    em = (EntityManager) inputStream.readObject();
                    view.putState(em);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        sendSocket.close();
        receiveSocket.close();
    }

    private boolean connect() {
        sendBuffer.clear();
        sendBuffer.putChar('n');
        sendBuffer.putInt(receiveSocket.getLocalPort());
        try {
            sendSocket.connect(SERVER);
            sendSocket.send(sendPacket);
            receiveSocket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
            exit();
        }
        return (char) receiveArray[0] == 'c';
    }

    public void exit() {
        sendSocket.close();
        receiveSocket.close();
        running = false;
    }
}
