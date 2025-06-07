package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class StickerHeadersButton extends VBox {

    private double mouseAnchorX;
    private double mouseAnchorY;

    public StickerHeadersButton() {
        super(15); // spacing between header and buttons

        // Header label - "Stickers"
        Label headerLabel = new Label("Stickers");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 27));
        headerLabel.setTextFill(Color.BLACK);
        headerLabel.setAlignment(Pos.CENTER);

        // Enable dragging of the label
        headerLabel.setOnMousePressed((MouseEvent event) -> {
            mouseAnchorX = event.getSceneX() - headerLabel.getLayoutX();
            mouseAnchorY = event.getSceneY() - headerLabel.getLayoutY();
        });

        headerLabel.setOnMouseDragged((MouseEvent event) -> {
            headerLabel.setLayoutX(event.getSceneX() - mouseAnchorX);
            headerLabel.setLayoutY(event.getSceneY() - mouseAnchorY);
        });

        // Create 7 circular buttons, smaller size
        Button[] buttons = new Button[7];
        for (int i = 0; i < 7; i++) {
            buttons[i] = new Button();
            buttons[i].setPrefSize(30, 30);  // smaller circle size
            buttons[i].setStyle(
                "-fx-background-radius: 20; " +   // half of width/height
                "-fx-background-color: #8B4CB8;" +
                "-fx-cursor: hand;"
            );
        }

        // First row with 4 buttons
        HBox row1 = new HBox(15);
        row1.setAlignment(Pos.CENTER_LEFT);
        row1.getChildren().addAll(buttons[0], buttons[1], buttons[2], buttons[3]);

        // Second row with 3 buttons
        HBox row2 = new HBox(15);
        row2.setAlignment(Pos.CENTER_LEFT);
        row2.getChildren().addAll(buttons[4], buttons[5], buttons[6]);

        // Align the entire VBox top-left and add padding
        this.setAlignment(Pos.TOP_LEFT);
        this.setPadding(new Insets(10));

        // Add header and button rows to the VBox
        this.getChildren().addAll(headerLabel, row1, row2);
    }
}
