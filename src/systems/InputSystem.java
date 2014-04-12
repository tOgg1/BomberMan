package systems;

import base.Engine;
import nodes.InputNode;

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
                if (entry.getValue().isBombLayer()) {
                    engine.factory.createBomb(entry.getValue().cellPosition.x, entry.getValue().cellPosition.y,
                            entry.getValue().bombLayer.damage, entry.getValue().bombLayer.spread, 40);
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
