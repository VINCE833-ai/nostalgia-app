package application;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class HeaderTexts extends StackPane {

    private double mouseAnchorX;
    private double mouseAnchorY;

    public HeaderTexts() {
        Label headerLabel = new Label("Choose Layout");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 27));
        headerLabel.setTextFill(Color.BLACK);

        this.setAlignment(Pos.CENTER_LEFT);
        this.setPrefHeight(60);
        this.getChildren().add(headerLabel);

        // Enable dragging
        headerLabel.setOnMousePressed((MouseEvent event) -> {
            mouseAnchorX = event.getSceneX() - headerLabel.getLayoutX();
            mouseAnchorY = event.getSceneY() - headerLabel.getLayoutY();
        });

        headerLabel.setOnMouseDragged((MouseEvent event) -> {
            headerLabel.setLayoutX(event.getSceneX() - mouseAnchorX);
            headerLabel.setLayoutY(event.getSceneY() - mouseAnchorY);
        });
    }
}

