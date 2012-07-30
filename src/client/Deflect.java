package client;

import simulation.Simulation;

public class Deflect {
    Comm comm;
    View view;
    Simulation sim;

    public static void main(String[] args) {
        new Deflect();
    }

    public Deflect() {
        System.err.println("starting threads");
        view = new View(this);
        comm = new Comm(view);

        new Thread(view).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(comm).start();
    }

    public void exit() {
        comm.exit();
    }
}
