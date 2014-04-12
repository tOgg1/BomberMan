package components;

import base.Component;

/**
 * Created by tormod on 11.04.14.
 */
public class PowerupPlayer extends Component {

    public Feature addsFeature;
    public int amount;

    enum Feature {
        FLAME_LENGTH,
        BOMB_AMOUNT,
        SPEED,
        CAN_JUMP,
        CAN_PUSH
    }
}
