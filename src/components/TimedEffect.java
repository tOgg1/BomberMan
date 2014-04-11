package components;

import base.Component;

/**
 * Created by tormod on 11.04.14.
 */
public class TimedEffect extends Component {
    public float timeRemaining;
    public EffectType type;

    public static enum EffectType {
        SPREAD,
        VANISH;
    }
}
