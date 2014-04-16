package nodes;

import components.*;

/**
 * Created by tormod on 11.04.14.
 */
public class RenderNode {

    public Renderable renderable;
    public Animatable animatable;
    public MapEffect effect;
    public Destroyable destroyable;
    public Score score;
    public final Size size;
    public final ScreenPosition pos;


    public RenderNode(Size size, ScreenPosition pos) {
        this.size = size;
        this.pos = pos;
    }

    public boolean isRenderable() { return renderable != null;}

    public boolean isAnimatable() { return animatable != null; }

    public boolean isDestroyable() { return destroyable != null; }

    public boolean isScore() { return score != null;}
}
