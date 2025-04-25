package UI;

import Main.GamePanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

public class MarketUI extends JPanel {
    GamePanel gp;
    JButton closeBtn;
    JPanel ordersPanel; // Panel to hold all orders
    JScrollPane scrollPane; // Scroll pane to make orders scrollable
    
    public MarketUI(GamePanel gp) {
        this.gp = gp;
        this.setLayout(null);
        
        // Create the orders panel
        ordersPanel = new JPanel();
        ordersPanel.setLayout(null);
        ordersPanel.setBackground(null); // Make it transparent
        
        // Create scroll pane and add orders panel to it
        scrollPane = new JScrollPane(ordersPanel);
        scrollPane.setBounds(
            (gp.tileSize * 16/2-gp.tileSize * 15/2),
            (int)(gp.tileSize * 2.5),
            gp.tileSize * 15,
            gp.tileSize * 13
        );
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.lightGray);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Add these lines to increase scroll speed
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(gp.tileSize/2); // Adjust this value to change scroll speed
        verticalScrollBar.setBlockIncrement(gp.tileSize * 3); // Adjust page-up/down speed

        // Add scroll pane to main panel
        this.add(scrollPane);
        
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
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //draw background and border
        g2.setColor(Color.lightGray);
        g2.fillRoundRect(5, 5, gp.tileSize * 16-10, gp.tileSize * 16-10, 10, 10);
        g2.setColor(Color.gray);
        g2.setStroke(new BasicStroke(5)); // Set border thickness
        g2.drawRoundRect(2, 2, gp.tileSize * 16-5, gp.tileSize * 16-5, 20, 20);
        
        //draw title text
        g2.setFont(new Font("Arial", Font.BOLD, gp.tileSize));
        g2.setColor(Color.black);
        g2.drawString("Market", 10, 35);
        updateOrders(5); // Example: create 10 orders
    }
    private void updateOrders(int numOrders) {
        ordersPanel.removeAll(); // Clear existing orders
        
        // Calculate total height needed for all orders
        int totalHeight = (int)(numOrders * gp.tileSize * 3.5);
        ordersPanel.setPreferredSize(new Dimension(gp.tileSize * 15, totalHeight));
        
        for (int i = 0; i < numOrders; i++) {
            JPanel order = createOrder();
            order.setBounds(
                0,
                (int)(i * gp.tileSize * 3.5), // Vertical position based on index
                gp.tileSize * 14,
                gp.tileSize * 3
            );
            ordersPanel.add(order);
        }
        
        ordersPanel.revalidate();
        ordersPanel.repaint();
    }
    public JPanel createOrder() {
        JPanel order = new JPanel();
        JButton dealBtn = null;
        JButton denyBtn = null;
        try {
            BufferedImage send = ImageIO.read(new File("res/deal-icon.png"));
            BufferedImage deny = ImageIO.read(new File("res/deny-icon.png"));
            ImageIcon sendIcon = new ImageIcon(send.getScaledInstance(gp.tileSize*2, gp.tileSize, Image.SCALE_SMOOTH));
            ImageIcon denyIcon = new ImageIcon(deny.getScaledInstance(gp.tileSize*2, gp.tileSize, Image.SCALE_SMOOTH));
            dealBtn = new JButton(sendIcon);
            denyBtn = new JButton(denyIcon);
            dealBtn.setFocusPainted(false);
            denyBtn.setFocusPainted(false);
            dealBtn.setContentAreaFilled(false);
            denyBtn.setContentAreaFilled(false);
            dealBtn.setBorderPainted(false);
            denyBtn.setBorderPainted(false);
            order.setBackground(Color.white);
            order.setBorder(BorderFactory.createLineBorder(Color.gray, 5));
            order.setLayout(null);
            order.add(dealBtn);
            order.add(denyBtn);
            dealBtn.setBounds(gp.tileSize * 12 - gp.tileSize/3, gp.tileSize/3, gp.tileSize * 2, gp.tileSize);
            denyBtn.setBounds(gp.tileSize * 12 - gp.tileSize/3, gp.tileSize * 2 - gp.tileSize/3, gp.tileSize * 2, gp.tileSize);
            dealBtn.setActionCommand("DEAL");
            denyBtn.setActionCommand("DENY");
            dealBtn.addActionListener(gp.actionEvent);
            denyBtn.addActionListener(gp.actionEvent);
            return order;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public void deal() {
        System.out.println("Deal button clicked");
    }
    public void deny() {
        System.out.println("Deny button clicked");
    }
}

