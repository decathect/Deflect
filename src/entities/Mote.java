package entities;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.vector.Vector3f;

import java.util.Random;

public class Mote extends Entity {
    private static Random r = new Random();

    public Mote() {
        physicsModel.setPosition(new Vector3f(r.nextInt(1000) - 500, r.nextInt(1000) - 500, 0));

        size = r.nextInt(5) + 1;
        physicsModel.setMass(size * size);

        listNum = glGenLists(1);
        Util.makeCircle(listNum, 10, size);
    }
}
