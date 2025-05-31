package photoboothApp;

import javax.swing.*;
import java.awt.*;

public class page1 {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Photo Booth");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(frame);

        // === Use CardLayout to switch pages ===
        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);

        // === Page 1: Background + Buttons panel ===
        buttons buttonsObj = new buttons();
        JPanel buttonPanel = buttonsObj.createResponsiveButtonsPanel();
        buttonPanel.setOpaque(false); // Let background show through

        // Create container for page 1 and set background
        JPanel page1Panel = new JPanel(null);
        page1Panel.setBackground(new Color(0xFF, 0xF4, 0xC5)); // #fff4c5
        page1Panel.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        buttonPanel.setBounds(0, 0, page1Panel.getPreferredSize().width, page1Panel.getPreferredSize().height);
        page1Panel.add(buttonPanel);

        // === Page 2 ===
        Page2Panel page2 = new Page2Panel();

        // Add both pages to card panel
        cardPanel.add(page1Panel, "page1");
        cardPanel.add(page2, "page2");

        // Hook the button to switch to page2
        buttonsObj.setSwitchCallback(() -> {
            cardLayout.show(cardPanel, "page2");
        });

        // Set card panel as content
        frame.setContentPane(cardPanel);
        frame.setVisible(true);
    }
}
