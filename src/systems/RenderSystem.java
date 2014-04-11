package systems;

import base.Entity;
import components.Renderable;
import components.ScreenPosition;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tormod on 11.04.14.
 */
public class RenderSystem extends base.System{

    private Map<Integer, Renderable> renderables = new HashMap<>();
    private Map<Integer, ScreenPosition> positions = new HashMap<>();
    private Map<Integer, Image> resources = new HashMap<>();

    private JFrame frame;
    private JPanel gamePanel;

    public static int SCREEN_WIDTH = 800, SCREEN_HEIGHT = 600;

    public RenderSystem() {
        gamePanel = new JPanel(){
            @Override
            public void paint(Graphics g) {
                g.setColor(Color.DARK_GRAY);
                g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

                System.out.println("Hello");
                for(Map.Entry<Integer, Renderable> integerRenderableEntry : renderables.entrySet()){
                    Integer entity_id = integerRenderableEntry.getKey();
                    Renderable renderable = integerRenderableEntry.getValue();
                    ScreenPosition pos = positions.get(entity_id);

                    if(pos == null){
                        throw new IllegalStateException("Entity registed in rendersystem has a renderable but no position");
                    }

                    g.drawImage(resources.get(renderable.resourceId), pos.x, pos.y, 128, 128, null);
                }
            }
        };
        frame = new JFrame();
        frame.add(gamePanel);
        frame.pack();
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    /* Render */
    @Override
    public void update(float dt) {
        gamePanel.repaint();
    }

    public void addToRender(int entity_id, Renderable renderable, ScreenPosition position){
        if(renderables.containsKey(entity_id)){
            return;
        }

        renderables.put(entity_id, renderable);
        positions.put(entity_id, position);
    }

    public int loadResource(String pathToImage){
        File img = new File(pathToImage);
        try {
            BufferedImage image = ImageIO.read(img);
            System.out.println(image);
            resources.put(resources.size(), image);
            return resources.size()-1;
        } catch (IOException e) {
            return -1;
        }
    }

    public static void main(String[] args){
        RenderSystem system = new RenderSystem();
        int someEntity = Entity.createNewEntity();
        int picture = system.loadResource("res/bomb.png");

        ScreenPosition pos = new ScreenPosition();
        pos.x = 100;
        pos.y = 100;

        Renderable renderable = new Renderable();
        renderable.resourceId = picture;

        system.addToRender(someEntity, renderable, pos);
        system.update(200);
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        system.update(300);
    }
}
