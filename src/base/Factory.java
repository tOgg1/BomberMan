package base;

import components.*;
import nodes.InputNode;
import nodes.MovementNode;
import nodes.RenderNode;
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
        Damager damager = new Damager();
        Moveable moveable = new Moveable();
        Score score = new Score();
        CellPosition cellPosition = new CellPosition();
        ScreenPosition screenPosition = new ScreenPosition();
        BombLayer bombLayer = new BombLayer();
        Size size = new Size();

        renderable.resourceId = renderSystem.getUnitResource();
        size.x = renderSystem.getUnitSize();
        size.y =renderSystem.getUnitSize();

        bombLayer.damage = 1;
        bombLayer.depth = 2;

        MovementNode moveNode = new MovementNode(cellPosition, screenPosition);
        moveNode.moveable = moveable;

        InputNode inputNode = new InputNode(cellPosition);
        inputNode.bombLayer = bombLayer;
        inputNode.moveable = moveable;

        RenderNode renderNode = new RenderNode();
        renderNode.pos = screenPosition;
        renderNode.size = size;
        renderNode.renderable = renderable;

        renderSystem.addToRender(player_id, renderNode);
        movementSystem.addToMovement(player_id, moveNode);
        inputSystem.addToInput(player_id, inputNode);
        return player_id;
    }

    public int createCrate(int cellX, int cellY){
        int crate_id = Entity.createNewEntity();
        Renderable renderable = new Renderable();
        Collideable collideable = new Collideable();
        Destroyable destroyable = new Destroyable();
        ScreenPosition screenPosition = new ScreenPosition();
        CellPosition cellPosition = new CellPosition();
        Size size = new Size();

        cellPosition.x = cellX;
        cellPosition.y = cellY;

        screenPosition.x = (int) (renderSystem.getUnitSize()*(cellX + 0.5));
        screenPosition.y = (int) (renderSystem.getUnitSize()*(cellY + 0.5));

        renderable.resourceId = renderSystem.getCrateResource();
        size.x = renderSystem.getUnitSize();
        size.y = renderSystem.getUnitSize();
        collideable.height = 10;
        destroyable.hitPoints = 1;

        MovementNode movnode = new MovementNode(cellPosition, screenPosition);
        movnode.collideable = collideable;

        RenderNode renderNode = new RenderNode();
        renderNode.pos = screenPosition;
        renderNode.size = size;
        renderNode.renderable = renderable;

        renderSystem.addToRender(crate_id, renderNode);
        movementSystem.addToMovement(crate_id, movnode);
        return crate_id;
    }

    public int createTeleporter(int cellX, int cellY, int toX, int toY){
        int teleporter_id = Entity.createNewEntity();
        Teleporter teleporter = new Teleporter();
        CellPosition position = new CellPosition();
        ScreenPosition screenPosition = new ScreenPosition();
        Renderable renderable = new Renderable();
        Size size = new Size();

        renderable.resourceId = renderSystem.getTeleporterResource();
        size.x = renderSystem.getUnitSize();
        size.y = renderSystem.getUnitSize();

        position.x = cellX;
        position.y = cellY;

        screenPosition.x = (int) (renderSystem.getUnitSize()*(cellX + 0.5));
        screenPosition.y = (int) (renderSystem.getUnitSize()*(cellY + 0.5));

        teleporter.toX = toX;
        teleporter.toY = toY;

        MovementNode node = new MovementNode(position, screenPosition);
        node.teleporter = teleporter;

        RenderNode renderNode = new RenderNode();
        renderNode.renderable = renderable;
        renderNode.pos = screenPosition;
        renderNode.size = size;

        renderSystem.addToRender(teleporter_id, renderNode);
        movementSystem.addToMovement(teleporter_id, node);
        return teleporter_id;
    }

    public int createBomb(int cellX, int cellY, int damage, int spread, int timeToDetonation){
        int bomb_id = Entity.createNewEntity();
        Renderable renderable = new Renderable();
        ScreenPosition screenPosition = new ScreenPosition();
        Size size = new Size();

        screenPosition.x = (int) (renderSystem.getUnitSize()*(cellX + 0.5));
        screenPosition.y = (int) (renderSystem.getUnitSize()*(cellY + 0.5));

        renderable.resourceId = renderSystem.getBombResource();
        size.x = renderSystem.getUnitSize();
        size.y = renderSystem.getUnitSize();

        RenderNode renderNode = new RenderNode();
        renderNode.renderable = renderable;
        renderNode.size = size;
        renderNode.pos = screenPosition;

        renderSystem.addToRender(bomb_id, renderNode);

        return bomb_id;
    }

    public int createBot(int cellX, int cellY){
        int bot_id = Entity.createNewEntity();
        return bot_id;
    }
}
