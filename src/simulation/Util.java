package simulation;

import org.lwjgl.Sys;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.*;

public class Util {
    private static final float g = .0001f; // gravitational constant
    private static final float e = .8f; // coefficient of restitution

    private static long now, then = 0;
    private static int delta;

    public static int getDelta() {
        now = Sys.getTime() * 1000 / Sys.getTimerResolution();
        delta = (int) (now - then);
        then = now;

        return delta;
    }

    // calculates force of gravity
    public static float gravity(float mass1, float mass2, float rSquared) {
        if (rSquared > 0)
            return g * mass1 * mass2 / rSquared;
        else
            return 0;
    }

    // calculates impulse
    // from http://chrishecker.com/images/e/e7/Gdmphys3.pdf
    public static float impulse(float mass1, float mass2, float relativeVelocity) {
        return (-(1 + e) * relativeVelocity) / (1 / mass1 + 1 / mass2);
    }

    public static Vector3f toVector(float rotation) {
        double r = Math.toRadians(rotation);
        Vector3f v = new Vector3f();
        v.setX((float) Math.cos(r));
        v.setY((float) Math.sin(r));

        return v;
    }

    public static void makeCircle(int id, int detail, float size) {
        glNewList(id, GL_COMPILE);
        double degree;
        glPushMatrix();
        glScalef(size, size, 1);
        glBegin(GL_TRIANGLE_FAN);
        for (int i = 0; i < detail; i++) {
            degree = (i / (float) detail) * (Math.PI * 2);
            glVertex3f((float) Math.cos(degree), (float) Math.sin(degree), 0);
        }
        glEnd();
        glPopMatrix();
        glEndList();
    }
}