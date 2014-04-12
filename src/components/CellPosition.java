package components;

import base.Component;

/**
 * Created by tormod on 11.04.14.
 */
public class CellPosition extends Component {
    public int x, y;


    @Override
    public String toString() {
        return "CellPosition{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object position) {
        if (position instanceof CellPosition) {
            CellPosition pos = (CellPosition) position;
            return x == pos.x && y == pos.y;
        }
        return false;
    }
}
