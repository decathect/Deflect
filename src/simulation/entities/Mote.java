package simulation.entities;

import org.lwjgl.util.vector.Vector3f;

public class Mote extends Entity {
    public Mote(int l) {
        size = 1;
        list = l;
        color = new Vector3f(1, 1, 1);
        physicsModel.setMass(size * size);
    }

    public Mote(int list, Vector3f p, int size) {
        this(list);
        physicsModel.setPosition(p);
        setSize(size);
    }

    public void setSize(int s) {
        size = s;
        physicsModel.setMass(size * size);
    }
}
