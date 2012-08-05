package simulation.entities;

import org.lwjgl.util.vector.Vector3f;

import java.util.Random;

public class Mote extends Entity {
    private static Random r = new Random();

    public Mote() {
        // TODO: extract these here arbitrary numbers
        int intSize = r.nextInt(10) + 1;
        size = intSize;
        color = new Vector3f(1, 1, 1);
        physicsModel.setMass(size * size);

        // TODO: scale instead of individual lists
        list = intSize;
    }

    public Mote(Vector3f p) {
        this();
        physicsModel.setPosition(p);
    }
}
