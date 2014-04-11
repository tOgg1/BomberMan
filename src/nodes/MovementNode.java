package nodes;

import components.*;

/**
 * Created by tormod on 11.04.14.
 */
public class MovementNode {
    public Collideable collideable;
    public Teleporter teleporter;
    public Moveable moveable;
    public CellPosition pos;
    public ScreenPosition screenPos;

    public boolean isCollideable(){
        return collideable != null;
    }

    public boolean isTeleporter(){
        return teleporter != null;
    }

    public boolean isMoveable(){
        return moveable != null;
    }
}
