package simulation.entities;

import client.Render;
import org.lwjgl.util.vector.Vector3f;
import simulation.Util;

public class Player extends Entity {
    float enginePower = .01f;
    float rotationSpeed = .5f;

    Vector3f initialDirection;
    Vector3f direction;
    int rotation;

    public Player() {
        color = new Vector3f(.5f, .5f, 1);
        initialDirection = new Vector3f(1, 0, 0);
        direction = new Vector3f(initialDirection);
        physicsModel.setMass(100);
        size = 10;

        list = 20;
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
        Render.render(physicsModel.getPosition(), rotation, color, list);
    }
}
