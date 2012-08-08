package simulation.entities;

import org.lwjgl.util.vector.Vector3f;

/**
 * Motes are inert physics entities.
 */
public class Mote extends Entity {
    /**
     * Constructs Mote entity with the given display list.
     *
     * @param list number of display list
     */
    public Mote(int list) {
        size = 1;
        this.list = list;
        color = new Vector3f(1, 1, 1);
        physicsModel.setMass(size * size);
    }

    /**
     * Constructs Mote entity with the given display list, position, and size.
     *
     * @param list     number of display list
     * @param position initial position of Mote
     * @param size     initial size of Mote
     */
    public Mote(int list, Vector3f position, int size) {
        this(list);
        physicsModel.setPosition(position);
        setSize(size);
    }

    /**
     * Changes the size of a Mote and sets its mass proportionally.
     *
     * @param size new size of Mote
     */
    public void setSize(int size) {
        this.size = size;
        physicsModel.setMass(size * size);
    }
}
