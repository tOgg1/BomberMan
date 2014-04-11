package systems;

import base.Entity;
import components.Renderable;
import components.ScreenPosition;
import nodes.RenderNode;

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

    private Map<Integer, RenderNode> nodes = new HashMap<>();
    private Map<Integer, Image> resources = new HashMap<>();

    private JFrame frame;
    private JPanel gamePanel;

    public static int SCREEN_WIDTH = 768, SCREEN_HEIGHT = 768;

    private final int sizex, sizey;

    private int unitResource;
    private int bombResource;
    private int crateResource;

    public RenderSystem(int sizex, int sizey) {

        this.sizex = sizex;
        this.sizey = sizey;

        gamePanel = new JPanel(){
            @Override
            public void paint(Graphics g) {
                g.setColor(Color.DARK_GRAY);
                g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

                System.out.println("Hello");
                System.out.println(nodes.size());
                for (Map.Entry<Integer, RenderNode> entry : nodes.entrySet()) {
                    RenderNode node = entry.getValue();
                    Integer entity_id = entry.getKey();
                    Renderable renderable = node.renderable;
                    ScreenPosition pos = node.pos;

                    if(pos == null){
                        throw new IllegalStateException("Entity registed in rendersystem has a renderable but no position");
                    }

                    System.out.println("lol");
                    System.out.println(resources.get(renderable.resourceId));
                    g.drawImage(resources.get(renderable.resourceId), pos.x, pos.y, renderable.sizex,
                            renderable.sizey, null);
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

        /* Load assetse */
        unitResource = loadResource("res/unit.png");
        bombResource = loadResource("res/bomb.png");
        crateResource = loadResource("res/create.png");
    }

    /* Render */
    @Override
    public void update(float dt) {
        gamePanel.repaint();
    }

    public void addToRender(int entity_id, Renderable renderable, ScreenPosition position){
        if(nodes.containsKey(entity_id)){
            return;
        }

        RenderNode node = new RenderNode();
        node.renderable = renderable;
        node.pos = position;
        nodes.put(entity_id, node);
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

    @Override
    public void removeEntity(int id) {
        nodes.remove(id);
    }

    public static void main(String[] args){
        RenderSystem system = new RenderSystem(16, 16);
        int someEntity = Entity.createNewEntity();
        int picture = system.loadResource("res/bomb.png");

        ScreenPosition pos = new ScreenPosition();
        pos.x = 100;
        pos.y = 100;

        Renderable renderable = new Renderable();
        renderable.resourceId = picture;
        renderable.sizex = 64;
        renderable.sizey = 64;

        system.addToRender(someEntity, renderable, pos);
        system.update(200);
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        system.update(300);
    }

    public int getCrateResource() {
        return crateResource;
    }

    public int getUnitResource() {
        return unitResource;
    }

    public int getBombResource() {
        return bombResource;
    }

    public int getUnitSize(){
        return SCREEN_HEIGHT/sizex;
    }
}
