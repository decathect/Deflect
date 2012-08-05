package client;

import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.*;

public class Render {
    public static final int MOTE_LIST = 1;
    public static final int PLAYER_LIST = 2;
    public static final int WING_LIST = 3;

    public static void render(Vector3f p, float s, int r, Vector3f c, int list) {
        glPushMatrix();
        glTranslatef(p.x, p.y, p.z);
        glRotatef(r, 0, 0, 1);
        glScalef(s, s, 1);
        glColor3f(c.x, c.y, c.z);
        glCallList(list);
        glPopMatrix();
    }

    public static void makeCircle(int id, int detail) {
        glNewList(id, GL_COMPILE);
        double degree;
        glPushMatrix();
        glBegin(GL_TRIANGLE_FAN);
        for (int i = 0; i < detail; i++) {
            degree = (i / (float) detail) * (Math.PI * 2);
            glVertex3f((float) Math.cos(degree), (float) Math.sin(degree), 0);
        }
        glEnd();
        glPopMatrix();
        glEndList();
    }

    public static void makeLists() {
        makeCircle(MOTE_LIST, 12);

        glNewList(WING_LIST, GL_COMPILE);
        glBegin(GL_QUADS);
        glVertex3f(-8, 4, 0);
        glVertex3f(-24, -12, 0);
        glVertex3f(-24, -16, 0);
        glVertex3f(-8, -16, 0);
        glEnd();
        glEndList();
        glNewList(PLAYER_LIST, GL_COMPILE);
        glRotatef(-90, 0, 0, 1);
        glScalef(.25f, .5f, 1);
        glBegin(GL_POLYGON);
        glVertex3f(-8, 4, 0);
        glVertex3f(0, 24, 0);
        glVertex3f(8, 4, 0);
        glVertex3f(8, -16, 0);
        glVertex3f(5, -19, 0);
        glVertex3f(-5, -19, 0);
        glVertex3f(-8, -16, 0);
        glEnd();
        glCallList(WING_LIST);
        glScalef(-1, 1, 1);
        glCallList(WING_LIST);
        glEndList();
    }
}
