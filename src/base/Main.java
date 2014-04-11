package base;

import systems.*;

/**
 * Created by tormod on 11.04.14.
 */
public class Main {

    public static void main(String[] args){
        Engine engine = new Engine();

        int gridSize = 16;

        RenderSystem renderSystem = new RenderSystem(gridSize, gridSize);
        MovementSystem movementSystem = new MovementSystem(gridSize, gridSize);
        CombatSystem combatSystem = new CombatSystem();
        PowerupSystem powerupSystem = new PowerupSystem();
        InputSystem inputSystem = new InputSystem();
        AISystem aiSystem = new AISystem();
        SchedulerSystem schedulerSystem = new SchedulerSystem(engine);

        Factory factory = Factory.instantiate(renderSystem, movementSystem, inputSystem, combatSystem, aiSystem);
        factory.createPlayer(1,1);
        factory.createCrate(2,2);
        factory.createCrate(3,2);

        engine.addSystem(renderSystem);
        engine.addSystem(movementSystem);

        engine.run();

    }
}
