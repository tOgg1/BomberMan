package nodes;

import components.PowerupPlayer;

/**
 * Created by tormod on 12.04.14.
 */
public class PowerupNode {

    public PowerupPlayer powerup;
    public PowerupDuration duration;
    public int timeRemaining;

    public static enum PowerupDuration{
        TEMPORARY,
        FOREVER
    }
}
