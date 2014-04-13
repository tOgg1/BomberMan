package base;

import systems.*;

/**
 * Created by tormod on 11.04.14.
 */
public class Main {

    public static void main(String[] args){
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
        creator.buildMap("res/maps/cross.txt");

        engine.addSystem(renderSystem);
        engine.addSystem(movementSystem);
        engine.addSystem(combatSystem);
        engine.addSystem(inputSystem);
        engine.addSystem(aiSystem);
        engine.addSystem(schedulerSystem);
        engine.addSystem(powerupSystem);

        engine.run();
    }
}
