package systems;

import base.Util;
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
    private GamePanel gamePanel;
    private MenuPanel menuPanel;
    private InfoPanel infoPanel;
    private LoadingPanel loadingPanel;

    private ParentPanel parentPanel;

    public static int GAME_WIDTH = 768, GAME_HEIGHT = 768;
    public static int INFO_WIDTH = 768, INFO_HEIGHT = 50;

    private final int sizex, sizey;

    private int backgroundResource;
    private int loadingResource;

    private int unitRedResources[];
    private int unitBlueResources[];
    private int unitPurpleResources[];
    private int unitGreenResources[];

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

    private boolean isInMenu;
    private boolean isInLoading;
    private boolean isRunning;

    public RenderSystem(int sizex, int sizey) {

        this.sizex = sizex;
        this.sizey = sizey;

        isInLoading = false;
        isInMenu = false;
        isRunning = false;

        gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        gamePanel.setBounds(0, INFO_HEIGHT, GAME_WIDTH, GAME_HEIGHT);

        infoPanel = new InfoPanel();
        infoPanel.setPreferredSize(new Dimension());

        menuPanel = new MenuPanel();

        loadingPanel = new LoadingPanel();

        parentPanel = new ParentPanel(gamePanel, infoPanel, menuPanel, loadingPanel);

        frame = new JFrame();
        frame.setLayout(null);

        gamePanel.setFont(Util.mankSans);
        frame.setFont(Util.mankSans);
        menuPanel.setFont(Util.mankSans);
        infoPanel.setFont(Util.mankSans);
        parentPanel.setFont(Util.mankSans);

        frame.add(parentPanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setFocusable(true);
        frame.requestFocus();
        frame.setVisible(true);
        frame.setResizable(false);

        /* Load assets */
        loadBackgrounds();
        loadUnitResources();
        loadObjects();
        loadPowerups();
    }

    public void setInMenu(){
        isInMenu = true;
        isInLoading = false;
        isRunning = false;
    }

    public void setInLoading(){
        isInMenu = false;
        isInLoading = true;
        isRunning = false;
    }

    public void setInGame(){
        isInMenu = false;
        isInLoading = false;
        isRunning = true;
    }

    private void loadBackgrounds(){
        backgroundResource = loadResource("res/backgrounds/background.png");
        loadingResource = loadResource("res/backgrounds/loadingscreen.png");
    }

    private void loadPowerups(){
        powerupBombResource = loadResource("res/powerup/powerup_bomb.png");
        powerupFireResource = loadResource("res/powerup/powerup_fire.png");
        powerupSpeedResource = loadResource("res/powerup/powerup_speed.png");
        powerupDamageResource = loadResource("res/powerup/powerup_damage.png");
    }

    private void loadObjects(){
        bombResource = loadResource("res/object/bomb.png");
        crateResource = loadResource("res/object/crate.png");
        crateDamagedOneResource = loadResource("res/object/crate_damaged_1.png");
        crateDamagedTwoResource = loadResource("res/object/crate_damaged_2.png");
        metalResource = loadResource("res/object/metal.png");
        teleporterResource = loadResource("res/object/teleporter.png");
        explosionResource = loadResource("res/object/explosion.png");
    }

    private void loadUnitResources(){

        unitPurpleResources = new int[8];
        unitPurpleResources[0] = loadResource("res/unit/unit_purple_up_1.png");
        unitPurpleResources[1] = loadResource("res/unit/unit_purple_up_2.png");
        unitPurpleResources[2] = loadResource("res/unit/unit_purple_down_1.png");
        unitPurpleResources[3] = loadResource("res/unit/unit_purple_down_2.png");
        unitPurpleResources[4] = loadResource("res/unit/unit_purple_right_1.png");
        unitPurpleResources[5] = loadResource("res/unit/unit_purple_right_2.png");
        unitPurpleResources[6] = loadResource("res/unit/unit_purple_left_1.png");
        unitPurpleResources[7] = loadResource("res/unit/unit_purple_left_2.png");

        unitRedResources = new int[8];
        unitRedResources[0] = loadResource("res/unit/unit_red_up_1.png");
        unitRedResources[1] = loadResource("res/unit/unit_red_up_2.png");
        unitRedResources[2] = loadResource("res/unit/unit_red_down_1.png");
        unitRedResources[3] = loadResource("res/unit/unit_red_down_2.png");
        unitRedResources[4] = loadResource("res/unit/unit_red_right_1.png");
        unitRedResources[5] = loadResource("res/unit/unit_red_right_2.png");
        unitRedResources[6] = loadResource("res/unit/unit_red_left_1.png");
        unitRedResources[7] = loadResource("res/unit/unit_red_left_2.png");

        unitGreenResources = new int[8];
        unitGreenResources[0] = loadResource("res/unit/unit_green_up_1.png");
        unitGreenResources[1] = loadResource("res/unit/unit_green_up_2.png");
        unitGreenResources[2] = loadResource("res/unit/unit_green_down_1.png");
        unitGreenResources[3] = loadResource("res/unit/unit_green_down_2.png");
        unitGreenResources[4] = loadResource("res/unit/unit_green_right_1.png");
        unitGreenResources[5] = loadResource("res/unit/unit_green_right_2.png");
        unitGreenResources[6] = loadResource("res/unit/unit_green_left_1.png");
        unitGreenResources[7] = loadResource("res/unit/unit_green_left_2.png");

        unitBlueResources = new int[8];
        unitBlueResources[0] = loadResource("res/unit/unit_blue_up_1.png");
        unitBlueResources[1] = loadResource("res/unit/unit_blue_up_2.png");
        unitBlueResources[2] = loadResource("res/unit/unit_blue_down_1.png");
        unitBlueResources[3] = loadResource("res/unit/unit_blue_down_2.png");
        unitBlueResources[4] = loadResource("res/unit/unit_blue_right_1.png");
        unitBlueResources[5] = loadResource("res/unit/unit_blue_right_2.png");
        unitBlueResources[6] = loadResource("res/unit/unit_blue_left_1.png");
        unitBlueResources[7] = loadResource("res/unit/unit_blue_left_2.png");
    }

    /* Render */
    @Override
    public void update(float dt) {

        synchronized (nodes){
            for (Map.Entry<Integer, RenderNode> entry : temps.entrySet()) {
                nodes.put(entry.getKey(), entry.getValue());
            }
        }

        temps.clear();

        parentPanel.repaint();
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

    private class ParentPanel extends JPanel {

        private final GamePanel gamePanel;
        private final InfoPanel infoPanel;
        private final MenuPanel menuPanel;
        private final LoadingPanel loadingPanel;

        private ParentPanel(GamePanel gamePanel, InfoPanel infoPanel, MenuPanel menuPanel, LoadingPanel loadingPanel) {
            this.gamePanel = gamePanel;
            this.infoPanel = infoPanel;
            this.menuPanel = menuPanel;
            this.loadingPanel = loadingPanel;
        }

        @Override
        public void paint(Graphics g) {
            if(isRunning){
                gamePanel.repaint();
            }else if(isInMenu){
                menuPanel.repaint();
            }else if(isInLoading){
                loadingPanel.repaint();
            }
        }
    }

    protected class LoadingPanel extends JPanel{

        protected LoadingPanel() {

        }

        @Override
        public void paint(Graphics g) {
            g.drawImage(resources.get(loadingResource), 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        }
    }

    private class MenuPanel extends JPanel {


    }

    private class InfoPanel extends JPanel {
        @Override
        public void paint(Graphics g) {
            g.setColor(Color.DARK_GRAY);
        }
    }

    private class GamePanel extends JPanel {
        @Override
        public void paint(Graphics g) {
            g.setColor(Color.DARK_GRAY);
            g.drawImage(resources.get(backgroundResource), 0, 0, GAME_WIDTH, GAME_HEIGHT, null);

            synchronized (nodes){
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

                        if(node.isDestroyable()){
                            g.setColor(Color.white);
                            String hp = "Hitpoints: " + node.destroyable.hitPoints;
                            g.drawChars(hp.toCharArray(), 0, hp.length(), 10, 10);
                        }

                        if(node.isScore()){
                            g.setColor(Color.white);
                            g.drawString("Score: " + node.score.kills + "kills", 0, 25);
                        }
                    }
                }
            }
        }

    }


    @Override
    public void removeEntity(int id) {
        nodes.remove(id);
    }

    public int getCrateResource() {
        return crateResource;
    }

    public int[] getUnitPurpleResources() {
        return unitPurpleResources;
    }

    public int[] getUnitRedResources() {
        return unitRedResources;
    }

    public int[] getUnitBlueResources() {
        return unitBlueResources;
    }

    public int[] getUnitGreenResources() {
        return unitGreenResources;
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
        return GAME_HEIGHT /sizex;
    }

    public void addKeyListener(KeyListener listener){
        frame.addKeyListener(listener);
    }
}
