package client;

import entities.EntityManager;
import entities.Util;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import simulation.Simulation;

import static org.lwjgl.opengl.GL11.*;

public class View implements Runnable {
    private static final int DISPLAY_WIDTH = 1024;
    private static final int DISPLAY_HEIGHT = 768;

    Deflect main;
    Simulation sim;

    public View(Deflect d) {
        main = d;
    }

    // apparently display.create and display.update need to be in the same thread if things are to be simple
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

        System.err.println("View initialized");

        sim = new Simulation();
        makeLists();

        while (!Display.isCloseRequested()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            Display.sync(60);
            sim.update();
            sim.render();
            Display.update();
        }
        Display.destroy();
        main.exit();
    }

    public void putState(EntityManager em) {
        sim.putState(em);
    }

    public void makeLists() {
        for (int i = 1; i < 6; i++)
            Util.makeCircle(i, 10, i);
    }
}
