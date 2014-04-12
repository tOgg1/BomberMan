package nodes;

import components.*;

/**
 * Created by tormod on 11.04.14.
 */
public class MovementNode {
    public Collideable collideable;
    public Teleporter teleporter;
    public Moveable moveable;
    public Size size;
    public PowerupPlayer powerupPlayer;
    public final CellPosition pos;
    public final ScreenPosition screenPos;

    public MovementNode(CellPosition pos, ScreenPosition screenPos) {
        this.pos = pos;
        this.screenPos = screenPos;
    }

    public boolean isCollideable(){
        return collideable != null;
    }

    public boolean isTeleporter(){
        return teleporter != null;
    }

    public boolean isMoveable(){
        return moveable != null;
    }

    public boolean isPowerup(){
        return powerupPlayer != null;
    }
}
