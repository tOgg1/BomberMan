package base;

/**
 * Created by tormod on 11.04.14.
 */
public class Component {
    public int component_id;
    public static int component_counter = 0;

    public Component() {
        component_id = ++component_counter;
    }

}
