package systems;

import components.Moveable;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tormod on 11.04.14.
 */
public class InputSystem extends base.System implements KeyListener {

    private Map<Integer, Moveable> moveables = new HashMap<>();

    @Override
    public void removeEntity(int id) {
        moveables.remove(id);
    }

    // Do nothing
    @Override
    public void update(float dt) {
        return;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {}

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        Moveable.Direction d;
        switch (keyEvent.getKeyCode()){
            case KeyEvent.VK_LEFT:
                d = Moveable.Direction.LEFT;
                break;
            case KeyEvent.VK_RIGHT:
                d = Moveable.Direction.RIGHT;
                break;
            case KeyEvent.VK_UP:
                d = Moveable.Direction.UP;
                break;
            case KeyEvent.VK_DOWN:
                d = Moveable.Direction.DOWN;
                break;
            default:
                return;

        }

        for (Map.Entry<Integer, Moveable> entry : moveables.entrySet()) {
            entry.getValue().curDir = d;
            entry.getValue().move = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        for (Map.Entry<Integer, Moveable> entry : moveables.entrySet()) {
            entry.getValue().move = true;
        }
    }
}
