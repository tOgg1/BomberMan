package components;

import base.Component;

/**
 * Created by tormod on 11.04.14.
 */
public class AI extends Component {

    public Difficulty difficulty;
    public Action lastAction;
    public Action curDir;
    public CellPosition targetPos;
    public long lastActionTime;
    public int bombCount;

    enum Difficulty{
        EASY,
        MEDIUM,
        HARD
    }

    enum Action{
        MOVE_RANDOM,
        MOVE_HIDE,
        MOVE_PICKUP,
        LAY_BOMB,
    }
}
