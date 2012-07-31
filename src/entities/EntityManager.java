package entities;

import org.lwjgl.util.vector.Vector3f;
import physics.PhysicsModel;

import java.io.Serializable;
import java.util.ArrayList;

public class EntityManager implements Serializable {
    private ArrayList<Entity> entityList = new ArrayList<Entity>();


    public synchronized void add(Entity e) {
        entityList.add(e);
    }

    public synchronized int addPlayer() {
        Player p = new Player();
        entityList.add(p);
        return entityList.indexOf(p);
    }

    public void updatePlayer(int index, int delta, int up, int left, int right) {
        Player p = (Player) entityList.get(index);
        p.updatePlayer(delta, up, left, right);
    }

    public void update(int delta) {
        calculatePhysics();
        for (Entity e : entityList) {
            e.update(delta);
        }
    }

    public void render() {
        for (Entity e : entityList) {
            e.render();
        }
    }

    public void calculatePhysics() {
        PhysicsModel e1;
        PhysicsModel e2;

        Vector3f distance = new Vector3f();
        Vector3f normal = new Vector3f();
        Vector3f temp = new Vector3f();

        for (int i = 0; i < entityList.size() - 1; i++) {
            e1 = entityList.get(i).getPhysics();
            for (int j = i + 1; j < entityList.size(); j++) {
                e2 = entityList.get(j).getPhysics();

                Vector3f.sub(e1.getPosition(), e2.getPosition(), distance);
                if (distance.length() > 0)
                    distance.normalise(normal);

                Vector3f.sub(e1.getVelocity(), e2.getVelocity(), temp);

                float relativeVelocity = Vector3f.dot(temp, normal);
                float collisionDistance = entityList.get(i).getSize() + entityList.get(j).getSize();
                float overlap = distance.length() / collisionDistance;
                // gravity is capped at the distance at impact
                float gravity = Util.gravity(e1.getMass(), e2.getMass(), distance.lengthSquared());
                if (distance.lengthSquared() < collisionDistance * collisionDistance)
                    gravity = 0;


                // calculate and add gravitational force to both entities
                temp.set(normal);

                temp.scale(gravity);
                e2.addForce(temp);
                temp.negate();
                e1.addForce(temp);

                // if the entities are touching or overlapping and moving towards each other
                // calculate and add collision impulses to both entities
                if (overlap <= 1 && relativeVelocity < 0) {
                    temp.set(normal);
                    temp.scale(Util.impulse(e1.getMass(), e2.getMass(), relativeVelocity));
                    e1.addImpulse(temp);
                    temp.negate();
                    e2.addImpulse(temp);
                }
            }
        }
    }
}