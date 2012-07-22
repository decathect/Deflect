package physics;

import org.lwjgl.util.vector.Vector3f;

public interface PhysicsModel {
    void addForce(Vector3f f);

    void addImpulse(Vector3f i);

    void step(int delta);

    Vector3f getPosition();

    void setPosition(Vector3f p);

    Vector3f getVelocity();

    float getMass();

    void setMass(float mass);
}
