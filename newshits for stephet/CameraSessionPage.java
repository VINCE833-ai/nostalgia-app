package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CameraSessionPage {

    private static final int MAX_PHOTOS = 3;
    private static final int COUNTDOWN_SECONDS = 3;

    private VideoCapture camera;
    private ImageView cameraView;
    private List<Image> capturedImages = new ArrayList<>();
    private HBox thumbnailBox;
    private Button editButton;
    private Label countdownLabel;
    private boolean capturing = false;

    // Use ScheduledExecutorService for background frame grabbing
    private ScheduledExecutorService timer;

    public Scene createScene(Stage primaryStage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        cameraView = new ImageView();
        cameraView.setFitWidth(600);
        cameraView.setFitHeight(400);
        cameraView.setPreserveRatio(true);

        thumbnailBox = new HBox(10);
        thumbnailBox.setAlignment(Pos.CENTER);

        countdownLabel = new Label();
        countdownLabel.setStyle("-fx-font-size: 48px; -fx-text-fill: red;");
        countdownLabel.setVisible(false);

        Button captureButton = new Button("Capture");
        captureButton.setOnAction(e -> {
            if (!capturing && capturedImages.size() < MAX_PHOTOS) {
                capturing = true;
                startCountdownAndCapture();
            }
        });

        editButton = new Button("Edit Pictures");
        editButton.setDisable(true);
        editButton.setOnAction(e -> {
            stopCamera();
            System.out.println("Edit Pictures clicked - implement transition");
            // TODO: Transition to edit page here
        });

        Button retakeButton = new Button("Retake");
        retakeButton.setOnAction(e -> {
            capturedImages.clear();
            thumbnailBox.getChildren().clear();
            editButton.setDisable(true);
        });

        HBox buttonBox = new HBox(20, captureButton, editButton, retakeButton);
        buttonBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(cameraView, countdownLabel, thumbnailBox, buttonBox);

        startCamera();

        return new Scene(root, 800, 700);
    }

    private void startCamera() {
        try {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            System.out.println("OpenCV loaded");
        } catch (Exception e) {
            System.out.println("Failed to load OpenCV: " + e.getMessage());
        }

        camera = new VideoCapture(0);
        if (!camera.isOpened()) {
            System.out.println("Failed to open the camera.");
            return;
        }

        // Use ScheduledExecutorService for smoother background frame grabbing
        timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(() -> {
            if (camera != null && camera.isOpened()) {
                Mat frame = new Mat();
                camera.read(frame);
                if (!frame.empty()) {
                    Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2RGB);
                    Image imageToShow = mat2Image(frame);
                    if (imageToShow != null) {
                        Platform.runLater(() -> cameraView.setImage(imageToShow));
                    }
                }
            }
        }, 0, 33, TimeUnit.MILLISECONDS); // approx 30 FPS
    }

    private void stopCamera() {
        if (timer != null && !timer.isShutdown()) {
            timer.shutdown();
            try {
                timer.awaitTermination(33, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (camera != null && camera.isOpened()) {
            camera.release();
        }
    }

    private void startCountdownAndCapture() {
        countdownLabel.setVisible(true);
        Timeline countdown = new Timeline();
        for (int i = COUNTDOWN_SECONDS; i >= 0; i--) {
            int count = i;
            countdown.getKeyFrames().add(new KeyFrame(Duration.seconds(COUNTDOWN_SECONDS - i), e -> {
                if (count > 0) {
                    countdownLabel.setText(String.valueOf(count));
                } else {
                    countdownLabel.setVisible(false);
                    capturePhoto();
                }
            }));
        }
        countdown.setOnFinished(e -> capturing = false);
        countdown.play();
    }

    private void capturePhoto() {
        if (camera != null && camera.isOpened()) {
            Mat frame = new Mat();
            camera.read(frame);
            if (!frame.empty()) {
                Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2RGB);
                Image capturedImage = mat2Image(frame);
                capturedImages.add(capturedImage);
                addThumbnail(capturedImage);

                if (capturedImages.size() == MAX_PHOTOS) {
                    editButton.setDisable(false);
                }
            }
        }
    }

    private void addThumbnail(Image image) {
        ImageView thumb = new ImageView(image);
        thumb.setFitWidth(100);
        thumb.setFitHeight(75);
        thumbnailBox.getChildren().add(thumb);
    }

    private Image mat2Image(Mat mat) {
        try {
            MatOfByte buffer = new MatOfByte();
            Imgcodecs.imencode(".png", mat, buffer);
            return new Image(new ByteArrayInputStream(buffer.toArray()));
        } catch (Exception e) {
            System.out.println("Error converting Mat to Image: " + e.getMessage());
            return null;
        }
    }
}

