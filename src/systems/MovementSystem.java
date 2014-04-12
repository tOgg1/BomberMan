package systems;

import base.Util;
import components.*;
import nodes.MovementNode;

import java.util.HashMap;
import java.util.Map;

import static components.Moveable.*;

/**
 * Created by tormod on 11.04.14.
 */
public class MovementSystem extends base.System {

    private Map<Integer, MovementNode> nodes = new HashMap<Integer, MovementNode>();

    private int sizex, sizey, tileSizeX, tileSizeY;

    public MovementSystem(int sizex, int sizey, int tileX, int tileY) {
        this.sizex = sizex;
        this.sizey = sizey;
        this.tileSizeX = tileX;
        this.tileSizeY = tileY;
    }

    @Override
    public void removeEntity(int id) {
        nodes.remove(id);
    }

    //TODO: Redo
    @Override
    public void update(float dt) {

        for(Map.Entry<Integer, MovementNode> entry : nodes.entrySet()){

            MovementNode node = entry.getValue();
            CellPosition position = node.pos;
            ScreenPosition screenPosition = node.screenPos;
            Moveable moveable = node.moveable;
            Size size = node.size;

            if(moveable == null){
                continue;
            }

            if(moveable.move){

                if((moveable.curDir & UP) > 0) {

                    if(canMoveTo(node, screenPosition.x, screenPosition.y-moveable.speed)){
                        screenPosition.y -= moveable.speed;
                    }

                    if ((screenPosition.y) / tileSizeY < position.y) {
                        changeCell(node, position.x, position.y - 1);
                    }
                }

                if((moveable.curDir & DOWN) > 0) {

                    if(canMoveTo(node, screenPosition.x, screenPosition.y+moveable.speed)){
                        screenPosition.y += moveable.speed;
                    }

                    if ((screenPosition.y) / tileSizeY > position.y) {
                        changeCell(node, position.x, position.y + 1);
                    }
                }

                if((moveable.curDir & LEFT) > 0) {

                    if(canMoveTo(node, screenPosition.x - moveable.speed, screenPosition.y)){
                        screenPosition.x -= moveable.speed;
                    }

                    if ((screenPosition.x) / tileSizeX < position.x) {
                        changeCell(node, position.x-1, position.y);
                    }
                }

                if((moveable.curDir & RIGHT) > 0) {

                    if(canMoveTo(node, screenPosition.x + moveable.speed, screenPosition.y)){
                        screenPosition.x += moveable.speed;
                    }

                    if ((screenPosition.x) / tileSizeX > position.x) {
                        changeCell(node, position.x+1, position.y);
                    }
                }

                moveable.move = false;
            }
        }
    }

    public boolean canMoveTo(MovementNode node, int screenX, int screenY){
        int cellX = screenX/tileSizeX;
        int cellY = screenY/tileSizeY;

        // Check if outside screen
        // +5 to add a minor margin
        if(screenX - node.size.x/2 + 5 < 0 || screenX + node.size.x/2 - 5> tileSizeX*sizex || screenY - node.size.y/2 +5
                < 0 || screenY + node.size.y - 5 > tileSizeY*sizey){
            System.out.println("Outside the screen");
            return false;
        }

        Util.Rect2 c;
        c = new Util.Rect2(screenX, screenY, node.size.x, node.size.y);

        for (Map.Entry<Integer, MovementNode> entry : nodes.entrySet()) {
            MovementNode _node = entry.getValue();


            if(_node == node)
                continue;

            if(!_node.isCollideable()) {
                continue;
            }

            // Is this node close to the node we want to check?
            if(Math.abs(_node.pos.x-cellX) < 2 && Math.abs(_node.pos.y - cellY) < 2) {

                // Check rectangular collision
                // If it is collideable
                // Check rectangular collision
                Util.Rect2 a, b;
                a = new Util.Rect2(screenX, screenY, node.size.x, node.size.y);
                // Add a 5 px margin
                b = new Util.Rect2(_node.screenPos.x, _node.screenPos.y, _node.size.x - _node.collideable.margin,
                        _node.size.y - _node.collideable.margin);


                if (a.intersects(b))
                    return false;
            }else{
                continue;
            }
        }
        return true;
    }

    public boolean changeCell(MovementNode node, int newX, int newY){
        for (Map.Entry<Integer, MovementNode> entry : nodes.entrySet()) {
            MovementNode _node = entry.getValue();

            if(node == _node)
                continue;

            if(!(_node.pos.x == newX && _node.pos.y == newY)){
                continue;
            }

            if(_node.isCollideable()){
                return false;
            }

            if(_node.isTeleporter()){
                Teleporter tele = _node.teleporter;
                node.pos.x = tele.toX;
                node.pos.y = tele.toY;
                node.screenPos.x = (node.pos.x-1)*tileSizeX;
                node.screenPos.y = (node.pos.y-1)*tileSizeY;
                return true;
            }
        }
        node.pos.x = newX;
        node.pos.y = newY;
        System.out.println(node.pos.toString());
        return true;
    }

    public void addToMovement(int entity_id, MovementNode node){
        nodes.put(entity_id, node);
    }
}
