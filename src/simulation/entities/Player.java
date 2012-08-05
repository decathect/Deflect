package simulation.entities;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import simulation.Util;

public class Player extends Entity {
    float enginePower = .01f;
    float rotationSpeed = .5f;

    Vector3f initialDirection;
    Vector3f direction;
    int rotation;

    public Player() {
        initialDirection = new Vector3f(1, 0, 0);
        direction = new Vector3f(initialDirection);
        physicsModel.setMass(100);
        size = 10;

        //initModel();
        listNum = 20;
    }

    private void initModel() {
        listNum = GL11.glGenLists(2);
        GL11.glNewList(listNum + 1, GL11.GL_COMPILE);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3f(-8, 4, 0);
        GL11.glVertex3f(-24, -12, 0);
        GL11.glVertex3f(-24, -16, 0);
        GL11.glVertex3f(-8, -16, 0);
        GL11.glEnd();
        GL11.glEndList();
        GL11.glNewList(listNum, GL11.GL_COMPILE);
        GL11.glRotatef(-90, 0, 0, 1);
        GL11.glScalef(.5f, .5f, 1);
        GL11.glBegin(GL11.GL_POLYGON);
        GL11.glVertex3f(-8, 4, 0);
        GL11.glVertex3f(0, 24, 0);
        GL11.glVertex3f(8, 4, 0);
        GL11.glVertex3f(8, -16, 0);
        GL11.glVertex3f(5, -19, 0);
        GL11.glVertex3f(-5, -19, 0);
        GL11.glVertex3f(-8, -16, 0);
        GL11.glEnd();
        GL11.glCallList(listNum + 1);
        GL11.glScalef(-1, 1, 1);
        GL11.glCallList(listNum + 1);
        GL11.glEndList();
    }

    public void updatePlayer(int up, int left, int right) {
        if (up > 0) {
            Vector3f t = new Vector3f(direction);
            t.scale(enginePower * up);
            physicsModel.addForce(t);
        }

        int temp = left - right;

        if (temp != 0) {
            rotation += rotationSpeed * temp;
        }

        direction = Util.toVector(rotation);
    }

    public void render() {
        Vector3f p = physicsModel.getPosition();
        GL11.glPushMatrix();
        GL11.glTranslatef(p.x, p.y, p.z);
        GL11.glRotatef(rotation, 0, 0, 1);
        GL11.glScalef(1, .5f, .5f);
        GL11.glColor3f(.5f, .5f, 1);
        GL11.glCallList(listNum);
        GL11.glColor3f(1, 1, 1);
        GL11.glPopMatrix();
    }
}
