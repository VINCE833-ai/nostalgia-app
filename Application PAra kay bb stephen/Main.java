package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #fff4c5;");

        BorderPane content = new BorderPane();

        // Create photo strip (left side)
        PhotoStrip photoStrip = new PhotoStrip();

        // Create layout header + buttons group
        HeaderTexts headerLayout = new HeaderTexts();
        LayoutButtons layoutButtons = new LayoutButtons(photoStrip);
        VBox layoutGroup = new VBox(10, headerLayout, layoutButtons);
        layoutGroup.setAlignment(Pos.TOP_LEFT);
        layoutGroup.setPadding(new Insets(0, 0, 0, 10)); // small left padding
        layoutGroup.setTranslateY(-30); // move up a bit

        // Create stickers header + buttons group
        StickerHeadersButton stickerGroup = new StickerHeadersButton();

        // Create color changer group
        PhotoStrip PhotoStrip = new PhotoStrip();
        PhotoStripColorChanger colorChangerGroup = new PhotoStripColorChanger(photoStrip);

        // Create filters group
        Filters filtersGroup = new Filters(photoStrip);

        // VBox for Stickers, Color Changer, and Filters
        VBox stickerAndColorGroup = new VBox(10); // spacing between sections
        stickerAndColorGroup.getChildren().addAll(stickerGroup, colorChangerGroup, filtersGroup);

        // Create the new Date and Download button group
        DateAndDownloadButton dateDownloadButtons = new DateAndDownloadButton(photoStrip);

        // VBox to hold sticker/color/filter group and bottom buttons with spacing
        VBox bottomSpacerGroup = new VBox(30);
        bottomSpacerGroup.getChildren().addAll(stickerAndColorGroup, dateDownloadButtons);
        bottomSpacerGroup.setAlignment(Pos.TOP_LEFT);

        // Combine layoutGroup and all right-side groups vertically
        VBox rightSideGroup = new VBox(30);  // space between layout and right side groups
        rightSideGroup.getChildren().addAll(layoutGroup, bottomSpacerGroup);
        rightSideGroup.setAlignment(Pos.TOP_LEFT);
        rightSideGroup.setPadding(new Insets(0, 0, 0, 10)); // same left padding

        // HBox to hold the photoStrip and the rightSideGroup side by side
        HBox leftBox = new HBox(30); // space between photoStrip and right side groups
        leftBox.setPadding(new Insets(20));
        leftBox.setAlignment(Pos.CENTER_LEFT);
        leftBox.getChildren().addAll(photoStrip, rightSideGroup);

        // Set HBox on the left of the BorderPane
        content.setLeft(leftBox);
        BorderPane.setMargin(leftBox, new Insets(20, 0, 20, 20));

        root.getChildren().add(content);

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("Nostalgia");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
