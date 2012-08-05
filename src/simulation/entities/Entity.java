package simulation.entities;

import client.Render;
import org.lwjgl.util.vector.Vector3f;
import simulation.physics.Naive;
import simulation.physics.PhysicsModel;

import java.io.Serializable;

public abstract class Entity implements Serializable {
    protected Vector3f color;
    protected float size;
    protected int list;
    protected PhysicsModel physicsModel;

    public Entity() {
        physicsModel = new Naive();
    }

    public PhysicsModel getPhysics() {
        return physicsModel;
    }

    public void update(int delta) {
        physicsModel.step(delta);
    }

    public float getSize() {
        return size;
    }

    public void render() {
        Render.render(physicsModel.getPosition(), 0, color, list);
    }
}
