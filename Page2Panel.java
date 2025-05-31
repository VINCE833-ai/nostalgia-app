package photoboothApp;

import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Page2Panel extends JPanel {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private JLabel cameraLabel;
    private JLabel countdownLabel;
    private List<BufferedImage> capturedImages = new ArrayList<>();
    private JLabel[] previewLabels = new JLabel[3];
    private VideoCapture capture;
    private volatile boolean capturing = false;
    private JPanel previewPanel;
    private Thread cameraThread;

    public Page2Panel() {
        setLayout(null);
        setBackground(new Color(0xFF, 0xF4, 0xC5));

        // === Camera Feed Label (LEFT side) ===
        cameraLabel = new JLabel();
        cameraLabel.setBounds(50, 100, 720, 540);  // larger and positioned to the left
        cameraLabel.setHorizontalAlignment(JLabel.CENTER);
        cameraLabel.setVerticalAlignment(JLabel.CENTER);
        cameraLabel.setLayout(null);
        add(cameraLabel);

        // === Countdown Overlay ===
        countdownLabel = new JLabel("", SwingConstants.CENTER);
        countdownLabel.setBounds(0, 0, 720, 540);
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 100));
        countdownLabel.setForeground(Color.RED);
        countdownLabel.setVisible(false);
        cameraLabel.add(countdownLabel);

        // === Buttons ===
        JButton captureButton = new JButton("Capture");
        captureButton.setBounds(850, 600, 150, 50);
        add(captureButton);

        JButton retakeButton = new JButton("Retake");
        retakeButton.setBounds(1020, 600, 150, 50);
        add(retakeButton);

        JButton editButton = new JButton("Edit Pictures");
        editButton.setBounds(1190, 600, 150, 50);
        add(editButton);

        // === Preview Panel (RIGHT side) ===
        previewPanel = new JPanel(null);
        previewPanel.setBounds(850, 20, 400, 550);
        previewPanel.setOpaque(false);
        for (int i = 0; i < 3; i++) {
            previewLabels[i] = new JLabel();
            previewLabels[i].setBounds(0, i * 180, 300, 160);
            previewPanel.add(previewLabels[i]);
        }
        add(previewPanel);

        // === Button Listeners ===
        captureButton.addActionListener(e -> {
            if (capturing || !capture.isOpened()) return;
            capturing = true;
            capturedImages.clear();
            for (JLabel label : previewLabels) label.setIcon(null);
            runCountdownSequence(0);
        });

        retakeButton.addActionListener(e -> {
            capturedImages.clear();
            for (JLabel label : previewLabels) label.setIcon(null);
        });

        startCamera();
    }

    private void runCountdownSequence(int index) {
        if (index >= 3) {
            capturing = false;
            return;
        }

        countdownLabel.setVisible(true);
        final int[] countdown = {3};

        Timer countdownTimer = new Timer(1000, null);
        countdownTimer.addActionListener(e -> {
            if (countdown[0] > 0) {
                countdownLabel.setText(String.valueOf(countdown[0]));
                countdown[0]--;
            } else {
                countdownTimer.stop();
                countdownLabel.setVisible(false);
                captureImage(index);
                runCountdownSequence(index + 1);
            }
        });

        countdownLabel.setText("3");
        countdownTimer.setInitialDelay(0);
        countdownTimer.start();
    }

    private void captureImage(int index) {
        if (capture.isOpened()) {
            Mat frame = new Mat();
            if (capture.read(frame)) {
                BufferedImage img = matToBufferedImage(frame);
                if (img != null) {
                    capturedImages.add(img);
                    Image scaled = getScaledImagePreservingAspectRatio(img, 300, 160);
                    previewLabels[index].setIcon(new ImageIcon(scaled));
                }
            }
        }
    }

    private void startCamera() {
        if (capture != null && capture.isOpened()) {
            capture.release();
        }

        capture = new VideoCapture(0, Videoio.CAP_DSHOW);
        if (!capture.isOpened()) {
            JOptionPane.showMessageDialog(this, "Unable to open camera. Make sure it's not used by another app.");
            return;
        }

        cameraThread = new Thread(() -> {
            Mat frame = new Mat();
            while (capture.isOpened()) {
                if (capture.read(frame)) {
                    BufferedImage img = matToBufferedImage(frame);
                    if (img != null) {
                        Image scaled = img.getScaledInstance(cameraLabel.getWidth(), cameraLabel.getHeight(), Image.SCALE_SMOOTH);
                        cameraLabel.setIcon(new ImageIcon(scaled));
                    }
                }
                try {
                    Thread.sleep(33);
                } catch (InterruptedException ignored) {
                }
            }
        });
        cameraThread.setDaemon(true);
        cameraThread.start();
    }

    private BufferedImage matToBufferedImage(Mat matrix) {
        Mat flipped = new Mat();
        Core.flip(matrix, flipped, 1);
        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(".jpg", flipped, mob);
        byte[] byteArray = mob.toArray();

        try {
            return javax.imageio.ImageIO.read(new java.io.ByteArrayInputStream(byteArray));
        } catch (Exception e) {
            return null;
        }
    }

    private Image getScaledImagePreservingAspectRatio(BufferedImage srcImg, int maxWidth, int maxHeight) {
        int originalWidth = srcImg.getWidth();
        int originalHeight = srcImg.getHeight();

        double widthRatio = (double) maxWidth / originalWidth;
        double heightRatio = (double) maxHeight / originalHeight;
        double scaleFactor = Math.min(widthRatio, heightRatio);

        int newWidth = (int) (originalWidth * scaleFactor);
        int newHeight = (int) (originalHeight * scaleFactor);

        return srcImg.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    }
}


