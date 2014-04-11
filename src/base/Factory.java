package base;

import components.*;
import nodes.MovementNode;
import systems.*;

/**
 * Created by tormod on 11.04.14.
 */
public class Factory {

    public final RenderSystem renderSystem;
    public final MovementSystem movementSystem;
    public final InputSystem inputSystem;
    public final CombatSystem combatSystem;

    private static Factory singleton;

    private Factory(RenderSystem renderSystem, MovementSystem movementSystem, InputSystem inputSystem,
              CombatSystem combatSystem, AISystem aiSystem) {
        this.renderSystem = renderSystem;
        this.movementSystem = movementSystem;
        this.inputSystem = inputSystem;
        this.combatSystem = combatSystem;
    }

    public static Factory instantiate(RenderSystem renderSystem, MovementSystem movementSystem, InputSystem inputSystem,
                                   CombatSystem combatSystem, AISystem aiSystem){
        singleton = new Factory(renderSystem, movementSystem, inputSystem, combatSystem, aiSystem);
        return singleton;
    }

    public static Factory getInstance(){
        return singleton;
    }

    public int createPlayer(int cellX, int cellY){
        int player_id = Entity.createNewEntity();

        Renderable renderable = new Renderable();
        renderable.resourceId = renderSystem.getUnitResource();
        renderable.sizex = renderSystem.getUnitSize();
        renderable.sizey = 64;
        Damager damager = new Damager();
        Moveable moveable = new Moveable();
        Score score = new Score();
        ScreenPosition screenPosition = new ScreenPosition();
        CellPosition cellPosition = new CellPosition();



        MovementNode moveNode = new MovementNode();
        moveNode.pos = cellPosition;
        moveNode.screenPos = screenPosition;
        moveNode.moveable = moveable;

        renderSystem.addToRender(player_id, renderable, screenPosition);
        movementSystem.addToMovement(player_id, moveNode);

        return player_id;
    }

    public int createCrate(int cellX, int cellY){
        int crate_id = Entity.createNewEntity();
        Renderable renderable = new Renderable();
        Collideable collideable = new Collideable();
        Destroyable destroyable = new Destroyable();
        ScreenPosition screenPos = new ScreenPosition();
        CellPosition cellPosition = new CellPosition();

        renderable.resourceId = renderSystem.getCrateResource();
        renderable.sizex = 64;
        renderable.sizey = 64;
        collideable.height = 10;
        destroyable.hitPoints = 1;

        MovementNode movnode = new MovementNode();
        movnode.collideable = collideable;

        renderSystem.addToRender(crate_id, renderable, screenPos);
        movementSystem.addToMovement(crate_id, movnode);
        return crate_id;
    }

    public int createTeleporter(int cellX, int cellY, int toX, int toY){
        int teleporter_id = Entity.createNewEntity();
        return teleporter_id;
    }

    public int createBomb(int cellX, int cellY){
        int bomb_id = Entity.createNewEntity();
        return bomb_id;
    }

    public int createBot(int cellX, int cellY){
        int bot_id = Entity.createNewEntity();
        return bot_id;
    }
}
