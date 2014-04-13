package components;

import base.Component;

/**
 * Created by tormod on 12.04.14.
 */
public class Animatable extends Component {
    public int[] resources;
    public int count = 0;
    public int status = 0;
    public int type = 0;
    public int sequentialCount = 0;
    public int nextSequntialAnimation = 1;
    public int sequentialAnimateInterval = 1;

    public final static int ANIMATE_DIRECTIONAL = 1;
    public final static int ANIMATE_SEQUENTIAL = 2;
    public final static int ANIMATE_RANDOM = 4;

    public enum AnimateType{
        DIRECTIONAL,
        SEQUENTIAL,
        RANDOM,
    }
}
