package simulation.physics;

import org.lwjgl.util.vector.Vector3f;

public class Naive extends AbstractModel {
    public void step(int delta) {
        force.scale(delta / mass);                  // ft/m = v = at
        Vector3f.add(velocity, force, velocity);    // vf = vi + at
        force.scale(delta / 2f);                    // at*t/2
        Vector3f.add(position, force, position);    // x = vt + 1/2at^2
        force.set(velocity);
        force.scale(delta);                         // vt
        Vector3f.add(position, force, position);
        force = new Vector3f();
    }
}