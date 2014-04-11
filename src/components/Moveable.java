package components;

import base.Component;

/**
 * Created by tormod on 11.04.14.
 */
public class Moveable extends Component {

    public boolean move = false;
    public Direction curDir = Direction.UP;
    public int speed = 2;

    public enum Direction{
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
}
