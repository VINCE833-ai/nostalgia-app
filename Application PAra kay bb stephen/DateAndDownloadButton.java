package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DateAndDownloadButton extends VBox {

    public DateAndDownloadButton(PhotoStrip photoStrip) {
        super(10);

        ToggleButton switchButton = new ToggleButton();
        switchButton.setPrefSize(40, 20);
        switchButton.setStyle(
            "-fx-background-radius: 20; " +
            "-fx-background-color: #ccc; " +
            "-fx-cursor: hand;"
        );

        // When toggled, show/hide date on the PhotoStrip
        switchButton.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                switchButton.setStyle(
                    "-fx-background-radius: 20; " +
                    "-fx-background-color: #4CAF50; " +
                    "-fx-cursor: hand;"
                );
                photoStrip.setDateVisible(true);
            } else {
                switchButton.setStyle(
                    "-fx-background-radius: 20; " +
                    "-fx-background-color: #ccc; " +
                    "-fx-cursor: hand;"
                );
                photoStrip.setDateVisible(false);
            }
        });

        Label switchLabel = new Label("Set Date");
        switchLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        HBox switchContainer = new HBox(10, switchButton, switchLabel);
        switchContainer.setAlignment(Pos.CENTER_LEFT);

        Button downloadButton = new Button("Download");
        downloadButton.setPrefSize(120, 40);
        downloadButton.setStyle(
            "-fx-background-radius: 15; " +
            "-fx-background-color: #4CAF50; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-cursor: hand;"
        );

        HBox mainBox = new HBox(20, switchContainer, downloadButton);
        mainBox.setAlignment(Pos.CENTER_LEFT);
        mainBox.setPadding(new Insets(10));

        this.getChildren().add(mainBox);
        this.setAlignment(Pos.TOP_LEFT);
    }
}

