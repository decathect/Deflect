package client;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

public class View implements Runnable {
    private static final int DISPLAY_WIDTH = 1024;
    private static final int DISPLAY_HEIGHT = 768;

    Deflect main;

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
        glOrtho(0, 100, 0, DISPLAY_HEIGHT * 100 / DISPLAY_WIDTH, 1, -1);
        glMatrixMode(GL_MODELVIEW);

        System.err.println("View initialized");

        while (!Display.isCloseRequested()) {
            Display.sync(60);
            Display.update();
        }
        Display.destroy();
        main.exit();
    }
}
