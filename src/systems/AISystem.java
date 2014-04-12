package systems;

import components.AI;
import nodes.AINode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tormod on 11.04.14.
 */
public class AISystem extends base.System{

    private Map<Integer, AINode> nodes = new HashMap<>();
    private Map<Integer, AINode> temp = new HashMap<>();

    private CombatSystem combatSystem;
    private MovementSystem movementSystem;

    public void addToAI(int entity_id, AINode ai){
        if(nodes.containsKey(entity_id) || temp.containsKey(entity_id))
            return;
        temp.put(entity_id, ai);
    }

    @Override
    public void removeEntity(int id) {
        nodes.remove(id);
    }

    @Override
    public void update(float dt) {
        for (Map.Entry<Integer, AINode> entry : temp.entrySet()) {
            nodes.put(entry.getKey(), entry.getValue());
        }

        nodes.clear();

        for (Map.Entry<Integer, AINode> entry : temp.entrySet()) {
            AI ai = entry.getValue().ai;

        }
    }
}
