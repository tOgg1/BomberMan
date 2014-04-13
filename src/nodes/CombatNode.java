package nodes;

import components.*;

/**
 * Created by tormod on 11.04.14.
 */
public class CombatNode {

    public Damager damager;
    public final CellPosition pos;
    public MapEffect effect;
    public Destroyable destroyable;
    public Renderable renderable;
    public Animatable animatable;
    public Collideable collideable;
    public BombLayer bombLayer;

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

    public boolean isBombLayer() {return bombLayer != null ;}

    public boolean isAnimateable() {return animatable != null;}

    public boolean isRenderable() {return renderable != null;}

}
