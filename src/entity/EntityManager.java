package entity;

import Main.GamePanel;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import tile.Tile;
import tile.TileManager;

public class EntityManager {
    GamePanel gp;
    TileManager tileManager;
    public ArrayList<Entity> entities = new ArrayList<>();
    public ArrayList<Entity> activeEntities = new ArrayList<>();
    public ArrayList<Entity> inactiveEntities = new ArrayList<>();
    
    public EntityManager(GamePanel gp, TileManager tileManager) {
        this.gp = gp;
        this.tileManager = tileManager;
        entities = createEntitites("src/Entities");
        /*
        try {
            entities[0] = new Entity();
            entities[0].name = "Iron";
            entities[0].image = ImageIO.read(new File("res/iron-bar.png"));
            entities[0].value = 1;
            entities[1] = new Entity();
            entities[1].name = "Iron";
            entities[1].image = ImageIO.read(new File("res/iron-bar.png"));
            entities[1].value = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }

    public ArrayList<Entity> createEntitites(String directory) {
        ArrayList<Entity> entities = new ArrayList<>();
        File jsonFiles = new File(directory);
        System.out.println("Looking for files in: " + jsonFiles.getAbsolutePath());
        
        if (!jsonFiles.exists()) {
            System.out.println("Directory does not exist!");
            return entities;
        }
        
        if (!jsonFiles.isDirectory()) {
            System.out.println("Path is not a directory!");
            return entities;
        }
        
        File[] files = jsonFiles.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));
        
        if (files == null || files.length == 0) {
            System.out.println("No JSON files found in the directory!");
            return entities;
        }
        
        // Print found files
        System.out.println("Found these JSON files:");
        for (File file : files) {
            System.out.println("- " + file.getName());
            String jsonContent = readJsonFile(file.getPath());
            Entity entity = parseEntity(jsonContent);
            entities.add(entity);
        }
        return entities;
    }
    private static String readJsonFile(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
    private static Entity parseEntity(String json) {
        Entity entity = new Entity();
        entity.name = getStringValue(json, "name");
        try {
            entity.image = ImageIO.read(new File(getStringValue(json, "image")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        entity.x = getIntValue(json, "x");
        entity.y = getIntValue(json, "y");
        entity.speed = getIntValue(json, "speed");
        entity.value = getIntValue(json, "value");
        entity.money = getIntValue(json, "money");
        return entity;
    }
    private static String getStringValue(String json, String key) {
        String searchKey = "\"" + key + "\"";
        int keyIndex = json.indexOf(searchKey);
        if (keyIndex == -1) return "";
        
        int valueStart = json.indexOf(":", keyIndex) + 1;
        int valueEnd = json.indexOf(",", valueStart);
        if (valueEnd == -1) {
            valueEnd = json.indexOf("}", valueStart);
        }
        
        String value = json.substring(valueStart, valueEnd).trim();
        return value.replace("\"", "");
    }
    private static int getIntValue(String json, String key) {
        String value = getStringValue(json, key);
        return Integer.parseInt(value);
    }

    public void addEntity(Entity sourceEntity, int x, int y) {
        Entity entity = new Entity();
        entity.name = sourceEntity.name;
        entity.x = x;
        entity.y = y;
        entity.targetX = x;
        entity.targetY = y;
        entity.speed = sourceEntity.speed;
        entity.image = sourceEntity.image;
        entity.value = sourceEntity.value;
        entity.money = sourceEntity.money;

        activeEntities.add(entity);
    }
    public void removeEntity(Entity entity) {
        System.out.println("Removing entity: " + entity.name);
        inactiveEntities.add(entity);
    }
    public void update() {
        // Use iterator to safely remove elements while iterating
        //Iterator<Entity> iterator = activeEntities.iterator();
        for (Entity entity : activeEntities) {
            if (!entity.moving) {
                // Check if entity is on an arrow tile
                Tile currentTile = getTileAtPosition(entity.x, entity.y);
                if (currentTile != null && (currentTile.name.equals("Conveyor") || currentTile.name.equals("Factory"))) { // Arrow tile
                    moveEntityInDirection(entity, currentTile.rotation);
                }
                if (currentTile != null && currentTile.name.equals("Smelter")) { // Smelter tile
                    if (entity.name.equals("Iron Ore")) {
                        currentTile.stack++;
                        removeEntity(entity);
                    }
                    moveEntityInDirection(entity, currentTile.rotation);
                    continue;
                }
                if (currentTile != null && currentTile.name.equals("Store")) { // Ball sold tile
                    if (entity.name.equals("Iron Bar")) {
                        gp.iron++;
                        removeEntity(entity);
                    }
                    if (entity.name.equals("Iron Ore")) {
                        removeEntity(entity);
                    }
                    continue;
                }
                if (currentTile != null && currentTile.name.equals("Sell")) { // Sell tile
                    gp.money += entity.money;
                    System.out.println("Entity: " + entity.money);
                    System.out.println("Money: " + gp.money);
                    removeEntity(entity);
                    continue;
                }
            }
            
            // Handle movement
            if (entity.moving) {
                if (entity.x < entity.targetX) entity.x += entity.speed;
                if (entity.x > entity.targetX) entity.x -= entity.speed;
                if (entity.y < entity.targetY) entity.y += entity.speed;
                if (entity.y > entity.targetY) entity.y -= entity.speed;
                
                // Check if reached target
                if (entity.x == entity.targetX && entity.y == entity.targetY) {
                    entity.moving = false;
                }
            }
        }
        activeEntities.removeAll(inactiveEntities);
    }
    
    private Tile getTileAtPosition(int x, int y) {
        for (Tile tile : tileManager.tilesSet) {
            if (tile.x == x && tile.y == y) {
                return tile;
            }
        }
        return null;
    }
    
    private void moveEntityInDirection(Entity entity, char direction) {
        entity.moving = true;
        entity.direction = direction;
        
        switch (direction) {
            case 'n':
                entity.targetY = entity.y - gp.tileSize;
                break;
            case 's':
                entity.targetY = entity.y + gp.tileSize;
                break;
            case 'e':
                entity.targetX = entity.x + gp.tileSize;
                break;
            case 'w':
                entity.targetX = entity.x - gp.tileSize;
                break;
        }
    }
    
    public void draw(Graphics2D g2) {
        for (Entity entity : activeEntities) {
            g2.drawImage(entity.image, entity.x, entity.y, gp.tileSize, gp.tileSize, null);
        }
    }
}