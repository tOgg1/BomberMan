package components;

import base.Component;

/**
 * Created by tormod on 11.04.14.
 */
public class TimedEffect extends Component {
    public float timeRemaining;
    public int parameter;
    public EffectType effectType;
    public CreateType createType;

    public static enum EffectType {
        NONE,
        SINGULAR,
        SPREAD,
        VANISH,
    }

    public static enum CreateType {
        EXPLOSION,
        POWERUP,
        AI,
    }

    public boolean isCreator(){
        return createType != null;
    }
}
