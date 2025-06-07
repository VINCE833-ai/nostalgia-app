package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PhotoStripColorChanger extends VBox {

    private double mouseAnchorX;
    private double mouseAnchorY;

    public PhotoStripColorChanger(PhotoStrip photoStrip) {
        super(15); // spacing between header and button rows

        // Header Label - "Strip Color"
        Label headerLabel = new Label("Strip Color");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 27));
        headerLabel.setTextFill(Color.BLACK);
        headerLabel.setAlignment(Pos.CENTER_LEFT);

        // Optional drag
        headerLabel.setOnMousePressed((MouseEvent event) -> {
            mouseAnchorX = event.getSceneX() - headerLabel.getLayoutX();
            mouseAnchorY = event.getSceneY() - headerLabel.getLayoutY();
        });

        headerLabel.setOnMouseDragged((MouseEvent event) -> {
            headerLabel.setLayoutX(event.getSceneX() - mouseAnchorX);
            headerLabel.setLayoutY(event.getSceneY() - mouseAnchorY);
        });

        // Define colors
        Color[] colors = {
            Color.web("#8B4CB8"), // Purple
            Color.web("#FF6F61"), // Coral
            Color.web("#6BCB77"), // Green
            Color.web("#FFD93D"), // Yellow
            Color.web("#00BFFF"), // Blue
            Color.web("#FF69B4"), // Pink
            Color.WHITE           // White
        };

        Button[] buttons = new Button[7];
        for (int i = 0; i < 7; i++) {
            Color color = colors[i];
            buttons[i] = new Button();
            buttons[i].setPrefSize(40, 40);
            buttons[i].setStyle(
                "-fx-background-radius: 20; " +
                "-fx-background-color: " + toHex(color) + "; " +
                "-fx-cursor: hand;"
            );

            int index = i;
            buttons[i].setOnAction(e -> {
                photoStrip.setBackground(new Background(new BackgroundFill(color, new CornerRadii(12), Insets.EMPTY)));
            });
        }

        // First row with 4 buttons
        HBox row1 = new HBox(15);
        row1.setAlignment(Pos.CENTER_LEFT);
        row1.getChildren().addAll(buttons[0], buttons[1], buttons[2], buttons[3]);

        // Second row with 3 buttons
        HBox row2 = new HBox(15);
        row2.setAlignment(Pos.CENTER_LEFT);
        row2.getChildren().addAll(buttons[4], buttons[5], buttons[6]);

        // Layout for the entire panel
        this.setAlignment(Pos.TOP_LEFT);
        this.setPadding(new Insets(10));
        this.getChildren().addAll(headerLabel, row1, row2);
    }

    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
            (int)(color.getRed() * 255),
            (int)(color.getGreen() * 255),
            (int)(color.getBlue() * 255)
        );
    }
}

