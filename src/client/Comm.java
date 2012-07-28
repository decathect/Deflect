package client;

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
    private static final int PACKET_SIZE = 10240;

    DatagramSocket sendSocket;
    DatagramSocket receiveSocket;
    DatagramPacket sendPacket;
    DatagramPacket receivePacket;
    byte[] sendArray;
    byte[] receiveArray;
    ByteBuffer sendBuffer;
    ByteBuffer receiveBuffer;

    private ByteArrayInputStream inputArray;
    private ObjectInputStream inputStream;

    private boolean running;

    public Comm() {
        sendArray = new byte[PACKET_SIZE];
        receiveArray = new byte[PACKET_SIZE];
        sendBuffer = ByteBuffer.wrap(sendArray);
        receiveBuffer = ByteBuffer.wrap(receiveArray);

        try {
            sendSocket = new DatagramSocket();
            receiveSocket = new DatagramSocket();
            System.err.println(receiveSocket.getLocalPort());
            sendPacket = new DatagramPacket(sendArray, PACKET_SIZE);
            receivePacket = new DatagramPacket(receiveArray, PACKET_SIZE);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        System.err.println("comm initialized");
    }

    public void run() {
        running = true;
        if (!connect()) System.err.println("couldn't connect to server");
        else System.err.println("connected to server");

        while (running) {
            try {
                receiveSocket.receive(receivePacket);
                inputArray = new ByteArrayInputStream(receivePacket.getData());
                inputStream = new ObjectInputStream(inputArray);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        return (receiveBuffer.getChar() == 'a');
    }

    public void exit() {
        sendSocket.close();
        receiveSocket.close();
    }
}
