package client;

import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.*;

/**
 * Collection of static methods to create and render display lists.
 */
public class Render {
    /**
     * Display list assigned to the Mote entity.
     */
    public static final int MOTE_LIST = 1;
    /**
     * Display list assigned to the Player entity.
     */
    public static final int PLAYER_LIST = 2;
    /**
     * Display list assigned to the wing polygon used in constructing the Player model.
     */
    public static final int WING_LIST = 3;

    /**
     * Renders the specified display list with the specified position, size, rotation, and color.
     *
     * @param position position of object
     * @param scale    size of object
     * @param rotation rotation of object
     * @param color    color of object
     * @param list     display list to be rendered
     */
    public static void render(Vector3f position, float scale, int rotation, Vector3f color, int list) {
        glPushMatrix();
        glTranslatef(position.x, position.y, position.z);
        glRotatef(rotation, 0, 0, 1);
        glScalef(scale, scale, 1);
        glColor3f(color.x, color.y, color.z);
        glCallList(list);
        glPopMatrix();
    }

    /**
     * Creates a display list representing a circle.
     *
     * @param id     display list id  to be assigned to the generated circle
     * @param detail number of triangles to be used to approximate the circle
     */
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

    /**
     * Initializes display lists for Mote and Player objects.
     */
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
