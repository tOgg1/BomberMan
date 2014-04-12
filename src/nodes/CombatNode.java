package nodes;

import components.*;

/**
 * Created by tormod on 11.04.14.
 */
public class CombatNode {

    public Damager damager;
    public final CellPosition pos;
    public TimedEffect effect;
    public Destroyable destroyable;
    public Renderable renderable;
    public Collideable collideable;

    public CombatNode(CellPosition pos) {
        this.pos = pos;
    }

    public boolean isDestroyable(){
        return destroyable != null;
    }

    public boolean isEffect(){
        return effect != null;
    }

    public boolean isDamager(){
        return damager != null;
    }

    public boolean isCollideable(){
        return collideable != null;
    }

}
