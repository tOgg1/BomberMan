package components;

import base.Component;

/**
 * Created by tormod on 12.04.14.
 */
public class Animatable extends Component {
    public int[] resources;
    public int lastShown;
    public AnimateType type;

    public enum AnimateType{
        DIRECTIONAL,
        SEQUENTIAL,
        RANDOM,
    }

}
