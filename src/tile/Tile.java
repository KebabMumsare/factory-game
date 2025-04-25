package tile;

import entity.Entity;
import java.awt.image.BufferedImage;

public class Tile {
    public int id;
    public String name;
    public String type;
    public char rotation;
    public int x, y;
    public BufferedImage image;
    public BufferedImage activeImage;
    
    public long lastActionTime;
    public int actionInterval; // 2000 milliseconds = 2 seconds
    public boolean functional;
    public Entity entity = null;
    public int stack;
    public boolean isSmelting = false;

}
