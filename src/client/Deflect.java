package client;

import network.ClientSock;

public class Deflect {
    ClientSock net;
    View view;
    static String serverAddress;

    public static void main(String[] args) {
        serverAddress = args[0];
        new Deflect();
    }

    public Deflect() {
        System.err.println("starting threads");
        view = new View(this);
        net = new ClientSock(view, serverAddress);

        while (!net.connect()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
