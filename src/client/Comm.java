package client;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

public class Comm implements Runnable{
    DatagramSocket socket;
    DatagramPacket packet;
    ByteBuffer buffer;
    byte[] pos;
    byte[] temp;

    public Comm() {
        temp = new byte[8192];
        try {
            socket = new DatagramSocket(5001);
            packet = new DatagramPacket(temp, 8192);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(true) {
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            pos = packet.getData();
            buffer = ByteBuffer.wrap(pos);
            for (int i = 0; i < packet.getLength() / 4; i++) {
                System.out.println(i + " " + buffer.getFloat());
            }
        }
    }
}
