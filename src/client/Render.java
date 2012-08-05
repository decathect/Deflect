package client;

import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.*;

public class Render {
    public static void render(Vector3f p, int r, Vector3f c, int list) {
        glPushMatrix();
        glTranslatef(p.x, p.y, p.z);
        glRotatef(r, 0, 0, 1);
        glScalef(1, 1, 1);
        glColor3f(c.x, c.y, c.z);
        glCallList(list);
        glPopMatrix();
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

    public static void makeLists() {
        for (int i = 1; i < 11; i++)
            makeCircle(i, 10, i);

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
}
