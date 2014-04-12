package systems;

import base.Engine;
import components.PowerupPlayer;
import nodes.InputNode;
import nodes.PowerupNode;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import static components.Moveable.*;

/**
 * Created by tormod on 11.04.14.
 */
public class InputSystem extends base.System implements KeyListener {

    private Map<Integer, InputNode> inputtables = new HashMap<>();

    private boolean[] keyMap = new boolean[0xFFF];

    @Override
    public void removeEntity(int id) {
        inputtables.remove(id);
    }

    public void addToInput(int entity_id, InputNode node){
        inputtables.put(entity_id, node);
    }

    // Do nothing as of now
    @Override
    public void update(float dt) {

        // A bomb shalt be layeth upon thou
        if(keyMap[KeyEvent.VK_SPACE]) {
            Engine engine = Engine.getInstance();
            for (Map.Entry<Integer, InputNode> entry : inputtables.entrySet()) {

                InputNode node = entry.getValue();

                if (node.isBombLayer()) {
                    if(node.bombLayer.curCount == 0) {
                        continue;
                    }

                    engine.factory.createBomb(node.cellPosition.x, node.cellPosition.y,
                                              node.bombLayer.damage, node.bombLayer.depth, 40);
                    PowerupPlayer powerup = new PowerupPlayer();
                    powerup.addsFeature = PowerupPlayer.Feature.BOMB_TEMP_COUNT;
                    powerup.amount = -1;
                    engine.factory.addPowerupToEntity(entry.getKey(), powerup, PowerupNode.PowerupDuration.TEMPORARY, 40);

                    // Set handled
                    keyMap[KeyEvent.VK_SPACE] = false;
                }
            }
        }

        int moveFlag = 0;

        if(keyMap[KeyEvent.VK_LEFT]) {
            moveFlag |= LEFT;
        }

        if(keyMap[KeyEvent.VK_RIGHT]) {
            moveFlag |= RIGHT;
        }

        if(keyMap[KeyEvent.VK_UP]) {
            moveFlag |= UP;
        }

        if(keyMap[KeyEvent.VK_DOWN]) {
            moveFlag |= DOWN;
        }

        if(keyMap[KeyEvent.VK_ESCAPE]) {
            System.exit(0);
        }

        if(moveFlag == 0)
            return;

        for (Map.Entry<Integer, InputNode> entry : inputtables.entrySet()) {
            entry.getValue().moveable.curDir = moveFlag;
            entry.getValue().moveable.move = true;
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {}

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        keyMap[keyEvent.getKeyCode()] = true;

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        keyMap[keyEvent.getKeyCode()] = false;
    }
}
