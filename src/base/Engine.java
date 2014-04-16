package base;

import net.Client;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by tormod on 11.04.14.
 */
public class Engine implements Runnable {
    private ArrayList<System> systems = new ArrayList<>();

    private ArrayList<System> logicSystems = new ArrayList<>();
    private ArrayList<System> prerenderSystems = new ArrayList<>();
    private ArrayList<System> renderSystems = new ArrayList<>();
    private ArrayList<System> postRenderSystems = new ArrayList<>();

    private long frameTime = 1000/24;
    public Factory factory;

    private static Engine singleton;
    private ArrayList<Integer> toDelete = new ArrayList<>();

    public boolean usesMultiplayer;
    public Client forwardTo;

    private Engine() {
        factory = Factory.getInstance();
    }

    private static Random random;

    static{
        random = new Random();
        random.setSeed(java.lang.System.currentTimeMillis());
    }

    public static Random getRandom(){
        return random;
    }

    public static Engine getInstance(){
        if(singleton == null){
            singleton = new Engine();
        }

        return singleton;
    }

    public void run(){
        long now = 0, prev = 0, dt = 0;

        // Bad game loop
        for(;;){

            for (Integer integer : toDelete) {
                for (System system : systems) {
                    system.removeEntity(integer);
                }
            }

            toDelete.clear();

            prev = java.lang.System.currentTimeMillis();

            for (System logicSystem : logicSystems) {
                logicSystem.update(dt);
            }

            for (System prerenderSystem : prerenderSystems) {
                prerenderSystem.update(dt);
            }

            for (System renderSystem : renderSystems) {
                renderSystem.update(dt);
            }

            for (System postRenderSystem : postRenderSystems) {
                postRenderSystem.update(dt);
            }

            now = java.lang.System.currentTimeMillis();

            java.lang.System.out.printf("Loop time: %d ms \r", java.lang.System.currentTimeMillis()- prev);

            // Fill remaining time
            try {
                if(frameTime - (now-prev) > 0)
                    Thread.sleep(frameTime - (now - prev));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            dt = now - prev;
        }
    }

    public void setTargetFps(int fps){
        frameTime = fps;
    }

    public void addSystem(System system, Property property){
        switch(property){
            case LOGIC:
                logicSystems.add(system);
                break;
            case PRERENDER:
                prerenderSystems.add(system);
                break;
            case RENDER:
                renderSystems.add(system);
                break;
            case POSTRENDER:
                postRenderSystems.add(system);
        }
        systems.add(system);
    }

    public void removeEntity(int id){
        toDelete.add(id);
    }

    public static enum Property{
        LOGIC,
        PRERENDER,
        RENDER,
        POSTRENDER
    }
}
