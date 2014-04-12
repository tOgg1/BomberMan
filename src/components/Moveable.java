package components;

import base.Component;

/**
 * Created by tormod on 11.04.14.
 */
public class Moveable extends Component {

    public boolean move = false;
    public int curDir = UP;
    public int speed = 2;

    public final static int UP = 0x00000001;
    public final static int DOWN = 0x00000002;
    public final static int RIGHT = 0x00000004;
    public final static int LEFT = 0x00000008;
}
