package systems;

import components.PowerupPlayer;
import nodes.PowerupNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static nodes.PowerupNode.PowerupDuration.FOREVER;
import static nodes.PowerupNode.PowerupDuration.TEMPORARY;

/**
 * Created by tormod on 11.04.14.
 */
public class PowerupSystem extends base.System{

    private Map<Integer, PowerupNode> nodes = new HashMap<>();
    private Map<Integer, PowerupNode> temps = new HashMap<>();
    private final MovementSystem movementSystem;
    private final CombatSystem combatSystem;

    public PowerupSystem(MovementSystem movementSystem, CombatSystem combatSystem) {
        this.movementSystem = movementSystem;
        this.combatSystem = combatSystem;
    }

    public void addToPowerUp(int entity_id, PowerupNode node){
        temps.put(entity_id, node);
    }

    @Override
    public void removeEntity(int id) {
        nodes.remove(id);
        temps.remove(id);
    }


    @Override
    public void update(float dt) {

        for(Map.Entry<Integer, PowerupNode> entry : temps.entrySet()) {

            if(entry.getValue().duration == TEMPORARY){
                nodes.put(entry.getKey(), entry.getValue());
                executePowerUp(entry.getKey(), entry.getValue().powerup);
            } else {
                executePowerUp(entry.getKey(), entry.getValue().powerup);
            }
        }
        temps.clear();

        ArrayList<Integer> toRemove = new ArrayList<>();

        for (Map.Entry<Integer, PowerupNode> entry : nodes.entrySet()) {
            PowerupNode node = entry.getValue();

            // Decrement time remaing
            if(node.duration == TEMPORARY){

                // Time to remove
                if(node.timeRemaining == 0){
                    PowerupPlayer temp = new PowerupPlayer();
                    temp.addsFeature = entry.getValue().powerup.addsFeature;
                    temp.amount = -entry.getValue().powerup.amount;
                    executePowerUp(entry.getKey(), temp);
                    toRemove.add(entry.getKey());
                }else{
                    --node.timeRemaining;
                }
            // This should never happen
            } else if(node.duration == FOREVER){
                throw new IllegalStateException("How did you manage this?");
            }
        }

        for (Integer integer : toRemove) {
            nodes.remove(integer);
        }
    }

    public void executePowerUp(int entity_id, PowerupPlayer powerup){
        System.out.println("SUP0");

        if(powerup == null)
            return;
        System.out.println("SUP");

        switch(powerup.addsFeature){
            case BOMB_MAX_COUNT:
                combatSystem.updateBombLayer(entity_id, null, null, powerup.amount, null);
                break;
            case BOMB_TEMP_COUNT:
                combatSystem.updateBombLayer(entity_id, null, null, null, powerup.amount);
                break;
            case CAN_JUMP:

                break;
            case CAN_PUSH:

                break;
            case FLAME_LENGTH:
                combatSystem.updateBombLayer(entity_id, powerup.amount, null, null, null);
                break;
            case SPEED:

                break;

        }
    }
}

