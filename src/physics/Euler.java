package physics;

import org.lwjgl.util.vector.Vector3f;

public class Euler extends AbstractModel {
    public void step(int delta) {
        Vector3f vt = new Vector3f(velocity);
        Vector3f at = new Vector3f(force);

        vt.scale(delta);
        Vector3f.add(position, vt, position);
        at.scale(delta / mass);
        Vector3f.add(velocity, at, velocity);
        force = new Vector3f();
    }
}