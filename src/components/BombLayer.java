package components;

/**
 * Created by tormod on 11.04.14.
 */
public class BombLayer {
    public int spread = 1;
    public int depth = 1;
    public int damage = 1;
    public int maxCount = 1;
    public int curCount = 1;

    @Override
    public String toString() {
        return "BombLayer{" +
                "spread=" + spread +
                ", depth=" + depth +
                ", damage=" + damage +
                ", maxCount=" + maxCount +
                ", curCount=" + curCount +
                '}';
    }
}
