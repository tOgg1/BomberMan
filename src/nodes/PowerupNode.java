package nodes;

import components.PowerupPlayer;

/**
 * Created by tormod on 12.04.14.
 */
public class PowerupNode {

    public PowerupPlayer powerup;
    public PowerupDuration duration;
    public int timeRemaining;
    public final int entity_target;

    public PowerupNode(int entity_target) {
        this.entity_target = entity_target;
    }

    public static enum PowerupDuration{
        TEMPORARY,
        FOREVER
    }
}
