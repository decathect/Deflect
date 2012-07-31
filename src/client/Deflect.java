package client;

public class Deflect {
    Comm comm;
    View view;

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
        System.err.println("exiting Deflect");
        System.exit(0);
    }

    public void send(int delta, int input[]) {
        comm.send(delta, input);
    }
}
