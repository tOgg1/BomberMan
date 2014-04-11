package systems;

import components.Renderable;
import components.ScreenPosition;

import javax.imageio.ImageIO;
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

    @Override
    public void update(float dt) {

    }

    public void addToRender(int entity_id, Renderable renderable, ScreenPosition position){
        if(renderables.containsKey(entity_id))
            return;
        renderables.put(entity_id, renderable);
        positions.put(entity_id, position);
    }

    public int loadResource(String pathToImage){
        File img = new File(pathToImage);
        try {
            BufferedImage image = ImageIO.read(img);
            resources.put(resources.size()-1, image);
            return resources.size()-1;
        } catch (IOException e) {
            return -1;
        }
    }
}
