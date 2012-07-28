package client;

public class Deflect {
    Comm comm;

    public static void main(String[] args) {
        new Deflect();
    }

    public Deflect() {
        System.err.println("starting threads");
        comm = new Comm();
        new Thread(comm).start();
        new Thread(new View(this)).start();
    }

    public void exit() {
        comm.exit();
    }
}
