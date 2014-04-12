package nodes;

import components.*;

/**
 * Created by tormod on 12.04.14.
 */
public class AINode{

    public final AI ai;
    public final BombLayer bombLayer;
    public final Moveable moveable;
    public final Destroyable destroyable;
    public final CellPosition cellPosition;
    public final ScreenPosition screenPosition;

    public AINode(AI ai, BombLayer bombLayer, Moveable moveable, Destroyable destroyable, CellPosition cellPosition, ScreenPosition screenPosition) {
        this.ai = ai;
        this.bombLayer = bombLayer;
        this.moveable = moveable;
        this.destroyable = destroyable;
        this.cellPosition = cellPosition;
        this.screenPosition = screenPosition;
    }
}
