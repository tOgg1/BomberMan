package systems;

import base.Engine;
import components.Moveable;
import nodes.InputNode;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

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

        Moveable.Direction d;
        if(keyMap[KeyEvent.VK_LEFT]) {
            d = Moveable.Direction.LEFT;
        } else if(keyMap[KeyEvent.VK_RIGHT]) {
            d = Moveable.Direction.RIGHT;
        } else if(keyMap[KeyEvent.VK_UP]) {
            d = Moveable.Direction.UP;
        } else if(keyMap[KeyEvent.VK_DOWN]) {
            d = Moveable.Direction.DOWN;
        }else{
            return;
        }

        for (Map.Entry<Integer, InputNode> entry : inputtables.entrySet()) {
            entry.getValue().moveable.curDir = d;
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
