package network;

import simulation.entities.EntityManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public abstract class Network implements Runnable {
    public static final byte CONNECT = 1;
    public static final byte DISCONNECT = 2;
    public static final byte UPDATE = 3;
    public static final byte SERVER_FULL = 4;
    public static final byte SERVER_SHUTDOWN = 5;

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
        socket.close();
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

    public void send(InetSocketAddress address, byte c) {
        send(address, new byte[]{c});
    }

    public void send(InetSocketAddress address, byte c, int i) {
        signalBuffer.clear();
        signalBuffer.put(c);
        signalBuffer.putInt(i);
        send(address, signalBuffer.array());
    }

    public void exit() {
        running = false;
    }
}
