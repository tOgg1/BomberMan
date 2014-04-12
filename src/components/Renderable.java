package components;

/**
 * Created by tormod on 11.04.14.
 */
public class Renderable {

    public int resourceId;
    public Status status;
    public int lifetime;

    public enum Status{
        ACTIVE,
        INACTIVE
    }
}
