package systems;

import components.CellPosition;
import components.Moveable;
import components.ScreenPosition;
import components.Teleporter;
import nodes.MovementNode;

import java.util.HashMap;
import java.util.Map;

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

    @Override
    public void update(float dt) {

        for(Map.Entry<Integer, MovementNode> entry : nodes.entrySet()){

            MovementNode node = entry.getValue();
            CellPosition position = node.pos;
            ScreenPosition screenPosition = node.screenPos;
            Moveable moveable = node.moveable;

            if(moveable == null){
                continue;
            }

            if(moveable.move){
                switch(moveable.curDir){
                    case UP:
                        screenPosition.y -= moveable.speed;
                        if(screenPosition.y/tileSizeY < position.y){
                            if(!moveMoveableInGrid(node, position.x, position.y-1)){
                                screenPosition.y += moveable.speed;
                            }
                        }
                        break;
                    case DOWN:
                        screenPosition.y += moveable.speed ;
                        if(screenPosition.y/tileSizeY > position.y){
                            if(!moveMoveableInGrid(node, position.x, position.y+1)){
                                screenPosition.y -= moveable.speed;
                            }
                        }
                        break;
                    case LEFT:
                        screenPosition.x -= moveable.speed;
                        if(screenPosition.x/tileSizeX < position.x){
                            if(!moveMoveableInGrid(node, position.x-1, position.y)){
                                screenPosition.x += moveable.speed;
                            }
                        }
                        break;
                    case RIGHT:
                        screenPosition.x += moveable.speed;
                        if(screenPosition.x/tileSizeX > position.x){
                            if(!moveMoveableInGrid(node, position.x+1, position.y)){
                                screenPosition.x -= moveable.speed;
                            }
                        }
                        break;

                }
                moveable.move = false;
            }
        }
    }

    public boolean moveMoveableInGrid(MovementNode node, int newX, int newY){
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
                node.screenPos.x = node.pos.x*tileSizeX;
                node.screenPos.y = node.pos.y*tileSizeY;
                return true;
            }
        }
        node.pos.x = newX;
        node.pos.y = newY;
        return true;
    }

    public void addToMovement(int entity_id, MovementNode node){
        nodes.put(entity_id, node);
    }
}
