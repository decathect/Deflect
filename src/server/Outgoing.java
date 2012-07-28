package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class Outgoing {
    private static final int PACKET_SIZE = 10240;

    private DatagramSocket outputSocket;
    private DatagramPacket outputPacket;
    private ByteBuffer outputBuffer;
    private byte[] outputArray;

    public Outgoing(Server s) {
        try {
            outputSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        outputArray = new byte[PACKET_SIZE];
        outputPacket = new DatagramPacket(outputArray, PACKET_SIZE);
        outputBuffer = ByteBuffer.wrap(outputArray);
    }

    public void send(InetSocketAddress address, char c) {
        outputPacket = new DatagramPacket(new byte[4], 4);
        outputPacket.setSocketAddress(address);
        outputBuffer.clear();
        outputBuffer.putChar(c);
        try {
            outputSocket.send(outputPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO: should construct the packet once and just change the address
    public void send(InetSocketAddress address, byte[] state) {
        outputPacket = new DatagramPacket(state, state.length);
        outputPacket.setSocketAddress(address);
        try {
            outputSocket.send(outputPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
