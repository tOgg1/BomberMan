package base;

import java.util.ArrayList;

/**
 * Created by tormod on 11.04.14.
 */
public class Engine implements Runnable {
    private ArrayList<System> systems = new ArrayList<System>();

    private long frameTime;

    public void run(){
        long now = 0, prev = 0, dt = 0;

        // Bad game loop
        for(;;){
            prev = java.lang.System.currentTimeMillis();

            for (System system : systems) {
                system.update(dt);
            }

            while((now = java.lang.System.currentTimeMillis()) - prev < frameTime){

            }

            dt = now - prev;
        }
    }

    public void setTargetFps(int fps){
        frameTime = fps;
    }

    public void addSystem(System system){
        systems.add(system);
    }
}
