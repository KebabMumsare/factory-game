package UI;

import Main.GamePanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
public class UI extends JPanel {
    GamePanel gp;
    Font font;
    BufferedImage[] toolBar;
    JButton marketBtn;
    JButton researchBtn;
    public UI(GamePanel gp) {
        this.gp = gp;
        font = new Font("Arial", Font.PLAIN, gp.tileSize * 2/3);
    }
    public JButton createMarketButton() { 
        try {
            BufferedImage market = ImageIO.read(new File("res/market-icon.png"));
            ImageIcon icon = new ImageIcon(market.getScaledInstance(gp.tileSize*2, gp.tileSize*2, Image.SCALE_SMOOTH));
            marketBtn = new JButton(icon);
            marketBtn.setBounds(gp.width - gp.tileSize*3, gp.height - gp.tileSize*3, gp.tileSize*2, gp.tileSize*2);
            marketBtn.setFocusPainted(false);
            marketBtn.setContentAreaFilled(false);
            marketBtn.setBorderPainted(false);
            marketBtn.setOpaque(false);  // Make button background transparent
            marketBtn.setVisible(true);
            marketBtn.setActionCommand("MARKET");
            marketBtn.addActionListener(gp.actionEvent);

            this.add(marketBtn);
            this.setComponentZOrder(marketBtn, 0);  // Ensure button is on top
            return marketBtn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public JButton createResearchButton() {
        try {
            BufferedImage research = ImageIO.read(new File("res/research-icon.png"));
            ImageIcon icon = new ImageIcon(research.getScaledInstance(gp.tileSize*2, gp.tileSize*2, Image.SCALE_SMOOTH));
            researchBtn = new JButton(icon);
            researchBtn.setBounds(gp.width - gp.tileSize*3, gp.height - gp.tileSize*6, gp.tileSize*2, gp.tileSize*2);
            researchBtn.setFocusPainted(false);
            researchBtn.setContentAreaFilled(false);
            researchBtn.setBorderPainted(false);
            researchBtn.setOpaque(false);  // Make button background transparent
            researchBtn.setVisible(true);
            researchBtn.setActionCommand("RESEARCH");
            researchBtn.addActionListener(gp.actionEvent);

            this.add(researchBtn);
            this.setComponentZOrder(researchBtn, 0);  // Ensure button is on top
            return researchBtn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public void drawUI(Graphics2D g2) {
        g2.setFont(font);
        drawCollectedBox(g2, 5, 5, "Ingots: " + gp.iron);
        drawCollectedBox(g2, 5, (gp.tileSize + 15), "Money: " + gp.money);
        drawToolBar(g2);
    }
    private void drawToolBar(Graphics2D g2) {
        try {
            BufferedImage image = ImageIO.read(new File("res/tool-bar.png"));
            toolBar = new BufferedImage[5];
            toolBar[0] = ImageIO.read(new File("res/store.png"));
            toolBar[2] = ImageIO.read(new File("res/factory.png"));
            toolBar[1] = ImageIO.read(new File("res/conveyor.png"));
            toolBar[3] = ImageIO.read(new File("res/smelter.png"));
            toolBar[4] = ImageIO.read(new File("res/sell.png"));

            int boxX = (gp.width/2) - 238;
            int boxY = gp.height - gp.tileSize*3;
            int boxWidth = 238*2;
            int boxHeight = 30*2;
            g2.setColor(Color.black);
            g2.drawImage(image, boxX, boxY, boxWidth, boxHeight, null);
            g2.drawImage(toolBar[0], boxX + 8, boxY + 8, boxHeight-16, boxHeight-16, null);
            g2.drawImage(toolBar[1], boxX + boxHeight, boxY + 8, boxHeight-16, boxHeight-16, null);
            g2.drawImage(toolBar[2], boxX + boxHeight*2 - 8, boxY + 8, boxHeight-16, boxHeight-16, null);
            g2.drawImage(toolBar[3], boxX + boxHeight*3 - 16, boxY + 8, boxHeight-16, boxHeight-16, null);
            g2.drawImage(toolBar[4], boxX + boxHeight*4 - 24, boxY + 8, boxHeight-16, boxHeight-16, null);

        } catch (Exception e) {
            e.printStackTrace();
        }

        
    }
    private void drawCollectedBox(Graphics2D g2, int x, int y, String text) {
        // Define box dimensions and position
        int boxX = x;
        int boxY = y;
        int boxWidth = gp.tileSize * 4;
        int boxHeight = gp.tileSize;
        
        // Draw translucent background
        g2.setColor(new Color(0, 0, 0, 180)); // Black with 180/255 alpha (translucent)
        g2.fillRect(boxX, boxY, boxWidth, boxHeight);
        
        // Draw border
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(5)); // Set border thickness
        g2.drawRect(boxX, boxY, boxWidth, boxHeight);
        
        // Draw text
        g2.setColor(Color.white);
        String drawText = text;
        FontMetrics metrics = g2.getFontMetrics();
        int textX = boxX + (boxWidth - metrics.stringWidth(drawText)) / 2;
        int textY = boxY + ((boxHeight - metrics.getHeight()) / 2) + metrics.getAscent();
        
        g2.drawString(drawText, textX, textY);
        
        // Reset stroke to default
        g2.setStroke(new BasicStroke(1));
    }
}