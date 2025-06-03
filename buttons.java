package photoboothApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class buttons {
    private Runnable switchToPage2Callback;
    private File[] importedImages = new File[0]; // store up to 3 images

    public void setSwitchCallback(Runnable callback) {
        this.switchToPage2Callback = callback;
    }

    public JPanel createResponsiveButtonsPanel() {
        JPanel panel = new JPanel(null);

        // === USE CAMERA Button ===
        JButton button1 = new JButton("USE CAMERA");
        button1.setBounds(100, 400, 300, 55); // Moved down
        button1.setBackground(Color.decode("#ffbd59"));
        button1.setForeground(Color.BLACK);
        button1.setFocusPainted(false);
        button1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        button1.addActionListener(e -> {
            if (switchToPage2Callback != null) {
                switchToPage2Callback.run();
            }
        });
        panel.add(button1);

        // === IMPORT PHOTO Button ===
        JButton button2 = new JButton("IMPORT PHOTO");
        button2.setBounds(100, 500, 300, 55); // Moved down
        button2.setBackground(Color.decode("#028747"));
        button2.setForeground(Color.WHITE);
        button2.setFocusPainted(false);
        button2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        button2.addActionListener(this::handleImportPhotos);
        panel.add(button2);

        return panel;
    }

    private void handleImportPhotos(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Image files", "jpg", "jpeg", "png", "bmp"));

        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = chooser.getSelectedFiles();

            if (selectedFiles.length > 3) {
                JOptionPane.showMessageDialog(null, "Please select only up to 3 images.", "Limit Exceeded", JOptionPane.WARNING_MESSAGE);
                return;
            }

            this.importedImages = selectedFiles;

            // For now, just show confirmation
            StringBuilder msg = new StringBuilder("Imported files:\n");
            for (File file : importedImages) {
                msg.append("- ").append(file.getName()).append("\n");
            }
            JOptionPane.showMessageDialog(null, msg.toString(), "Import Successful", JOptionPane.INFORMATION_MESSAGE);

            // TODO: Add functionality to send these images to another page/view
        }
    }

    // Optionally expose imported images for use in other pages
    public File[] getImportedImages() {
        return importedImages;
    }
}

