package client;

import network.ClientSock;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import simulation.Simulation;
import simulation.Util;
import simulation.entities.EntityManager;

import static org.lwjgl.opengl.GL11.*;

public class Deflect implements Runnable {
    private static final int DISPLAY_WIDTH = 1440;
    private static final int DISPLAY_HEIGHT = 900;

    private int[] input = {0, 0};
    private int tempDelta;

    static String serverAddress;

    Simulation sim;
    ClientSock net;

    public static void main(String[] args) {
        serverAddress = args[0];
        new Deflect();
    }

    public Deflect() {
        System.err.println("starting threads");
        net = new ClientSock(this, serverAddress);

        while (!net.connect()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        new Thread(this).start();
    }

    public void run() {
        try {
            Display.setTitle("Deflect");
            Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT));
            Display.setFullscreen(false);
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, 1000, 0, DISPLAY_HEIGHT * 1000 / DISPLAY_WIDTH, 1, -1);
        glMatrixMode(GL_MODELVIEW);

        System.err.println("view initialized");

        sim = new Simulation();
        makeLists();

        startNetwork();
        while (!Display.isCloseRequested()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            Display.sync(60);
            sim.update();
            getInput();
            sim.render();
            Display.update();
        }
        Display.destroy();
        exit();
    }

    public void startNetwork() {
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

    public void putState(EntityManager em) {
        send(input);
        input[0] = 0;
        input[1] = 0;
        sim.putState(em);
    }

    public void makeLists() {
        for (int i = 1; i < 6; i++)
            Util.makeCircle(i, 10, i);

        glNewList(21, GL_COMPILE);
        glBegin(GL_QUADS);
        glVertex3f(-8, 4, 0);
        glVertex3f(-24, -12, 0);
        glVertex3f(-24, -16, 0);
        glVertex3f(-8, -16, 0);
        glEnd();
        glEndList();
        glNewList(20, GL_COMPILE);
        glRotatef(-90, 0, 0, 1);
        glScalef(.5f, .5f, 1);
        glBegin(GL_POLYGON);
        glVertex3f(-8, 4, 0);
        glVertex3f(0, 24, 0);
        glVertex3f(8, 4, 0);
        glVertex3f(8, -16, 0);
        glVertex3f(5, -19, 0);
        glVertex3f(-5, -19, 0);
        glVertex3f(-8, -16, 0);
        glEnd();
        glCallList(21);
        glScalef(-1, 1, 1);
        glCallList(21);
        glEndList();
    }

    private void getInput() {
        tempDelta = sim.getDelta();
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            input[0] += tempDelta;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            input[1] += tempDelta;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            input[1] -= tempDelta;
        }
    }
}
