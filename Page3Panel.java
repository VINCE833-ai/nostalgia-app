package photoboothApp;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Page3Panel extends JPanel {

    private JPanel photoPanel;
    private JLabel[] photoLabels = new JLabel[3];
    private List<BufferedImage> originalImages;
    private List<BufferedImage> filteredImages = new ArrayList<>();
    private boolean isPortrait = true;
    private JPanel controlPanel;
    private Color currentStripColor = Color.WHITE;
    private String stripStyle = "Default";

    public Page3Panel(List<BufferedImage> capturedImages) {
        this.originalImages = capturedImages;
        for (BufferedImage img : capturedImages) {
            filteredImages.add(deepCopy(img));
        }
        setLayout(null);
        setBackground(new Color(0xFFF4C5));
        setupPhotoPanel();
        setupControlPanel();
        setupDownloadButton();
    }

    private void setupPhotoPanel() {
        photoPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        photoPanel.setBackground(currentStripColor);
        photoPanel.setBounds(50, 20, 240, 720);
        photoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(photoPanel);

        for (int i = 0; i < 3; i++) {
            photoLabels[i] = new JLabel();
            photoLabels[i].setHorizontalAlignment(JLabel.CENTER);
            photoLabels[i].setVerticalAlignment(JLabel.CENTER);
            photoLabels[i].setOpaque(true);
            photoLabels[i].setBackground(currentStripColor);
            updateLabelIcon(photoLabels[i], filteredImages.get(i), 220, 230);
            photoPanel.add(photoLabels[i]);
        }
    }

    private void setupDownloadButton() {
        JButton downloadBottomBtn = new JButton("Download Photo Strip");
        downloadBottomBtn.setBounds(300, 700, 200, 40);
        downloadBottomBtn.setBackground(new Color(200, 255, 200));
        downloadBottomBtn.addActionListener(e -> downloadStripImage());
        add(downloadBottomBtn);
    }

    private void downloadStripImage() {
        int width = isPortrait ? 240 : 720;
        int height = isPortrait ? 720 : 240;
        BufferedImage combined = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = combined.createGraphics();
        g.setColor(currentStripColor);
        g.fillRect(0, 0, width, height);

        int padding = 10;
        int imgW = 220;
        int imgH = isPortrait ? 230 : 180;

        for (int i = 0; i < 3; i++) {
            BufferedImage img = filteredImages.get(i);
            int x = isPortrait ? padding : padding + i * (imgW + padding);
            int y = isPortrait ? padding + i * (imgH + padding) : padding;
            g.drawImage(img.getScaledInstance(imgW, imgH, Image.SCALE_SMOOTH), x, y, null);
        }

        g.dispose();

        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Photo Strip");
            fileChooser.setSelectedFile(new File("photostrip.png"));
            int option = fileChooser.showSaveDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                ImageIO.write(combined, "png", file);
                JOptionPane.showMessageDialog(this, "Photo strip saved successfully!");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving image.");
        }
    }
    private void setupControlPanel() {
        controlPanel = new JPanel(null);
        controlPanel.setOpaque(false);
        controlPanel.setBounds(320, 20, 500, 650);
        add(controlPanel);

        int sectionSpacing = 150;

        // === Choose Layout ===
        JLabel layoutLabel = createSectionLabel("Choose Layout", 0, 0);
        controlPanel.add(layoutLabel);

        JButton portraitBtn = createStripButton("Portrait", 0, 40, 90, 60);
        JButton landscapeBtn = createStripButton("Landscape", 100, 40, 120, 40);
        controlPanel.add(portraitBtn);
        controlPanel.add(landscapeBtn);

        portraitBtn.addActionListener(e -> switchToPortrait());
        landscapeBtn.addActionListener(e -> switchToLandscape());

        // === Stickers (dummy buttons) ===
        int stickerY = sectionSpacing;
        JLabel stickersLabel = createSectionLabel("Stickers", 0, stickerY);
        controlPanel.add(stickersLabel);

        for (int i = 0; i < 7; i++) {
            JButton circle = new JButton();
            circle.setBounds(i * 30, stickerY + 40, 20, 20);
            circle.setBackground(Color.PINK);
            circle.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            circle.setFocusPainted(false);
            controlPanel.add(circle);
        }

        JButton moreSticker = new JButton("More");
        moreSticker.setBounds(7 * 30 + 10, stickerY + 36, 50, 26);
        controlPanel.add(moreSticker);

        // === Photo Strip Color Buttons ===
        int stripY = stickerY + sectionSpacing;
        JLabel stripLabel = createSectionLabel("Photo Strip Color", 0, stripY);
        controlPanel.add(stripLabel);

        Color[] stripColors = new Color[] {
            Color.WHITE, new Color(0xFFF4C5), Color.LIGHT_GRAY, new Color(0xFFDDC1), new Color(0xC1FFD7), new Color(0xC1D7FF), new Color(0xF0C1FF)
        };

        for (int i = 0; i < stripColors.length; i++) {
            JButton colorBtn = new JButton();
            colorBtn.setBounds(i * 35, stripY + 40, 30, 30);
            colorBtn.setBackground(stripColors[i]);
            colorBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            int idx = i;
            colorBtn.addActionListener(e -> {
                currentStripColor = stripColors[idx];
                updateStripColor();
            });
            controlPanel.add(colorBtn);
        }

        // === Filters ===
        int filtersY = stripY + sectionSpacing;
        JLabel filtersLabel = createSectionLabel("Filters", 0, filtersY);
        controlPanel.add(filtersLabel);

        for (int i = 0; i < 6; i++) {
            JButton filterBtn = new JButton("F" + (i + 1));
            filterBtn.setBounds((i % 3) * 70, filtersY + 40 + (i / 3) * 50, 65, 40);
            filterBtn.setBackground(Color.WHITE);
            filterBtn.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            int filterIndex = i;
            filterBtn.addActionListener(e -> applyFilter(filterIndex));
            controlPanel.add(filterBtn);
        }
    }

    private void updateStripColor() {
        photoPanel.setBackground(currentStripColor);
        for (JLabel label : photoLabels) {
            label.setBackground(currentStripColor);
        }
        repaint();
    }

    private void applyFilter(int filterIndex) {
        for (int i = 0; i < filteredImages.size(); i++) {
            filteredImages.set(i, filterImage(originalImages.get(i), filterIndex));
            updateLabelIcon(photoLabels[i], filteredImages.get(i), 220, isPortrait ? 230 : 180);
        }
        repaint();
    }

    private BufferedImage filterImage(BufferedImage src, int filterIndex) {
        BufferedImage filtered = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < src.getHeight(); y++) {
            for (int x = 0; x < src.getWidth(); x++) {
                int rgba = src.getRGB(x, y);
                Color c = new Color(rgba, true);
                Color nc = c;
                switch (filterIndex) {
                    case 0: // F1: Grayscale
                        int gray = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
                        nc = new Color(gray, gray, gray, c.getAlpha());
                        break;
                    case 1: // F2: Sepia
                        int tr = (int)(0.393 * c.getRed() + 0.769 * c.getGreen() + 0.189 * c.getBlue());
                        int tg = (int)(0.349 * c.getRed() + 0.686 * c.getGreen() + 0.168 * c.getBlue());
                        int tb = (int)(0.272 * c.getRed() + 0.534 * c.getGreen() + 0.131 * c.getBlue());
                        nc = new Color(
                            Math.min(255, tr),
                            Math.min(255, tg),
                            Math.min(255, tb),
                            c.getAlpha());
                        break;
                    case 2: // F3: Invert
                        nc = new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue(), c.getAlpha());
                        break;
                    case 3: // F4: Red Tint
                        nc = new Color(c.getRed(), 0, 0, c.getAlpha());
                        break;
                    case 4: // F5: Green Tint
                        nc = new Color(0, c.getGreen(), 0, c.getAlpha());
                        break;
                    case 5: // F6: Blue Tint
                        nc = new Color(0, 0, c.getBlue(), c.getAlpha());
                        break;
                    default:
                        nc = c;
                }
                filtered.setRGB(x, y, nc.getRGB());
            }
        }
        return filtered;
    }

    private void switchToPortrait() {
        isPortrait = true;
        photoPanel.removeAll();
        photoPanel.setLayout(new GridLayout(3, 1, 10, 10));
        photoPanel.setBounds(50, 20, 240, 720);
        for (int i = 0; i < 3; i++) {
            updateLabelIcon(photoLabels[i], filteredImages.get(i), 220, 230);
            photoPanel.add(photoLabels[i]);
        }
        controlPanel.setBounds(320, 20, 500, 650);
        updateStripColor();
        revalidate();
        repaint();
    }

    private void switchToLandscape() {
        isPortrait = false;
        photoPanel.removeAll();
        photoPanel.setLayout(new GridLayout(1, 3, 10, 10));
        photoPanel.setBounds(50, 20, 720, 240);
        for (int i = 0; i < 3; i++) {
            updateLabelIcon(photoLabels[i], filteredImages.get(i), 220, 180);
            photoPanel.add(photoLabels[i]);
        }
        controlPanel.setBounds(50, 280, 800, 650);
        updateStripColor();
        revalidate();
        repaint();
    }

    private void updateLabelIcon(JLabel label, BufferedImage image, int width, int height) {
        if (image != null) {
            ImageIcon icon = new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH));
            label.setIcon(icon);
        }
    }

    private static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    private JLabel createSectionLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 200, 30);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        return label;
    }

    private JButton createStripButton(String text, int x, int y, int w, int h) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, w, h);
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return btn;
    }
}
