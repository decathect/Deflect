package network;

import client.Deflect;
import simulation.entities.EntityManager;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class ClientSock extends Network {
    private Deflect deflect;

    private ByteArrayInputStream byteInput;
    private ObjectInputStream objInput;

    private InetSocketAddress server;

    public ClientSock(Deflect v, String a) {
        deflect = v;
        server = new InetSocketAddress(a, SERVER_PORT);

        byteInput = new ByteArrayInputStream(stateArray);

        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public boolean connect() {
        send(server, CONNECT);
        receive();

        switch (stateArray[0]) {
            case CONNECT:
                System.err.println("connection successful");
                return true;
            case SERVER_FULL:
                System.err.println("server full");
                break;
            default:
                System.err.println("unknown packet received");
                break;
        }
        return false;
    }

    public void sendUpdate(int[] input) {
        signalBuffer.clear();
        signalBuffer.put(UPDATE);
        for (int i : input) signalBuffer.putInt(i);
        send(server, signalBuffer.array());
    }

    void process() {
        byteInput.reset();

        try {
            objInput = new ObjectInputStream(new BufferedInputStream(byteInput));
            switch (objInput.read()) {
                case UPDATE:
                    state = (EntityManager) objInput.readObject();
                    deflect.putState(state);
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
