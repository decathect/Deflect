package simulation.entities;

import client.Render;
import org.lwjgl.util.vector.Vector3f;
import simulation.Util;

/**
 * Players are physics entities that respond to player input.
 */
public class Player extends Entity {
    /**
     * Scalar for player's forward input
     */
    float enginePower = .01f;
    /**
     * Scalar for player's rotational input
     */
    float rotationSpeed = .5f;

    /**
     * Indicates the Player entity's initial direction
     */
    Vector3f initialDirection;
    /**
     * Indicates the Player entity's current direction
     */
    Vector3f direction;
    /**
     * Indicates the Player entity's rotation from its initial direction in degrees
     */
    int rotation;

    /**
     * Constructs a Player entity with the given display list.
     *
     * @param list number of display list
     */
    public Player(int list) {
        color = new Vector3f(.5f, .5f, 1);
        initialDirection = new Vector3f(1, 0, 0);
        direction = new Vector3f(initialDirection);
        physicsModel.setMass(100);
        size = 10;
        this.list = list;
    }

    /**
     * Updates the Player entity's direction and thrust by multiplying the inputs by the corresponding scalar values.
     *
     * @param forward number of milliseconds since the last update that the player has held forward
     * @param turn    number of milliseconds since the last update that the player has held left and right in aggregate (positive is clockwise)
     */
    public void updatePlayer(int forward, int turn) {
        if (forward > 0) {
            Vector3f t = new Vector3f(direction);
            t.scale(enginePower * forward);
            physicsModel.addForce(t);
        }

        if (turn != 0) {
            rotation += rotationSpeed * turn;
        }

        direction = Util.toVector(rotation);
    }

    /**
     * Renders the Player entity.
     */
    public void render() {
        Render.render(physicsModel.getPosition(), 1, rotation, color, list);
    }
}
