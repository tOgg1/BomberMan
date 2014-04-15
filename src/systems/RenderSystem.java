package systems;

import components.Animatable;
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

    private Map<Integer, RenderNode> temps = new HashMap<>();

    private JFrame frame;
    private JPanel gamePanel;

    public static int SCREEN_WIDTH = 768, SCREEN_HEIGHT = 768;

    private final int sizex, sizey;

    private int unitRedResource;
    private int unitBlueResource;
    private int unitPurpleResources[];
    private int unitYellowResource;
    private int unitPinkResource;

    private int bombResource;
    private int crateResource;
    private int crateDamagedOneResource;
    private int crateDamagedTwoResource;
    private int metalResource;
    private int explosionResource;
    private int teleporterResource;

    private int powerupFireResource;
    private int powerupBombResource;
    private int powerupSpeedResource;
    private int powerupDamageResource;

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
                    Renderable renderable = node.renderable;
                    Animatable animatable = node.animatable;
                    ScreenPosition pos = node.pos;
                    Size size = node.size;

                    if(pos == null) {
                        throw new IllegalStateException("Entity registed in rendersystem has a renderable but no position");
                    }


                    if(node.isRenderable()){
                        g.drawImage(resources.get(renderable.resourceId), pos.x - size.x/2,
                                pos.y - size.y/2, size.x, size.y, null);
                    } else if(node.isAnimatable()){
                        --animatable.nextSequntialAnimation;
                        g.drawImage(resources.get(animatable.resources[animatable.status]), pos.x - size.x/2,
                                pos.y - size.y/2, size.x, size.y, null);
                    }
                }

            }
        };

        gamePanel.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

        frame = new JFrame();
        frame.add(gamePanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setFocusable(true);
        frame.requestFocus();
        frame.setVisible(true);
        frame.setResizable(false);

        /* Load assets */
        loadUnitResources();
        loadObjects();
        loadPowerups();

    }

    public void loadPowerups(){
        powerupBombResource = loadResource("res/powerup/powerup_bomb.png");
        powerupFireResource = loadResource("res/powerup/powerup_fire.png");
        powerupSpeedResource = loadResource("res/powerup/powerup_speed.png");
        powerupDamageResource = loadResource("res/powerup/powerup_damage.png");
    }

    public void loadObjects(){
        bombResource = loadResource("res/object/bomb.png");
        crateResource = loadResource("res/object/crate.png");
        crateDamagedOneResource = loadResource("res/object/crate_damaged_1.png");
        crateDamagedTwoResource = loadResource("res/object/crate_damaged_2.png");
        metalResource = loadResource("res/object/metal.png");
        teleporterResource = loadResource("res/object/teleporter.png");
        explosionResource = loadResource("res/object/explosion.png");
    }

    public void loadUnitResources(){
        unitRedResource = loadResource("res/unit/unit_red.png");
        unitBlueResource = loadResource("res/unit/unit_blue.png");
        unitYellowResource = loadResource("res/unit/unit_yellow.png");
        unitPinkResource = loadResource("res/unit/unit_pink.png");

        unitPurpleResources = new int[8];
        unitPurpleResources[0] = loadResource("res/unit/unit_purple_up_1.png");
        unitPurpleResources[1] = loadResource("res/unit/unit_purple_up_2.png");
        unitPurpleResources[2] = loadResource("res/unit/unit_purple_down_1.png");
        unitPurpleResources[3] = loadResource("res/unit/unit_purple_down_2.png");
        unitPurpleResources[4] = loadResource("res/unit/unit_purple_right_1.png");
        unitPurpleResources[5] = loadResource("res/unit/unit_purple_right_2.png");
        unitPurpleResources[6] = loadResource("res/unit/unit_purple_left_1.png");
        unitPurpleResources[7] = loadResource("res/unit/unit_purple_left_2.png");
    }

    /* Render */
    @Override
    public void update(float dt) {

        for (Map.Entry<Integer, RenderNode> entry : temps.entrySet()) {
            nodes.put(entry.getKey(), entry.getValue());
        }

        temps.clear();

        gamePanel.repaint();
    }

    public void addToRender(int entity_id, RenderNode node){
        if(nodes.containsKey(entity_id) || temps.containsKey(entity_id)){
            return;
        }

        temps.put(entity_id, node);
    }

    public int loadResource(String pathToImage){
        File img = new File(pathToImage);
        try {
            BufferedImage image = ImageIO.read(img);
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

    public int getUnitRedResource() {
        return unitRedResource;
    }

    public int getUnitBlueResource() {
        return unitBlueResource;
    }

    public int[] getUnitPurpleResources() {
        return unitPurpleResources;
    }

    public int getUnitYellowResource() {
        return unitYellowResource;
    }

    public int getUnitPinkResource() {
        return unitPinkResource;
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

    public int getCrateDamagedOneResource() {
        return crateDamagedOneResource;
    }

    public int getCrateDamagedTwoResource() {
        return crateDamagedTwoResource;
    }

    public int getMetalResource() {
        return metalResource;
    }

    public int getPowerupFireResource() {
        return powerupFireResource;
    }

    public int getPowerupBombResource() {
        return powerupBombResource;
    }

    public int getPowerupSpeedResource() {
        return powerupSpeedResource;
    }

    public int getPowerupDamageResource() {
        return powerupDamageResource;
    }

    public int getUnitSize(){
        return SCREEN_HEIGHT/sizex;
    }

    public void addKeyListener(KeyListener listener){
        frame.addKeyListener(listener);
    }
}
