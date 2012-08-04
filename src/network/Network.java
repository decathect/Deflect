package network;

import entities.EntityManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public abstract class Network implements Runnable {
    static final char CONNECT = 'c';
    static final char DISCONNECT = 'd';
    static final char UPDATE = 'u';
    static final char SERVER_FULL = 'f';
    static final char SERVER_SHUTDOWN = 's';

    static final int SERVER_PORT = 5000;
    static final int MAX_PACKET_SIZE = 32 * 1024;

    DatagramSocket socket;

    EntityManager state;
    DatagramPacket statePacket;
    byte[] stateArray;
    ByteBuffer stateBuffer;

    ByteBuffer signalBuffer;

    private boolean running;

    public Network() {
        stateArray = new byte[MAX_PACKET_SIZE];
        statePacket = new DatagramPacket(stateArray, stateArray.length);
        stateBuffer = ByteBuffer.wrap(stateArray);
        signalBuffer = ByteBuffer.allocate(4 * 4);
    }

    public void run() {
        running = true;

        try {
            while (running) {
                receive();
                process();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    abstract void process();

    void receive() {
        try {
            socket.receive(statePacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(InetSocketAddress address, DatagramPacket p) {
        p.setSocketAddress(address);
        try {
            socket.send(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(InetSocketAddress address, byte[] array) {
        send(address, new DatagramPacket(array, array.length));
    }

    public void send(InetSocketAddress address, char c) {
        signalBuffer.clear();
        signalBuffer.putChar(c);
        send(address, signalBuffer.array());
    }

    public void exit() {
        running = false;
        socket.close();
    }
}
