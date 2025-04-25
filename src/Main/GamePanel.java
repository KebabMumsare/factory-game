package Main;
import UI.MarketUI;
import UI.ResearchUI;
import UI.UI;
import entity.EntityManager;
import event.ActionEvent;
import event.KeyboardEvent;
import event.MouseEvent;
import java.awt.*;
import javax.swing.*;
import tile.Tile;
import tile.TileManager;
public class GamePanel extends JPanel implements Runnable {
    public TileManager tileManager;
    public EntityManager entityManager;
    private MouseEvent mouseEvent;
    public UI ui;
    public MarketUI marketUI;
    public ResearchUI researchUI;
    public final int scale = 16;
    public final int tileSize = scale * 2;
    public final int width = tileSize * 26;
    public final int height = tileSize * 20;
    public KeyboardEvent keyboardEvent;
    public Tile selectedTile;
    public ActionEvent actionEvent;
    Thread gameThread;
    int FPS = 60;
    public int iron = 0;
    public int money = 0;
    public int mouseX;
    public int mouseY;
    public GamePanel() {
        this.setLayout(null);
        tileManager = new TileManager(this);
        entityManager = new EntityManager(this, tileManager);
        tileManager.setEntityManager(entityManager);
        mouseEvent = new MouseEvent(this, tileManager, entityManager);
        keyboardEvent = new KeyboardEvent(this, tileManager);
        ui = new UI(this);
        actionEvent = new ActionEvent(this);
        gameThread = new Thread(this);
        marketUI = new MarketUI(this);
        researchUI = new ResearchUI(this);
        addMouseListener(mouseEvent);
        addMouseMotionListener(mouseEvent);
        addKeyListener(keyboardEvent);
        setPreferredSize(new Dimension(width, height));
        setFocusable(true);

        add(ui.createMarketButton());
        add(ui.createResearchButton());
    }
    public void update() {

        entityManager.update();
        tileManager.update();
    }
    
    public void setSelectedTilePosition(int x, int y) {
        mouseX = x;
        mouseY = y;
    }
    public void drawSelectedTile(Graphics2D g2) {
        if (selectedTile != null) {
            // Save the original composite
            Composite originalComposite = g2.getComposite();
            
            // Set transparency (0.0f = fully transparent, 1.0f = fully opaque)
            AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
            g2.setComposite(alphaComposite);
            
            // Draw the semi-transparent image
            g2.drawImage(selectedTile.image, getGridX(mouseX), getGridY(mouseY), tileSize, tileSize, null);
            
            // Restore the original composite
            g2.setComposite(originalComposite);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        tileManager.draw(g2);
        entityManager.draw(g2);
        drawSelectedTile(g2);
        ui.drawUI(g2);
    }
    public void startGameThread() {
        gameThread.start();
    }
    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }
    public int getGridX(int mouseX) {
        return (mouseX / tileSize) * tileSize;
    }

    public int getGridY(int mouseY) {
        return (mouseY / tileSize) * tileSize;
    }
}

