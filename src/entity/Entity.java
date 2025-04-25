package entity;

import java.awt.image.BufferedImage;

public class Entity {
    public String name;
    public int x, y;
    public double speed = 1;
    public BufferedImage image;
    public char direction;
    public boolean moving = false;
    public int value = 0;
    public int money = 0;
    // For smooth movement between tiles
    public int targetX, targetY;
} 