package base;

import net.Client;
import systems.*;

import static base.Engine.Property.LOGIC;
import static base.Engine.Property.PRERENDER;
import static base.Engine.Property.RENDER;

/**
 * Created by tormod on 11.04.14.
 */
public class Main {

    public static void main(String[] args){
        initSingleplayer();
    }

    public static void initMultiplayer(){
        Engine engine = Engine.getInstance();

        int gridSize = 16;

        RenderSystem renderSystem = new RenderSystem(gridSize, gridSize);
        MovementSystem movementSystem = new MovementSystem(gridSize, gridSize,
                renderSystem.getUnitSize(), renderSystem.getUnitSize());

        CombatSystem combatSystem = new CombatSystem(engine);
        PowerupSystem powerupSystem = new PowerupSystem(movementSystem, combatSystem);
        InputSystem inputSystem = new InputSystem();
        AISystem aiSystem = new AISystem();
        SchedulerSystem schedulerSystem = new SchedulerSystem(engine);
        Client client = new Client();

        engine.usesMultiplayer = true;
        engine.forwardTo = client;

        renderSystem.addKeyListener(inputSystem);

        Factory factory = Factory.getInstance();
        engine.factory = factory;

        factory.schedulerSystem = schedulerSystem;
        factory.powerupSystem = powerupSystem;
        factory.renderSystem = renderSystem;
        factory.aiSystem = aiSystem;
        factory.combatSystem = combatSystem;
        factory.inputSystem = inputSystem;
        factory.movementSystem = movementSystem;
        factory.client = client;

        MapCreator creator = new MapCreator(factory);
        creator.buildMap("res/maps/default.txt");

        engine.addSystem(movementSystem, LOGIC);
        engine.addSystem(combatSystem, LOGIC);
        engine.addSystem(inputSystem, LOGIC);
        engine.addSystem(aiSystem, LOGIC);
        engine.addSystem(schedulerSystem, LOGIC);
        engine.addSystem(powerupSystem, LOGIC);
        engine.addSystem(renderSystem, RENDER);
        engine.addSystem(client, PRERENDER);

        engine.run();
    }

    public static void initSingleplayer(){
        Engine engine = Engine.getInstance();

        int gridSize = 16;

        RenderSystem renderSystem = new RenderSystem(gridSize, gridSize);
        MovementSystem movementSystem = new MovementSystem(gridSize, gridSize,
                renderSystem.getUnitSize(), renderSystem.getUnitSize());

        CombatSystem combatSystem = new CombatSystem(engine);
        PowerupSystem powerupSystem = new PowerupSystem(movementSystem, combatSystem);
        InputSystem inputSystem = new InputSystem();
        AISystem aiSystem = new AISystem();
        SchedulerSystem schedulerSystem = new SchedulerSystem(engine);

        renderSystem.addKeyListener(inputSystem);

        engine.usesMultiplayer = false;

        Factory factory = Factory.getInstance();
        engine.factory = factory;

        factory.schedulerSystem = schedulerSystem;
        factory.powerupSystem = powerupSystem;
        factory.renderSystem = renderSystem;
        factory.aiSystem = aiSystem;
        factory.combatSystem = combatSystem;
        factory.inputSystem = inputSystem;
        factory.movementSystem = movementSystem;

        MapCreator creator = new MapCreator(factory);
        creator.buildMap("res/maps/default.txt");

        engine.addSystem(movementSystem, LOGIC);
        engine.addSystem(combatSystem, LOGIC);
        engine.addSystem(inputSystem, LOGIC);
        engine.addSystem(aiSystem, LOGIC);
        engine.addSystem(schedulerSystem, LOGIC);
        engine.addSystem(powerupSystem, LOGIC);
        engine.addSystem(renderSystem, RENDER);

        engine.run();
    }
}
