package entities;

import org.lwjgl.util.vector.Vector3f;

import java.util.Random;

public class Mote extends Entity {
    private static Random r = new Random();

    public Mote() {
        // TODO: extract these here arbitrary numbers
        physicsModel.setPosition(new Vector3f(r.nextInt(1000), r.nextInt(1000), 0));

        int intSize = r.nextInt(5) + 1;
        size = intSize;
        physicsModel.setMass(size * size);

        // TODO: scale instead of individual lists
        listNum = intSize;
    }
}
