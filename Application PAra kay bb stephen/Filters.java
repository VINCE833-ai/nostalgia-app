package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Filters extends VBox {

    private double mouseAnchorX;
    private double mouseAnchorY;
    private final PhotoStrip photoStripInstance;
    private final Button[] buttons = new Button[6];

    public Filters(PhotoStrip photoStrip) {
        super(15);
        this.photoStripInstance = photoStrip;

        Label headerLabel = new Label("Filters");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 27));
        headerLabel.setTextFill(Color.BLACK);
        headerLabel.setAlignment(Pos.CENTER_LEFT);

        // Optional: draggable header
        headerLabel.setOnMousePressed((MouseEvent event) -> {
            mouseAnchorX = event.getSceneX() - headerLabel.getLayoutX();
            mouseAnchorY = event.getSceneY() - headerLabel.getLayoutY();
        });

        headerLabel.setOnMouseDragged((MouseEvent event) -> {
            headerLabel.setLayoutX(event.getSceneX() - mouseAnchorX);
            headerLabel.setLayoutY(event.getSceneY() - mouseAnchorY);
        });

        // Define filter effects
        ColorAdjust[] effects = new ColorAdjust[]{
            new ColorAdjust(0, 0, 0, 0),                 // No Filter
            new ColorAdjust(0.1, 0, 0.2, 0),             // Bright
            new ColorAdjust(-0.1, 0.1, -0.2, 0.1),       // Vintage
            new ColorAdjust(0.5, 0.3, -0.4, 0.2),        // Pop
            new ColorAdjust(0, -1.0, -0.5, 0),           // Grayscale
            new ColorAdjust(0.6, 0.6, -0.3, 0.3)         // Warm
        };

        // Corresponding colors for buttons (approximate representation)
        String[] buttonColors = new String[]{
            "#D3D3D3", // No Filter (gray)
            "#FFF176", // Bright (yellow)
            "#C48B9F", // Vintage (muted pink)
            "#FF7043", // Pop (orange)
            "#9E9E9E", // Grayscale (gray)
            "#FFD180"  // Warm (soft orange)
        };

        for (int i = 0; i < 6; i++) {
            Button button = new Button();
            button.setPrefSize(60, 35);
            final int index = i;

            button.setStyle(
                "-fx-background-color: " + buttonColors[i] + ";" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;"
            );

            // Filter apply action
            button.setOnAction(e -> {
                // Reset all button styles
                for (int j = 0; j < buttons.length; j++) {
                    buttons[j].setStyle(
                        "-fx-background-color: " + buttonColors[j] + ";" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;"
                    );
                }

                // Highlight selected button with a border
                button.setStyle(
                    "-fx-background-color: " + buttonColors[index] + ";" +
                    "-fx-border-color: black;" +
                    "-fx-border-width: 2;" +
                    "-fx-background-radius: 8;" +
                    "-fx-cursor: hand;"
                );

                // Apply filter effect
                photoStripInstance.setEffectToPhotos(effects[index]);
            });

            buttons[i] = button;
        }

        // Arrange buttons into 2 rows
        HBox row1 = new HBox(15, buttons[0], buttons[1], buttons[2]);
        row1.setAlignment(Pos.CENTER_LEFT);

        HBox row2 = new HBox(15, buttons[3], buttons[4], buttons[5]);
        row2.setAlignment(Pos.CENTER_LEFT);

        this.setAlignment(Pos.TOP_LEFT);
        this.setPadding(new Insets(10));
        this.getChildren().addAll(headerLabel, row1, row2);
    }
}
