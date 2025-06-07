package application;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PhotoStrip extends VBox {

    private boolean isPortrait = true;
    private final List<VBox> photoSlots = new ArrayList<>();
    private Label dateLabel; // Shared date label
    private final String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));

    public PhotoStrip() {
        super();
        setPortraitLayout(); // Start with portrait layout
    }

    public void setPortraitLayout() {
        isPortrait = true;
        this.getChildren().clear();
        photoSlots.clear();

        this.setSpacing(20);
        this.setPadding(new Insets(30, 20, 10, 20));
        this.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(12), Insets.EMPTY)));
        this.setStyle("-fx-border-color: #cccccc; -fx-border-width: 3px;");
        this.setPrefWidth(250);
        this.setMaxWidth(250);
        this.setMaxHeight(Region.USE_COMPUTED_SIZE);

        VBox slot1 = createPhotoSlot("Photo 1", 200, 200);
        VBox slot2 = createPhotoSlot("Photo 2", 200, 200);
        VBox slot3 = createPhotoSlot("Photo 3", 200, 200);

        this.getChildren().addAll(slot1, slot2, slot3);

        dateLabel = new Label(todayDate);
        dateLabel.setTextFill(Color.GRAY);
        dateLabel.setStyle("-fx-font-size: 12px;");
        VBox.setMargin(dateLabel, new Insets(10, 50, 0, 0));
        dateLabel.setVisible(false); // Hidden by default
        this.getChildren().add(dateLabel);
    }

    public void setLandscapeLayout() {
        isPortrait = false;
        this.getChildren().clear();
        photoSlots.clear();

        this.setSpacing(10);
        this.setPadding(new Insets(30, 20, 20, 20));
        this.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(12), Insets.EMPTY)));
        this.setStyle("-fx-border-color: #cccccc; -fx-border-width: 3px;");
        this.setPrefWidth(700);
        this.setMaxWidth(700);
        this.setMaxHeight(250);

        HBox photoRow = new HBox(20);
        photoRow.setPadding(new Insets(0));
        photoRow.setPrefHeight(150);

        VBox slot1 = createPhotoSlot("Photo 1", 200, 150);
        VBox slot2 = createPhotoSlot("Photo 2", 200, 150);
        VBox slot3 = createPhotoSlot("Photo 3", 200, 150);

        photoRow.getChildren().addAll(slot1, slot2, slot3);
        this.getChildren().add(photoRow);

        dateLabel = new Label(todayDate);
        dateLabel.setTextFill(Color.GRAY);
        dateLabel.setStyle("-fx-font-size: 12px;");
        VBox.setMargin(dateLabel, new Insets(10, 0, 0, 0));
        dateLabel.setVisible(false); // Hidden by default
        this.getChildren().add(dateLabel);
    }

    private VBox createPhotoSlot(String labelText, double width, double height) {
        VBox photoSlot = new VBox();
        photoSlot.setPrefSize(width, height);
        photoSlot.setStyle(
            "-fx-background-color: black; " +
            "-fx-border-color: #8B4CB8; " +
            "-fx-border-width: 3px; " +
            "-fx-alignment: center;"
        );
        photoSlot.getStyleClass().add("photo-slot");

        Label label = new Label(labelText);
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-font-size: 12px;");
        photoSlot.getChildren().add(label);

        photoSlots.add(photoSlot);
        return photoSlot;
    }

    public void setEffectToPhotos(ColorAdjust effect) {
        for (VBox slot : photoSlots) {
            slot.setEffect(effect);
        }
    }

    public void setDateVisible(boolean visible) {
        if (dateLabel != null) {
            dateLabel.setVisible(visible);
        }
    }

    public void setDateText(String dateText) {
        if (dateLabel != null) {
            dateLabel.setText(dateText);
        }
    }

    public boolean isPortrait() {
        return isPortrait;
    }

    public List<VBox> getPhotoSlots() {
        return photoSlots;
    }
}
