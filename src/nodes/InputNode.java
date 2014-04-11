package nodes;

import components.BombLayer;
import components.CellPosition;
import components.Moveable;

/**
 * Created by tormod on 11.04.14.
 */
public class InputNode {
    public Moveable moveable;
    public final CellPosition cellPosition;
    public BombLayer bombLayer;

    public InputNode(CellPosition cellPosition) {
        this.cellPosition = cellPosition;
    }

    public boolean isBombLayer(){
        return bombLayer != null;
    }

    public boolean isMoveable(){
        return moveable != null;
    }
}
