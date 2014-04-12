package systems;

import base.Engine;
import components.Renderable;
import nodes.CombatNode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tormod on 11.04.14.
 */
public class CombatSystem extends base.System {

    private Map<Integer, CombatNode> nodes = new HashMap<>();
    private final Engine engine;

    public CombatSystem(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void removeEntity(int id) {
        nodes.remove(id);
    }

    public void addToCombat(int entity_id, CombatNode node){
        nodes.put(entity_id, node);
    }

    @Override
    public void update(float dt) {

        for (Map.Entry<Integer, CombatNode> entry : nodes.entrySet()) {
            CombatNode node = entry.getValue();

            // Is effect, are we looking for trouble now, or later?
            if(node.isEffect()){
                // Lets execute!
                if(node.effect.timeRemaining == 0){
                    node.renderable.status = Renderable.Status.ACTIVE;

                    if(node.isDamager()){
                        doDamage(node);
                    }
                // Lets wait and let the scheduler work with it a bit
                }else{
                    continue;
                }
            }else if(node.isDamager()){
                doDamage(node);
            }else{
                // Do nothing
                continue;
            }
        }

    }

    public void doDamage(CombatNode damager){
        for (Map.Entry<Integer, CombatNode> entry : nodes.entrySet()) {
            CombatNode _node = entry.getValue();

            if(_node == damager)
                continue;

            if(!_node.isDestroyable())
                continue;

            if(_node.pos.x != damager.pos.x || _node.pos.y != damager.pos.y){
                continue;
            }

            --_node.destroyable.hitPoints;

            if(-_node.destroyable.hitPoints <= 0) {
                kill(entry.getKey(), _node);
            }
        }
    }

    public void kill(int entity_id, CombatNode node){
        engine.removeEntity(entity_id);
    }
}
