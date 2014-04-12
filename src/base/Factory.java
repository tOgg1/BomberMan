package base;

import components.*;
import nodes.*;
import systems.*;

import java.util.Random;

import static components.MapEffect.EffectType.SPREAD;
import static components.MapEffect.EffectType.VANISH;
import static components.PowerupPlayer.Feature.BOMB_MAX_COUNT;
import static components.PowerupPlayer.Feature.FLAME_LENGTH;

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
        Moveable moveable = new Moveable();
        Score score = new Score();
        CellPosition cellPosition = new CellPosition();
        ScreenPosition screenPosition = new ScreenPosition();
        BombLayer bombLayer = new BombLayer();
        Size size = new Size();

        renderable.resourceId = renderSystem.getUnitResource();

        createDefaultScreenAndCellPosition(cellPosition, screenPosition, cellX, cellY);
        createDefaultSize(size);

        bombLayer.damage = 1;
        bombLayer.depth = 2;
        bombLayer.maxCount = 1;
        bombLayer.curCount = 1;

        MovementNode moveNode = new MovementNode(cellPosition, screenPosition);
        moveNode.moveable = moveable;
        moveNode.size = size;

        InputNode inputNode = new InputNode(cellPosition);
        inputNode.bombLayer = bombLayer;
        inputNode.moveable = moveable;

        RenderNode renderNode = new RenderNode(size, screenPosition);
        renderNode.renderable = renderable;

        CombatNode combatNode = new CombatNode(cellPosition);
        combatNode.renderable = renderable;
        combatNode.bombLayer = bombLayer;

        combatSystem.addToCombat(player_id, combatNode);
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

        createDefaultScreenAndCellPosition(cellPosition, screenPosition, cellX, cellY);
        createDefaultSize(size);

        renderable.resourceId = renderSystem.getCrateResource();

        collideable.height = 10;
        collideable.margin = 10;

        destroyable.hitPoints = 1;
        destroyable.dropsPowerups = 1;

        MovementNode movnode = new MovementNode(cellPosition, screenPosition);
        movnode.collideable = collideable;
        movnode.size = size;

        RenderNode renderNode = new RenderNode(size, screenPosition);
        renderNode.renderable = renderable;

        CombatNode combatNode = new CombatNode(cellPosition);
        combatNode.destroyable = destroyable;
        combatNode.collideable = collideable;

        renderSystem.addToRender(crate_id, renderNode);
        movementSystem.addToMovement(crate_id, movnode);
        combatSystem.addToCombat(crate_id, combatNode);
        return crate_id;
    }

    public int createTeleporter(int cellX, int cellY, int toX, int toY){
        int teleporter_id = Entity.createNewEntity();
        Teleporter teleporter = new Teleporter();
        CellPosition cellPosition = new CellPosition();
        ScreenPosition screenPosition = new ScreenPosition();
        Renderable renderable = new Renderable();
        Size size = new Size();

        renderable.resourceId = renderSystem.getTeleporterResource();

        createDefaultScreenAndCellPosition(cellPosition, screenPosition, cellX, cellY);
        createDefaultSize(size);


        teleporter.toX = toX;
        teleporter.toY = toY;

        MovementNode node = new MovementNode(cellPosition, screenPosition);
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
        MapEffect effect = new MapEffect();
        Damager damager = new Damager();

        damager.inflictDamage = 1;

        effect.timeRemaining = timeToDetonation;
        effect.effectType = SPREAD;
        effect.parameter = depth;
        effect.createType = MapEffect.CreateType.EXPLOSION;

        createDefaultScreenAndCellPosition(cellPosition, screenPosition, cellX, cellY);
        createDefaultSize(size);

        renderable.resourceId = renderSystem.getBombResource();

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
        int exploder_id = Entity.createNewEntity();
        CellPosition position = new CellPosition();
        Damager damager = new Damager();
        damager.inflictDamage = damage;

        position.x = cellX;
        position.y = cellY;

        MapEffect effect = new MapEffect();
        effect.effectType = VANISH;
        effect.timeRemaining = 1;

        CombatNode combatNode = new CombatNode(position);
        combatNode.damager = damager;

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

        createDefaultScreenAndCellPosition(cellPosition, screenPosition, cellX, cellY);
        createDefaultSize(size);

        renderable.resourceId = renderSystem.getMetalResource();

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
        int expl_id = Entity.createNewEntity();
        Renderable renderable = new Renderable();
        CellPosition cellPosition = new CellPosition();
        ScreenPosition screenPosition = new ScreenPosition();
        Size size = new Size();
        MapEffect effect = new MapEffect();

        effect.effectType = VANISH;
        effect.timeRemaining = 20;

        createDefaultScreenAndCellPosition(cellPosition, screenPosition, cellX, cellY);
        createDefaultSize(size);

        renderable.resourceId = renderSystem.getExplosionResource();

        RenderNode renderNode = new RenderNode(size, screenPosition);
        renderNode.renderable = renderable;

        CombatNode combatNode = new CombatNode(cellPosition);
        combatNode.effect = effect;
        combatNode.renderable = renderable;

        renderSystem.addToRender(expl_id, renderNode);
        combatSystem.addToCombat(expl_id, combatNode);
        schedulerSystem.addTimedEffect(expl_id, effect);

        return expl_id;
    }

    public int createRandomPowerup(int cellX, int cellY) {
        PowerupPlayer powerup = new PowerupPlayer();

        PowerupPlayer.Feature feature;

        Random ran = new Random();
        int ranFeature = ran.nextInt(PowerupPlayer.powerUpCount);

        switch (ranFeature){
            case 0:
                feature = FLAME_LENGTH;
                break;
            case 1:
                feature = BOMB_MAX_COUNT;
                break;
            default:
                feature = FLAME_LENGTH;
        }

        powerup.addsFeature = feature;
        powerup.amount = 1;

        return createPowerup(cellX, cellY, powerup);
    }

    public int createPowerup(int cellX, int cellY, PowerupPlayer powerup){
        int power_id = Entity.createNewEntity();

        Renderable renderable = new Renderable();
        CellPosition cellPosition = new CellPosition();
        ScreenPosition screenPosition = new ScreenPosition();
        Size size = new Size();
        Destroyable destroyable = new Destroyable();
        destroyable.destroyOnWalk = true;
        destroyable.hitPoints = 1;

        switch(powerup.addsFeature){
            default:
            case FLAME_LENGTH:
                renderable.resourceId = renderSystem.getPowerupFireResource();
                break;
            case BOMB_MAX_COUNT:
                renderable.resourceId = renderSystem.getPowerupBombResource();
                break;
        }

        createDefaultScreenAndCellPosition(cellPosition, screenPosition, cellX, cellY);
        createDefaultSize(size);

        CombatNode combatNode = new CombatNode(cellPosition);
        combatNode.renderable = renderable;
        combatNode.destroyable = destroyable;

        RenderNode renderNode = new RenderNode(size, screenPosition);
        renderNode.renderable = renderable;

        MovementNode movementNode = new MovementNode(cellPosition, screenPosition);
        movementNode.powerupPlayer = powerup;

        renderSystem.addToRender(power_id, renderNode);
        movementSystem.addToMovement(power_id, movementNode);
        combatSystem.addToCombat(power_id, combatNode);

        return power_id;
    }

    public void addPowerupToEntity(int entity_id, PowerupPlayer powerup, PowerupNode.PowerupDuration duration, int timeRemaining) {
        PowerupNode node = new PowerupNode();

        node.powerup = powerup;
        node.duration = duration;
        node.timeRemaining = timeRemaining;

        powerupSystem.addToPowerUp(entity_id, node);
        java.lang.System.out.println("Hello");
    }


    public void createDefaultScreenAndCellPosition(CellPosition cellPosition, ScreenPosition screenPosition, int cellX, int cellY){
        cellPosition.x = cellX;
        cellPosition.y = cellY;

        screenPosition.x = (int) (renderSystem.getUnitSize()*(cellX + 0.5));
        screenPosition.y = (int) (renderSystem.getUnitSize()*(cellY + 0.5));
    }

    public void createDefaultSize(Size size){
        size.x = renderSystem.getUnitSize();
        size.y = renderSystem.getUnitSize();
    }

}
