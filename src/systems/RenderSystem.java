package systems;

import components.Renderable;
import components.ScreenPosition;
import components.Size;
import nodes.RenderNode;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
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
    private int explosionResource;
    private int teleporterResource;

    public RenderSystem(int sizex, int sizey) {

        this.sizex = sizex;
        this.sizey = sizey;

        gamePanel = new JPanel(){
            @Override
            public void paint(Graphics g) {
                g.setColor(Color.DARK_GRAY);
                g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

                for (Map.Entry<Integer, RenderNode> entry : nodes.entrySet()) {
                    RenderNode node = entry.getValue();
                    Integer entity_id = entry.getKey();
                    Renderable renderable = node.renderable;
                    ScreenPosition pos = node.pos;
                    Size size = node.size;

                    if(pos == null){
                        throw new IllegalStateException("Entity registed in rendersystem has a renderable but no position");
                    }

                    g.drawImage(resources.get(renderable.resourceId), pos.x - size.x/2,
                            pos.y - size.y/2, size.x, size.y, null);
                }
            }
        };

        frame = new JFrame();
        frame.add(gamePanel);
        frame.pack();
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setFocusable(true);
        frame.requestFocus();
        frame.setVisible(true);
        frame.setResizable(false);

        /* Load assetse */
        unitResource = loadResource("res/unit.png");
        bombResource = loadResource("res/bomb.png");
        crateResource = loadResource("res/crate.png");
        teleporterResource = loadResource("res/teleporter.png");
        explosionResource = loadResource("res/explosion.png");
    }

    /* Render */
    @Override
    public void update(float dt) {
        gamePanel.repaint();
    }

    public void addToRender(int entity_id, RenderNode node){
        if(nodes.containsKey(entity_id)){
            return;
        }

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
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public void removeEntity(int id) {
        nodes.remove(id);
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

    public int getExplosionResource() {
        return explosionResource;
    }

    public int getTeleporterResource() {
        return teleporterResource;
    }

    public int getUnitSize(){
        return SCREEN_HEIGHT/sizex;
    }

    public void addKeyListener(KeyListener listener){
        frame.addKeyListener(listener);
    }
}
