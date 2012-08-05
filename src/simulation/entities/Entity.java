package simulation.entities;

import org.lwjgl.util.vector.Vector3f;
import simulation.physics.Naive;
import simulation.physics.PhysicsModel;

import java.io.Serializable;

import static org.lwjgl.opengl.GL11.*;

public abstract class Entity implements Serializable {
    protected float size;
    protected int listNum;
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
        Vector3f p = physicsModel.getPosition();
        glPushMatrix();
        glTranslatef(p.x, p.y, p.z);
        glCallList(listNum);
        glPopMatrix();
    }
}
