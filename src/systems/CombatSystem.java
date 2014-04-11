package systems;

import components.TimedEffect;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tormod on 11.04.14.
 */
public class CombatSystem extends base.System {

    private Map<Integer, TimedEffect> effects = new HashMap<Integer, TimedEffect>();

    @Override
    public void removeEntity(int id) {
        effects.remove(id);
    }

    @Override
    public void update(float dt) {

    }
}
