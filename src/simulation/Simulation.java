package simulation;

import client.Deflect;
import client.Render;
import org.lwjgl.util.vector.Vector3f;
import simulation.entities.EntityManager;
import simulation.entities.Mote;

import java.util.Random;

public class Simulation implements Runnable {
    // TODO: enums
    private static final int NUM_MOTES = 200;

    private static final Random r = new Random();

    EntityManager em;
    EntityManager temp;
    int delta;
    boolean dirty;
    boolean running;

    public Simulation() {
        em = new EntityManager();
        for (int i = 0; i < NUM_MOTES; i++) {
            em.add(new Mote(Render.MOTE_LIST, new Vector3f(r.nextInt(1000), r.nextInt(Deflect.DISPLAY_HEIGHT * 1000 / Deflect.DISPLAY_WIDTH), 0), r.nextInt(6)));
        }
        delta = Util.getDelta();
    }

    public int addPlayer() {
        return em.addPlayer(new Vector3f(r.nextFloat(), r.nextFloat(), r.nextFloat()));
    }

    public void updatePlayer(int index, int forward, int turn) {
        em.updatePlayer(index, forward, turn);
    }

    public void update() {
        if (dirty) {
            em = temp;
            dirty = false;
        }
        delta = Util.getDelta();
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

    public void putState(EntityManager em) {
        temp = em;
        dirty = true;
    }

    public int getDelta() {
        return delta;
    }
}
