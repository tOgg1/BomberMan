package systems;

import components.CellPosition;
import components.Collideable;
import components.ScreenPosition;
import components.Teleporter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tormod on 11.04.14.
 */
public class MovementSystem extends base.System {

    private Map<Integer, ScreenPosition> screen_positions = new HashMap<>();
    private Map<Integer, CellPosition> positions = new HashMap<>();
    private Map<Integer, Collideable> collideables = new HashMap<>();
    private Map<Integer, Teleporter> teleporters = new HashMap<>();

    @Override
    public void update(float dt) {

    }
}
