package systems;

import base.Engine;
import components.TimedEffect;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tormod on 11.04.14.
 */
public class SchedulerSystem extends base.System{

    private HashMap<Integer, TimedEffect> timeables;
    private final Engine engineRef;

    public SchedulerSystem(Engine engineRef) {
        this.engineRef = engineRef;
    }

    @Override
    public void removeEntity(int id) {

    }

    @Override
    public void update(float dt) {
        for (Map.Entry<Integer, TimedEffect> entry : timeables.entrySet()) {
            TimedEffect eff = entry.getValue();
            eff.timeRemaining--;

            if(eff.timeRemaining < 0){
                engineRef.removeEntity(entry.getKey());
            }
        }
    }

    public boolean addTimedEffect(int entity_id, TimedEffect eff){
        if(timeables.containsKey(entity_id))
            return false;

        timeables.put(entity_id, eff);
        return true;
    }
}
