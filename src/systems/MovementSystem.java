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

    private int sizex, sizey;

    public MovementSystem(int sizex, int sizey) {
        this.sizex = sizex;
        this.sizey = sizey;
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
                        screenPosition.y -= 2;
                        if(screenPosition.y/sizey < position.y){
                            if(!moveMoveableInGrid(node, position.x, position.y-1)){
                                screenPosition.y += 2;
                            }
                        }
                        break;
                    case DOWN:
                        screenPosition.y += 2 ;
                        if(screenPosition.y/sizey > position.y){
                            if(!moveMoveableInGrid(node, position.x, position.y+1)){
                                screenPosition.y -= 2;
                            }
                        }
                        break;
                    case LEFT:
                        screenPosition.x -= 2;
                        if(screenPosition.x/sizex < position.x){
                            if(!moveMoveableInGrid(node, position.x-1, position.y)){
                                screenPosition.x += 2;
                            }
                        }
                        break;
                    case RIGHT:
                        screenPosition.x += 2;
                        System.out.println("ScreenPosition: " + screenPosition);
                        System.out.println("Screen/size: " + screenPosition.x/sizex);
                        System.out.println("CellPos: " + position.x);
                        if(screenPosition.x/sizex > position.x){
                            if(!moveMoveableInGrid(node, position.x+1, position.y)){
                                screenPosition.x -= 2;
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

            System.out.println("lol2");
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
                node.screenPos.x = node.pos.x/sizex;
                node.screenPos.y = node.pos.y/sizey;
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
