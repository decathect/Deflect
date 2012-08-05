package server;

import network.Network;
import network.ServerSock;
import simulation.Simulation;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;

public class Server {
    private static final byte MAX_PLAYERS = 8;

    private HashMap<InetSocketAddress, Integer> clientList;

    private Simulation sim;
    private ServerSock net;

    private ByteArrayOutputStream outputArray;
    private ObjectOutputStream outputStream;

    private boolean running;
    private int players;
    private byte[] state;

    public static void main(String[] args) {
        new Server();
    }

    public Server() {
        outputArray = new ByteArrayOutputStream();
        try {
            outputStream = new ObjectOutputStream(new BufferedOutputStream(outputArray));
        } catch (IOException e) {
            e.printStackTrace();
        }

        running = true;
        clientList = new HashMap<InetSocketAddress, Integer>(MAX_PLAYERS);
        players = 0;

        net = new ServerSock(this);
        sim = new Simulation();
        new Thread(net).start();
        new Thread(sim).start();

        while (players < 1) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (running) {
            // TODO: this seems dumb just to get a byte array, find a better way
            try {
                outputArray.reset();
                outputStream = new ObjectOutputStream(outputArray);

                outputStream.writeByte(Network.UPDATE);
                outputStream.writeObject(sim.getState());
                outputStream.flush();
                state = outputArray.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (InetSocketAddress i : clientList.keySet()) {
                //System.err.println(i);
                net.send(i, state);
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void addClient(InetSocketAddress client) {
        if (players < MAX_PLAYERS) {
            clientList.put(client, sim.addPlayer());
            net.send(client, Network.CONNECT);
            players++;
            System.err.println("added client " + client + "\nplayers: " + players);
        } else net.send(client, Network.SERVER_FULL);
    }

    public synchronized void removeClient(InetSocketAddress client) {
        clientList.remove(client);
        players--;
        System.err.println("removed client " + client + "\nplayers: " + players);
    }

    public synchronized void updateClient(InetSocketAddress client, int up, int left, int right) {
        sim.updatePlayer(clientList.get(client), up, left, right);
    }
}
