package systems;

import base.Engine;
import components.CellPosition;
import components.Renderable;
import components.TimedEffect;
import nodes.CombatNode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tormod on 11.04.14.
 */
public class CombatSystem extends base.System {

    private Map<Integer, CombatNode> nodes = new HashMap<>();
    private Map<Integer, CombatNode> temps = new HashMap<>();
    private final Engine engine;

    public static final int COMBAT_EMPTY = 1;
    public static final int COMBAT_COLLIDEABLE = 2;
    public static final int COMBAT_DESTROYABLE = 4;

    public CombatSystem(Engine engine) {
        this.engine = engine;

    }

    @Override
    public void removeEntity(int id) {
        nodes.remove(id);
    }

    public void addToCombat(int entity_id, CombatNode node){
        temps.put(entity_id, node);
    }

    @Override
    public void update(float dt) {

        for (Map.Entry<Integer, CombatNode> entry : temps.entrySet()) {
            nodes.put(entry.getKey(), entry.getValue());
        }

        temps.clear();

        for (Map.Entry<Integer, CombatNode> entry : nodes.entrySet()) {
            CombatNode node = entry.getValue();

            // Is effect, are we looking for trouble now, or later?
            if(node.isEffect()){
                // Lets execute!
                if(node.effect.timeRemaining == 0){
                    System.out.println("Execution time!");
                    node.renderable.status = Renderable.Status.ACTIVE;

                    if(node.isDamager()){
                        doDamage(node);
                    }

                    if(node.effect.isCreator()){
                        create(node);
                    }

                    if(node.effect.effectType == TimedEffect.EffectType.VANISH){
                        engine.removeEntity(entry.getKey());
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

    public void create(CombatNode node){
        TimedEffect effect = node.effect;

        if(effect.effectType != null){
            if(effect.effectType == TimedEffect.EffectType.SPREAD){

                // Create spread
                engine.factory.createExplosion(node.pos.x, node.pos.y);

                // Propagate in all directions
                propagateCreate(node,  1,  0);
                propagateCreate(node, -1,  0);
                propagateCreate(node,  0,  1);
                propagateCreate(node,  0, -1);

            } else if(effect.effectType == TimedEffect.EffectType.SINGULAR){
                engine.factory.createExplosion(node.pos.x, node.pos.y);
            } else {
                return;
            }
        }

    }

    public void propagateCreate(CombatNode node, int dx, int dy){
        int depth = node.effect.parameter;

        CellPosition newPos = new CellPosition();
        newPos.x = node.pos.x;
        newPos.y = node.pos.y;

        while(depth < 0){
            depth--;
            newPos.x += dx;
            newPos.y += dy;

            if(node.effect.createType == TimedEffect.CreateType.EXPLOSION){

                engine.factory.createExplosionDamager(newPos.x, newPos.y, node.damager.inflictDamage);
                engine.factory.createExplosion(newPos.x, newPos.y);

                // We've struck our last target in this direction.
                if(collideableInTheWay(newPos.x, newPos.y)) {
                    return;
                }
            }
        }
    }

    public CombatNode getAt(int x, int y){
        for (Map.Entry<Integer, CombatNode> entry : nodes.entrySet()) {

            if(entry.getValue().pos.x == x && entry.getValue().pos.y == y){
                return entry.getValue();
            }
        }
        return null;
    }

    public boolean collideableInTheWay(int x, int y){
        for (Map.Entry<Integer, CombatNode> entry : nodes.entrySet()) {
            if(!entry.getValue().isCollideable())
                continue;
            if(entry.getValue().pos.x == x && entry.getValue().pos.y == y){
                return true;
            }
        }
        return false;
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
