package client;

import network.Client;

public class Deflect {
    Client net;
    View view;
    static String serverAddress;

    public static void main(String[] args) {
        serverAddress = args[0];
        new Deflect();
    }

    public Deflect() {
        System.err.println("starting threads");
        view = new View(this);
        net = new Client(view, serverAddress);

        net.connect();
        new Thread(view).start();
        new Thread(net).start();
    }

    public void exit() {
        net.exit();
        System.err.println("exiting Deflect");
        System.exit(0);
    }

    public void send(int[] input) {
        net.sendUpdate(input);
    }
}
