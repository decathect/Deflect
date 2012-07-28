package simulation;

import entities.EntityManager;
import entities.Mote;

public class Simulation implements Runnable {
    // TODO: enums
    private static final int NUM_MOTES = 100;

    EntityManager em;
    int delta;
    boolean running;

    public Simulation(boolean render) {
        em = new EntityManager();
        for (int i = 0; i < NUM_MOTES; i++) {
            em.add(new Mote(render));
        }
    }

    public void run() {
        running = true;
        Timer.getDelta();

        while (running) {
            delta = Timer.getDelta();
            em.update(delta);
        }
    }

    public EntityManager getState() {
        return em;
    }

    public void stop() {
        running = false;
    }
}
