package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage primaryStage;

    // Static block to load OpenCV native library
    static {
        try {
            System.load("c:/vince/Downloads/opencv/build/java/x86/opencv_java480.dll");
            System.out.println("OpenCV native library loaded successfully.");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Failed to load OpenCV native library: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        VBox mainContainer = new VBox();
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setSpacing(40);
        mainContainer.setPadding(new Insets(50));
        mainContainer.setStyle("-fx-background-color: #FFF4C5;");

        Text titleText = new Text("Nostalgia");
        titleText.setFont(Font.font("Brush Script MT", FontWeight.NORMAL, 48));
        titleText.setFill(Color.web("#8B4513"));

        HBox contentContainer = new HBox();
        contentContainer.setAlignment(Pos.CENTER);
        contentContainer.setSpacing(50);

        VBox buttonContainer = new VBox();
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setSpacing(20);

        Button useCameraBtn = new Button("📷 USE CAMERA");
        useCameraBtn.setPrefWidth(200);
        useCameraBtn.setPrefHeight(50);
        useCameraBtn.setStyle("-fx-background-color: #FF8C42; -fx-text-fill: white; -fx-background-radius: 25; -fx-font-size: 14px; -fx-font-weight: bold;");
        useCameraBtn.setOnAction(e -> {
            CameraSessionPage cameraPage = new CameraSessionPage();
            try {
                Stage cameraStage = new Stage();
                Scene cameraScene = cameraPage.createScene(cameraStage);
                cameraStage.setTitle("Camera Session");
                cameraStage.setScene(cameraScene);
                cameraStage.show();

                primaryStage.hide(); // Hide main window when camera opens
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("Error opening camera: " + ex.getMessage());
            }
        });

        Button importPhotosBtn = new Button("📁 IMPORT PHOTOS");
        importPhotosBtn.setPrefWidth(200);
        importPhotosBtn.setPrefHeight(50);
        importPhotosBtn.setStyle("-fx-background-color: #2ECC71; -fx-text-fill: white; -fx-background-radius: 25; -fx-font-size: 14px; -fx-font-weight: bold;");
        importPhotosBtn.setOnAction(e -> {
            System.out.println("Import Photos - Not implemented yet");
        });

        buttonContainer.getChildren().addAll(useCameraBtn, importPhotosBtn);

        ImageView mainPhoto = new ImageView();
        try {
            Image image = new Image(getClass().getResourceAsStream("/com/example/nostalgiaapp/Images Background/Page 1 Background.png"));
            mainPhoto.setImage(image);
            mainPhoto.setFitWidth(444);
            mainPhoto.setFitHeight(357);
            mainPhoto.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Could not load background image: " + e.getMessage());
            mainPhoto.setFitWidth(444);
            mainPhoto.setFitHeight(357);
            mainPhoto.setStyle("-fx-background-color: #E8E8E8; -fx-border-color: #CCCCCC; -fx-border-width: 2;");
        }

        contentContainer.getChildren().addAll(buttonContainer, mainPhoto);
        mainContainer.getChildren().addAll(titleText, contentContainer);

        Scene scene = new Scene(mainContainer, 1000, 700);
        primaryStage.setTitle("Nostalgia Photo Booth");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public void showMainWindow() {
        primaryStage.show();
        primaryStage.toFront();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


