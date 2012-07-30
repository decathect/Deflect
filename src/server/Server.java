package server;

import simulation.Simulation;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;

// TODO: incoming and outgoing as subclasses
// TODO: figure out the right way to handle varying packet sizes
public class Server {
    private static final byte MAX_PLAYERS = 8;

    private ArrayList<InetSocketAddress> clientList;
    private ObjectOutputStream outputStream;
    private ByteArrayOutputStream outputArray;

    private int seq;
    private int players;
    private Simulation sim;
    private Outgoing out;
    private Incoming in;
    private boolean running;
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
        clientList = new ArrayList<InetSocketAddress>(MAX_PLAYERS);
        seq = 0;
        players = 0;

        out = new Outgoing(this);
        in = new Incoming(this);
        sim = new Simulation();
        new Thread(in).start();
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
                outputArray = new ByteArrayOutputStream();
                outputStream = new ObjectOutputStream(new BufferedOutputStream(outputArray));

                outputStream.writeObject(sim.getState());
                outputStream.flush();
                state = outputArray.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (InetSocketAddress i : clientList) {
                out.send(i, state);
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void addClient(InetSocketAddress client) {
        if (players < MAX_PLAYERS) {
            clientList.add(client);
            out.send(client, 'a');
            System.err.println("added client");
            players++;
        } else out.send(client, 'f');
    }
}
