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
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        page1Panel.setPreferredSize(screenSize);

        // === Header: Nostalgia title ===
        JLabel titleLabel = new JLabel("Nostalgia");
        titleLabel.setFont(new Font("Lucida Handwriting", Font.PLAIN, 48));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBounds(40, 30, 600, 60);
        page1Panel.add(titleLabel);

        // === Add centered and enlarged image ===
        try {
            ImageIcon originalIcon = new ImageIcon(page1.class.getResource("/images/imageshii.png"));
            Image originalImage = originalIcon.getImage();

            int maxWidth = 800;
            int maxHeight = 650;
            int originalWidth = originalIcon.getIconWidth();
            int originalHeight = originalIcon.getIconHeight();
            double widthRatio = (double) maxWidth / originalWidth;
            double heightRatio = (double) maxHeight / originalHeight;
            double scale = Math.min(widthRatio, heightRatio);
            int newWidth = (int) (originalWidth * scale);
            int newHeight = (int) (originalHeight * scale);

            Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
            int x = (screenSize.width - newWidth) / 2;
            int y = (screenSize.height - newHeight) / 3;
            imageLabel.setBounds(x, y, newWidth, newHeight);

            page1Panel.add(imageLabel);
        } catch (Exception e) {
            System.out.println("❌ Failed to load image: " + e.getMessage());
        }

        // === Add buttons panel ===
        buttonPanel.setBounds(0, 0, screenSize.width, screenSize.height);
        page1Panel.add(buttonPanel);

        // === Page 2 ===
        Page2Panel page2 = new Page2Panel(cardLayout, cardPanel);
        cardPanel.add(page2, "page2");
        
        cardPanel.add(page1Panel, "page1");
        cardPanel.add(page2, "page2");

        buttonsObj.setSwitchCallback(() -> {
            cardLayout.show(cardPanel, "page2");
        });

        frame.setContentPane(cardPanel);
        frame.setVisible(true);
    }
}

