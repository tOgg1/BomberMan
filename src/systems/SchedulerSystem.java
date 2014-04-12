package systems;

import base.Engine;
import components.MapEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tormod on 11.04.14.
 */
public class SchedulerSystem extends base.System{

    private HashMap<Integer, MapEffect> timeables = new HashMap<>();
    private final Engine engineRef;

    public SchedulerSystem(Engine engineRef) {
        this.engineRef = engineRef;
    }

    @Override
    public void removeEntity(int id) {
        timeables.remove(id);
    }

    @Override
    public void update(float dt) {
        ArrayList<Integer> toRemove = new ArrayList<>();
        for (Map.Entry<Integer, MapEffect> entry : timeables.entrySet()) {
            MapEffect eff = entry.getValue();
            --eff.timeRemaining;

            if(eff.timeRemaining < 0){
                if(eff.effectType == MapEffect.EffectType.VANISH || eff.effectType == MapEffect.EffectType.SPREAD)
                    engineRef.removeEntity(entry.getKey());
                else
                    toRemove.add(entry.getKey());
            }
        }
    }

    public boolean addTimedEffect(int entity_id, MapEffect eff){
        if(timeables.containsKey(entity_id))
            return false;

        timeables.put(entity_id, eff);
        return true;
    }
}
