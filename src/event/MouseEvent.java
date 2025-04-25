package event;

import Main.GamePanel;
import entity.EntityManager;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import tile.TileManager;
public class MouseEvent implements MouseListener, MouseMotionListener{
    GamePanel gp;
    TileManager tileManager;
    EntityManager entityManager;
    public MouseEvent(GamePanel gp, TileManager tileManager, EntityManager entityManager) {
        this.gp = gp;
        this.tileManager = tileManager;
        this.entityManager = entityManager;
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        int gridX = gp.getGridX(e.getX());
        int gridY = gp.getGridY(e.getY());
        // Left mouse button
        if (e.getButton() == java.awt.event.MouseEvent.BUTTON1) {
            tileManager.setTile(gp.selectedTile, gridX, gridY);
        }
        // Right mouse button
        if (e.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            tileManager.removeTile(gridX, gridY);
        }
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {
        
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {

    }

    @Override
    public void mouseDragged(java.awt.event.MouseEvent e) {
        
    }

    @Override
    public void mouseMoved(java.awt.event.MouseEvent e) {
        gp.setSelectedTilePosition(e.getX(), e.getY());
    }

}
