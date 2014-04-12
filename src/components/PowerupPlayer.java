package components;

import base.Component;

/**
 * Created by tormod on 11.04.14.
 */
public class PowerupPlayer extends Component {

    public static int powerUpCount = 3;

    public Feature addsFeature;
    public int amount;

    public static enum Feature {
        FLAME_LENGTH,
        BOMB_MAX_COUNT,
        BOMB_TEMP_COUNT,
        DAMAGE,
        SPEED,
        CAN_JUMP,
        CAN_PUSH;
    }
}
