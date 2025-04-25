package tile;
import Main.GamePanel;
import entity.Entity;
import entity.EntityManager;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import javax.imageio.ImageIO;
public class TileManager {
    public ArrayList<Tile> tiles = new ArrayList<>();
    public ArrayList<Tile> tilesSet = new ArrayList<>();
    GamePanel gp;
    EntityManager entityManager;
    public TileManager(GamePanel gp) {
        this.gp = gp;
        tiles = createTiles("src/Tiles");
        tiles.sort(Comparator.comparingInt(p -> p.id));
    }
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public ArrayList<Tile> createTiles(String directory) {
        ArrayList<Tile> tiles = new ArrayList<>();
        File jsonFiles = new File(directory);
        System.out.println("Looking for files in: " + jsonFiles.getAbsolutePath());
        
        if (!jsonFiles.exists()) {
            System.out.println("Directory does not exist!");
            return tiles;
        }
        
        if (!jsonFiles.isDirectory()) {
            System.out.println("Path is not a directory!");
            return tiles;
        }
        
        File[] files = jsonFiles.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));
        
        if (files == null || files.length == 0) {
            System.out.println("No JSON files found in the directory!");
            return tiles;
        }
        
        // Print found files
        System.out.println("Found these JSON files:");
        for (File file : files) {
            System.out.println("- " + file.getName());
            String jsonContent = readJsonFile(file.getPath());
            Tile tile = parseTile(jsonContent);
            tiles.add(tile);
        }
        return tiles;
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
    private static Tile parseTile(String json) {
        Tile tile = new Tile();
        tile.id = getIntValue(json, "id");
        tile.name = getStringValue(json, "name");
        try {
            String imagePath = getStringValue(json, "image");
            String activeImagePath = getStringValue(json, "activeImage");
            
            tile.image = ImageIO.read(new File(imagePath));
            tile.activeImage = ImageIO.read(new File(activeImagePath));
            
            if (tile.image == null) System.out.println("Failed to load normal image!");
            if (tile.activeImage == null) System.out.println("Failed to load active image!");
            
        } catch (IOException e) {
            System.out.println("Error loading images for " + tile.name + ": " + e.getMessage());
            e.printStackTrace();
        }
        tile.rotation = getStringValue(json, "rotation").charAt(0);
        tile.x = getIntValue(json, "x");
        tile.y = getIntValue(json, "y");
        tile.functional = getBooleanValue(json, "functional");
        tile.actionInterval = getIntValue(json, "actionInterval");
        tile.lastActionTime = getIntValue(json, "lastActionTime");

        return tile;
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
    private static boolean getBooleanValue(String json, String key) {
        String value = getStringValue(json, key);
        return Boolean.parseBoolean(value);
    }
    private static int getIntValue(String json, String key) {
        String value = getStringValue(json, key);
        return Integer.parseInt(value);
    }

    public void setTile(Tile sourceTile, int x, int y) {
        if (sourceTile != null) {
            // Create a new tile instance
            Tile newTile = new Tile();
            newTile.name = sourceTile.name;
            newTile.image = sourceTile.image;
            newTile.activeImage = sourceTile.activeImage;
            newTile.x = x;
            newTile.y = y;
            newTile.rotation = sourceTile.rotation;
            newTile.functional = sourceTile.functional;
            newTile.actionInterval = sourceTile.actionInterval;
            
            tilesSet.add(newTile);
        }
    }
    public void removeTile(int gridX, int gridY){
        Tile tile = getTileAtPosition(gridX, gridY);
        if (tile != null) {
            tilesSet.remove(tile);
        }
    }
    public void rotateTile(Tile tile, char direction) {
        tile.rotation = direction;
    }
    public Tile selectTile(int index) {
        if (index == -1) {
            return null;
        }
        return tiles.get(index);
    }
    private Tile getTileAtPosition(int x, int y) {
        for (Tile tile : tilesSet) {
            if (tile.x == x && tile.y == y) {
                return tile;
            }
        }
        return null;
    }

    public void draw(Graphics2D g2) {
        for (int i = 0; i < tilesSet.size(); i++) {
            Tile tile = tilesSet.get(i);
            if (tile != null) {
                // Store the current transform
                java.awt.geom.AffineTransform oldTransform = g2.getTransform();
                
                // Calculate rotation angle in radians based on direction
                double angle = 0;
                switch (tile.rotation) {
                    case 'n': angle = 0; break;
                    case 'e': angle = Math.PI / 2; break; // 90 degrees
                    case 's': angle = Math.PI; break;     // 180 degrees
                    case 'w': angle = -Math.PI / 2; break; // -90 degrees
                }
                
                // Move to tile position, rotate, then draw
                g2.translate(tile.x + gp.tileSize/2, tile.y + gp.tileSize/2);
                g2.rotate(angle);
                
                // Simplified image selection logic
                if (tile.name.equals("Smelter")) {
                    g2.drawImage(tile.isSmelting ? tile.activeImage : tile.image, 
                        -gp.tileSize/2, -gp.tileSize/2, 
                        gp.tileSize, gp.tileSize, null);
                } else {
                    g2.drawImage(tile.image, -gp.tileSize/2, -gp.tileSize/2, 
                        gp.tileSize, gp.tileSize, null);
                }
                
                // Restore the original transform
                g2.setTransform(oldTransform);
            }
        }
    }
    public void update() {
        long currentTime = System.currentTimeMillis();
        for (Tile tile : tilesSet) {
            if (tile.functional && tile.name.equals("Factory")) {
                if (currentTime - tile.lastActionTime >= tile.actionInterval) {
                    Entity ballEntity = entityManager.entities.get(1);
                    entityManager.addEntity(ballEntity, tile.x, tile.y);
                    tile.lastActionTime = currentTime;
                }
            }
            if (tile.functional && tile.name.equals("Smelter")) {
                if (tile.stack > 0 && !tile.isSmelting) {
                    tile.isSmelting = true;
                    System.out.println("Smelter started smelting. Stack: " + tile.stack); // Debug log
                    tile.lastActionTime = currentTime;
                }
                if (tile.isSmelting && currentTime - tile.lastActionTime >= tile.actionInterval) {
                    Entity IronBarEntity = entityManager.entities.get(0);
                    entityManager.addEntity(IronBarEntity, tile.x, tile.y);
                    tile.stack--;
                    tile.isSmelting = false;
                    System.out.println("Smelter finished smelting. Stack: " + tile.stack); // Debug log
                }
            }
        }
    }
    public void updateSmelter() {
        for (Tile tile : tilesSet) {
            if (tile.functional == true && tile.name.equals("Smelter")) {
                tile.image = tile.isSmelting ? tile.activeImage : tile.image;
            }
        }
    }
}