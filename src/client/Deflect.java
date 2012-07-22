package client;

public class Deflect implements Runnable {
    public static void main(String[] args) {
        new Deflect();
    }

    public Deflect() {
        new Thread(this).start();
        new Thread(new View()).start();
        new Thread(new Comm()).start();
    }

    public void run() {
    }
}
