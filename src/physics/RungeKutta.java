package physics;

import org.lwjgl.util.vector.Vector3f;

public class RungeKutta extends AbstractModel {
    public void step(int delta) {
        Vector3f[] k2 = evaluate(delta * .5f, new Vector3f(), new Vector3f());
        Vector3f[] k3 = evaluate(delta * .5f, k2[0], k2[1]);
        Vector3f[] k4 = evaluate((float) delta, k3[0], k3[1]);

        Vector3f temp = new Vector3f();

        Vector3f.add(k2[0], k3[0], temp);
        temp.scale(2);
        Vector3f.add(temp, k4[0], temp);

        Vector3f.add(position, temp, position);

        Vector3f.add(k2[1], k3[1], temp);
        temp.scale(2);
        Vector3f.add(temp, k4[1], temp);

        Vector3f.add(velocity, temp, velocity);
        force = new Vector3f();
    }

    private Vector3f[] evaluate(float delta, Vector3f dx, Vector3f dv) {
        Vector3f x = new Vector3f(position);
        Vector3f v = new Vector3f(velocity);

        dx.scale(delta);
        dv.scale(delta);

        Vector3f.add(x, dx, x);
        Vector3f.add(v, dv, v);

        dx.set(v);
        dv.set(force);
        dv.scale(1 / mass);

        Vector3f[] out = {dx, dv};
        return out;
    }
}