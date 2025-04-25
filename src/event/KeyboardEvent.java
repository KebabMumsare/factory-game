package event;
import Main.GamePanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import tile.TileManager;


public class KeyboardEvent implements KeyListener {
    GamePanel gp;
    TileManager tileManager;
    
    private final char[] directions = {'n', 'e', 's', 'w'};
    private int currentDirectionIndex = 0;
    
    public KeyboardEvent(GamePanel gamePanel, TileManager tileManager) {
        this.gp = gamePanel;
        this.tileManager = tileManager;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'a') {
            gp.selectedTile = tileManager.selectTile(-1);
            System.out.println("No tile selected");
        }
        if (e.getKeyChar() == '1') {
            gp.selectedTile = tileManager.selectTile(0);
            System.out.println(gp.selectedTile.name);
        }
        if (e.getKeyChar() == '2') {
            gp.selectedTile = tileManager.selectTile(1);
            System.out.println(gp.selectedTile.name);
        }
        if (e.getKeyChar() == '3') {
            gp.selectedTile = tileManager.selectTile(2);
            System.out.println(gp.selectedTile.name);
        }
        if (e.getKeyChar() == '4') {
            gp.selectedTile = tileManager.selectTile(3);
            System.out.println(gp.selectedTile.name);
        }
        if (e.getKeyChar() == '5') {
            gp.selectedTile = tileManager.selectTile(4);
            System.out.println(gp.selectedTile.name);
        }
        if (e.getKeyChar() == 'r') {
            currentDirectionIndex = (currentDirectionIndex + 1) % directions.length;
            tileManager.rotateTile(gp.selectedTile, directions[currentDirectionIndex]);
            System.out.println("Rotated to: " + directions[currentDirectionIndex]);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

}
