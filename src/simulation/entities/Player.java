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

        listNum = 20;
    }

    public void updatePlayer(int forward, int turn) {
        if (forward > 0) {
            Vector3f t = new Vector3f(direction);
            t.scale(enginePower * forward);
            physicsModel.addForce(t);
        }

        if (turn != 0) {
            rotation += rotationSpeed * turn;
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
