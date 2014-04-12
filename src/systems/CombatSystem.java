package systems;

import base.Engine;
import components.BombLayer;
import components.CellPosition;
import components.MapEffect;
import components.Renderable;
import nodes.CombatNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
                    node.renderable.status = Renderable.Status.ACTIVE;

                    if(node.isDamager()){
                        doDamage(node);
                    }

                    if(node.effect.isCreator()){
                        create(node);
                    }

                    if(node.effect.effectType == MapEffect.EffectType.VANISH){
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
        MapEffect effect = node.effect;

        if(effect.effectType != null){
            if(effect.effectType == MapEffect.EffectType.SPREAD){

                // Create in senter
                engine.factory.createExplosion(node.pos.x, node.pos.y);
                engine.factory.createExplosionDamager(node.pos.x, node.pos.y, node.damager.inflictDamage);

                // Propagate in all directions
                propagateCreate(node,  1,  0);
                propagateCreate(node, -1,  0);
                propagateCreate(node,  0,  1);
                propagateCreate(node,  0, -1);

            } else if(effect.effectType == MapEffect.EffectType.SINGULAR){
                engine.factory.createExplosion(node.pos.x, node.pos.y);
                engine.factory.createExplosionDamager(node.pos.x, node.pos.y, node.damager.inflictDamage);
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

        System.out.println(depth);
        while(depth > 0){
            depth--;
            newPos.x += dx;
            newPos.y += dy;
            if(node.effect.createType == MapEffect.CreateType.EXPLOSION){

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
            if(_node == damager) {
                continue;
            }

            if(!_node.isDestroyable()) {
                continue;
            }


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

        if(node.destroyable.dropsPowerups > 0){
            Random ran = new Random();
            ran.setSeed(System.currentTimeMillis());
            double random = ran.nextDouble();

            if(ranFunction(node.destroyable.dropsPowerups) > random){
                engine.factory.createRandomPowerup(node.pos.x, node.pos.y);
            }

        }
        engine.removeEntity(entity_id);

    }

    public boolean updateBombLayer(int entity_id, Integer depth, Integer damage, Integer  maxCount, Integer curCount){
        System.out.println("Hello");
        if(!nodes.containsKey(entity_id))
            return false;

        if(!nodes.get(entity_id).isBombLayer()) {
            return false;
        }
        System.out.println("Hello");

        BombLayer bombLayer = nodes.get(entity_id).bombLayer;

        bombLayer.depth += depth != null ? depth : 0;
        bombLayer.maxCount += maxCount != null ? maxCount : 0;
        bombLayer.curCount += curCount != null ? curCount : 0;
        bombLayer.curCount += maxCount != null ? maxCount : 0;
        bombLayer.damage += damage != null ? damage : 0;

        if(bombLayer.curCount > bombLayer.maxCount)
            bombLayer.curCount = bombLayer.maxCount;
        return true;

    }

    public double ranFunction(int x){
        return 1 - Math.exp(-x);
    }
}
