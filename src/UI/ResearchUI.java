package UI;

import Main.GamePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ResearchUI extends JPanel {
    GamePanel gp;
    JButton closeBtn;

    public ResearchUI(GamePanel gp) {
        this.gp = gp;
        this.setLayout(null);

        try {
            BufferedImage close = ImageIO.read(new File("res/close-icon.png"));
            ImageIcon icon = new ImageIcon(close.getScaledInstance(gp.tileSize, gp.tileSize, Image.SCALE_SMOOTH));
            closeBtn = new JButton(icon);
            closeBtn.setBounds(gp.tileSize*16-gp.tileSize-7,7, gp.tileSize, gp.tileSize);
            closeBtn.setFocusPainted(false);
            closeBtn.setContentAreaFilled(false);
            closeBtn.setBorderPainted(false);
            closeBtn.setOpaque(false);  // Make button background transparent
            closeBtn.setVisible(true);
            closeBtn.setActionCommand("CLOSE");
            closeBtn.addActionListener(gp.actionEvent);

            this.add(closeBtn);
            this.setComponentZOrder(closeBtn, 0);  // Ensure button is on top
        } catch (Exception e) {
            e.printStackTrace();
        }
        //this.add()
        //this.setBackground(Color.green);
    }
}
