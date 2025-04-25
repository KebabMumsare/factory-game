package Main;
import javax.swing.*;
public class Start {
    public static void main(String[] args) {
        JFrame window = new JFrame("Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //window.setResizable(false);
        window.setTitle("Factory Game");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        gamePanel.startGameThread();
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}