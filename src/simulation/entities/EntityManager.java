package simulation.entities;

import client.Render;
import org.lwjgl.util.vector.Vector3f;
import simulation.Util;
import simulation.physics.PhysicsModel;

import java.io.Serializable;
import java.util.ArrayList;

public class EntityManager implements Serializable {
    private ArrayList<Entity> entityList = new ArrayList<Entity>();

    public synchronized void add(Entity e) {
        entityList.add(e);
    }

    public synchronized int addPlayer(Vector3f color) {
        Player p = new Player(Render.PLAYER_LIST);
        p.setColor(color);
        entityList.add(p);
        return entityList.indexOf(p);
    }

    public void updatePlayer(int index, int forward, int turn) {
        if (entityList.size() > index) {
            Player p = (Player) entityList.get(index);
            p.updatePlayer(forward, turn);
        }
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

    /**
     * Calculates the force of gravity and the impulse imparted by collisions and applies them to the relevant entities.
     * <p/>
     * (Note: gravity and collision are calculated at the same time. this is probably why clusters collapse.)
     */
    public void calculatePhysics() {
        PhysicsModel e1;
        PhysicsModel e2;

        float relativeVelocity;
        float collisionDistance;
        float overlap;
        float gravity;

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

                // relative velocity
                Vector3f.sub(e1.getVelocity(), e2.getVelocity(), temp);

                // relative velocity projected onto the normalized distance vector
                // positive if the entities are approaching each other, negative otherwise
                relativeVelocity = Vector3f.dot(temp, normal);

                collisionDistance = entityList.get(i).getSize() + entityList.get(j).getSize();
                overlap = distance.length() / collisionDistance;

                // gravity is capped at the distance at impact
                // otherwise as the distance approaches zero, the force of gravity goes to infinity
                // (it's a neat effect)
                gravity = Util.gravity(e1.getMass(), e2.getMass(), Math.max(distance.lengthSquared(), collisionDistance * collisionDistance));

                // calculate and add gravitational force to both entities
                temp.set(normal);
                temp.scale(gravity);
                e2.addForce(temp);
                temp.negate();
                e1.addForce(temp);

                // if the entities are touching or overlapping and moving towards each other
                // calculate and add collision impulses to both entities
                if (overlap <= 1 && relativeVelocity <= 0) {
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