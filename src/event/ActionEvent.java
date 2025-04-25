package event;
import Main.GamePanel;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class ActionEvent implements ActionListener {
    GamePanel gp;
    boolean marketOpen = false;
    boolean researchOpen = false;
    public ActionEvent(GamePanel gp) {
        this.gp = gp;
    }
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        // Get the source button
        JButton sourceButton = (JButton)e.getSource();
        
        // Check button by its name/action command
        switch(sourceButton.getActionCommand()) {
            case "MARKET" -> {
                System.out.println("Market button clicked");
                if (!marketOpen) {
                    gp.add(gp.marketUI);
                    gp.marketUI.setBounds(gp.width / 2 - gp.tileSize * 8, gp.height / 2 - gp.tileSize * 8, gp.tileSize * 16, gp.tileSize * 16);
                    gp.revalidate();
                    gp.repaint();
                    marketOpen = true;
                } else {
                    gp.remove(gp.marketUI);
                    gp.revalidate();
                    gp.repaint();
                    marketOpen = false;
                }

            }
            case "RESEARCH" -> {
                System.out.println("Research button clicked");
                if (!researchOpen) {
                    gp.add(gp.researchUI);
                    gp.researchUI.setBounds(gp.width / 2 - gp.tileSize * 8, gp.height / 2 - gp.tileSize * 8, gp.tileSize * 16, gp.tileSize * 16);
                    gp.revalidate();
                    gp.repaint();
                    researchOpen = true;
                } else {
                    gp.remove(gp.researchUI);
                    gp.revalidate();
                    gp.repaint();
                    researchOpen = false;
                }

            }
            case "CLOSE" -> {
                System.out.println("Close button clicked");
                gp.remove(gp.marketUI);
                gp.remove(gp.researchUI);
                gp.revalidate();
                gp.repaint();
                marketOpen = false;
            }
            case "DEAL" -> {
                gp.marketUI.deal();
            }
            case "DENY" -> {
                gp.marketUI.deny();
            }
        }
        
        gp.requestFocusInWindow();
    }
}
