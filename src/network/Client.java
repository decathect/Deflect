package network;

import client.View;
import entities.EntityManager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class Client extends Network {
    private View view;

    private ByteArrayInputStream byteInput;
    private ObjectInputStream objInput;

    private InetSocketAddress server;

    public Client(View v, String a) {
        view = v;
        server = new InetSocketAddress(a, SERVER_PORT);

        byteInput = new ByteArrayInputStream(stateArray);

        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        send(server, CONNECT);
        receive();

        stateBuffer.clear();
        switch (stateBuffer.getChar()) {
            case CONNECT:
                System.err.println("connection successful");
                break;
            case SERVER_FULL:
                System.err.println("server full");
                break;
            default:
                System.err.println("unknown packet received");
                break;
        }
    }

    public void sendUpdate(int[] input) {
        signalBuffer.clear();
        signalBuffer.putChar('u');
        for (int i : input) signalBuffer.putInt(i);
        send(server, signalBuffer.array());
    }

    void process() {
        byteInput.reset();

        try {
            objInput = new ObjectInputStream(byteInput);
            switch (objInput.readChar()) {
                case UPDATE:
                    state = (EntityManager) objInput.readObject();
                    view.putState(state);
                    break;
                case SERVER_SHUTDOWN:
                    System.err.println("server shutting down");
                    exit();
                    break;
                default:
                    System.err.println("unknown packet received");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void exit() {
        send(server, DISCONNECT);
        super.exit();
    }
}
