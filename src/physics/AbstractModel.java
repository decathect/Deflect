package physics;

import org.lwjgl.util.vector.Vector3f;

public abstract class AbstractModel implements PhysicsModel {
    protected Vector3f position, velocity, force, impulse;
    protected float mass;

    public AbstractModel() {
        position = new Vector3f();
        velocity = new Vector3f();
        force = new Vector3f();
        impulse = new Vector3f();
        mass = 1;
    }

    public void addForce(Vector3f f) {
        Vector3f.add(force, f, force);
    }

    public void addImpulse(Vector3f i) {
        Vector3f im = new Vector3f(i);
        im.scale(1 / mass);
        Vector3f.add(velocity, im, velocity);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f p) {
        position = new Vector3f(p);
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float m) {
        mass = m;
    }

}
