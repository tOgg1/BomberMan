package base;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by tormod on 12.04.14.
 */
public class Util {

    public static Font mankSans;

    static{
        try {
            mankSans = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("res/MankSans.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(mankSans);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class Rect2{

        public int x, y, w, h;

        public Rect2(int x, int y, int width, int height){
            this.x = x;
            this.y = y;
            this.w = width;
            this.h = height;
        }

        public boolean intersects(Rect2 rect){
            return !(x - w/2 > rect.x + rect.w/2 ||
                     x + w/2 < rect.x - rect.w/2 ||
                     y + h/2 < rect.y - rect.h/2 ||
                     y - h/2 > rect.y + rect.h/2);
        }

        public boolean contains(int _x, int _y) {
            return _x > x - w / 2 && _x < x + w / 2 && _y < y + h / 2 && _y > y - h / 2;
        }

        @Override
        public String toString() {
            return "Rect2{" +
                    "x=" + x +
                    ", y=" + y +
                    ", w=" + w +
                    ", h=" + h +
                    '}';
        }
    }

}
