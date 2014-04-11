package base;

/**
 * Created by tormod on 11.04.14.
 */
public class Entity {

    public static int currentEntity;

    static{
        currentEntity = 0;
    }

    public static int createNewEntity(){
        return ++currentEntity;
    }
}
