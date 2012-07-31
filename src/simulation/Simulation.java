package simulation;

import entities.EntityManager;
import entities.Mote;

public class Simulation implements Runnable {
    // TODO: enums
    private static final int NUM_MOTES = 200;

    EntityManager em;
    EntityManager temp;
    int delta;
    boolean dirty;
    boolean running;
    boolean render;

    public Simulation() {
        em = new EntityManager();
        for (int i = 0; i < NUM_MOTES; i++) {
            em.add(new Mote());
        }
        delta = Timer.getDelta();
    }

    public int addPlayer() {
        return em.addPlayer();
    }

    public void updatePlayer(int index, int delta, int up, int left, int right) {
        em.updatePlayer(index, delta, up, left, right);
    }

    public void update() {
        if (dirty == true) {
            em = temp;
            dirty = false;
        }
        delta = Timer.getDelta();
        em.update(delta);
    }

    public void render() {
        em.render();
    }

    public void run() {
        running = true;

        while (running) {
            update();
        }
    }

    public EntityManager getState() {
        return em;
    }

    public void putState(EntityManager newem) {
        temp = newem;
        dirty = true;
    }

    public int getDelta() {
        return delta;
    }

    public void stop() {
        running = false;
    }
}
