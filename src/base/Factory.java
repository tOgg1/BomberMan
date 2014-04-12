package base;

import components.*;
import nodes.CombatNode;
import nodes.InputNode;
import nodes.MovementNode;
import nodes.RenderNode;
import systems.*;

import static components.TimedEffect.EffectType.*;

/**
 * Created by tormod on 11.04.14.
 */
public class Factory {

    public RenderSystem renderSystem;
    public MovementSystem movementSystem;
    public InputSystem inputSystem;
    public CombatSystem combatSystem;
    public AISystem aiSystem;
    public SchedulerSystem schedulerSystem;
    public MapSystem mapSystem;
    public PowerupSystem powerupSystem;

    private static Factory singleton;

    public Factory() {
    }

    public static Factory getInstance(){
        if(singleton == null){
            singleton = new Factory();
        }
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
        size.y = renderSystem.getUnitSize();

        screenPosition.x = (int) (renderSystem.getUnitSize()*(cellX + 0.5));
        screenPosition.y = (int) (renderSystem.getUnitSize()*(cellY + 0.5));

        bombLayer.damage = 1;
        bombLayer.depth = 5;

        MovementNode moveNode = new MovementNode(cellPosition, screenPosition);
        moveNode.moveable = moveable;
        moveNode.size = size;

        InputNode inputNode = new InputNode(cellPosition);
        inputNode.bombLayer = bombLayer;
        inputNode.moveable = moveable;

        RenderNode renderNode = new RenderNode(size, screenPosition);
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
        collideable.margin = 10;

        destroyable.hitPoints = 1;

        MovementNode movnode = new MovementNode(cellPosition, screenPosition);
        movnode.collideable = collideable;
        movnode.size = size;

        RenderNode renderNode = new RenderNode(size, screenPosition);
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
        node.size = size;

        RenderNode renderNode = new RenderNode(size, screenPosition);
        renderNode.renderable = renderable;

        renderSystem.addToRender(teleporter_id, renderNode);
        movementSystem.addToMovement(teleporter_id, node);
        return teleporter_id;
    }

    public int createBomb(int cellX, int cellY, int damage, int depth, int timeToDetonation){
        int bomb_id = Entity.createNewEntity();
        Renderable renderable = new Renderable();
        ScreenPosition screenPosition = new ScreenPosition();
        CellPosition cellPosition = new CellPosition();

        cellPosition.x = cellX;
        cellPosition.y = cellY;
        Size size = new Size();
        TimedEffect effect = new TimedEffect();
        Damager damager = new Damager();

        damager.inflictDamage = 1;

        effect.timeRemaining = timeToDetonation;
        effect.effectType = SPREAD;
        effect.parameter = depth;
        effect.createType = TimedEffect.CreateType.EXPLOSION;

        screenPosition.x = (int) (renderSystem.getUnitSize()*(cellX + 0.5));
        screenPosition.y = (int) (renderSystem.getUnitSize()*(cellY + 0.5));

        renderable.resourceId = renderSystem.getBombResource();
        size.x = renderSystem.getUnitSize();
        size.y = renderSystem.getUnitSize();

        RenderNode renderNode = new RenderNode(size, screenPosition);
        renderNode.renderable = renderable;

        CombatNode combatNode = new CombatNode(cellPosition);
        combatNode.renderable = renderable;
        combatNode.effect = effect;
        combatNode.damager = damager;

        combatSystem.addToCombat(bomb_id, combatNode);
        schedulerSystem.addTimedEffect(bomb_id, effect);
        renderSystem.addToRender(bomb_id, renderNode);

        return bomb_id;
    }

    public int createExplosionDamager(int cellX, int cellY, int damage){
        java.lang.System.out.println("lol");
        int exploder_id = Entity.createNewEntity();
        CellPosition position = new CellPosition();
        Damager damager = new Damager();
        damager.inflictDamage = damage;

        TimedEffect effect = new TimedEffect();
        effect.effectType = VANISH;
        effect.timeRemaining = 1;

        CombatNode combatNode = new CombatNode(position);

        combatSystem.addToCombat(exploder_id, combatNode);
        schedulerSystem.addTimedEffect(exploder_id, effect);

        return exploder_id;

    }

    public int createBot(int cellX, int cellY){
        int bot_id = Entity.createNewEntity();
        return bot_id;
    }

    public int createMetal(int cellX, int cellY) {
        int metal_id = Entity.createNewEntity();
        Renderable renderable = new Renderable();
        Collideable collideable = new Collideable();
        ScreenPosition screenPosition = new ScreenPosition();
        CellPosition cellPosition = new CellPosition();
        Size size = new Size();

        cellPosition.x = cellX;
        cellPosition.y = cellY;

        screenPosition.x = (int) (renderSystem.getUnitSize()*(cellX + 0.5));
        screenPosition.y = (int) (renderSystem.getUnitSize()*(cellY + 0.5));

        renderable.resourceId = renderSystem.getMetalResource();
        size.x = renderSystem.getUnitSize();
        size.y = renderSystem.getUnitSize();

        collideable.height = 12;
        collideable.margin = 10;

        MovementNode movnode = new MovementNode(cellPosition, screenPosition);
        movnode.collideable = collideable;
        movnode.size = size;

        RenderNode renderNode = new RenderNode(size, screenPosition);
        renderNode.renderable = renderable;

        renderSystem.addToRender(metal_id, renderNode);
        movementSystem.addToMovement(metal_id, movnode);
        return metal_id;

    }

    public int createExplosion(int cellX, int cellY){
        java.lang.System.out.println("hellooo");
        int expl_id = Entity.createNewEntity();
        Renderable renderable = new Renderable();
        CellPosition cellPosition = new CellPosition();
        ScreenPosition screenPosition = new ScreenPosition();
        Size size = new Size();
        TimedEffect effect = new TimedEffect();

        effect.effectType = VANISH;
        effect.timeRemaining = 20;

        cellPosition.x = cellX;
        cellPosition.y = cellY;

        screenPosition.x = (int) (renderSystem.getUnitSize()*(cellX + 0.5));
        screenPosition.y = (int) (renderSystem.getUnitSize()*(cellY + 0.5));

        renderable.resourceId = renderSystem.getExplosionResource();

        size.x = renderSystem.getUnitSize();
        size.y = renderSystem.getUnitSize();

        RenderNode renderNode = new RenderNode(size, screenPosition);
        renderNode.renderable = renderable;

        CombatNode combatNode = new CombatNode(cellPosition);
        combatNode.effect = effect;
        combatNode.renderable = renderable;

        renderSystem.addToRender(expl_id, renderNode);
        combatSystem.addToCombat(expl_id, combatNode);

        return expl_id;
    }
}
