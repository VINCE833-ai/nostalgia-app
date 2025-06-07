package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class LayoutButtons extends HBox {

    public LayoutButtons(PhotoStrip photoStrip) {
        // Portrait Mini Strip
        VBox portraitMini = createMiniPhotoStrip(true);
        portraitMini.setOnMouseClicked((MouseEvent e) -> photoStrip.setPortraitLayout());

        // Landscape Mini Strip
        HBox landscapeMini = createMiniPhotoStrip(false);
        landscapeMini.setOnMouseClicked((MouseEvent e) -> photoStrip.setLandscapeLayout());

        this.setSpacing(20);
        this.setAlignment(Pos.CENTER_LEFT);
        this.setPadding(new Insets(10, 0, 0, 0));
        this.getChildren().addAll(portraitMini, landscapeMini);
    }

    // Creates a mini photo strip style VBox or HBox based on orientation
    private <T extends Pane> T createMiniPhotoStrip(boolean isPortrait) {
        T container;
        if (isPortrait) {
            container = (T) new VBox(5); // 5px spacing between slots
        } else {
            container = (T) new HBox(5);
        }

        container.setPadding(new Insets(8));
        container.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(6), Insets.EMPTY)));
        container.setBorder(new Border(new BorderStroke(Color.DARKVIOLET, BorderStrokeStyle.SOLID, new CornerRadii(6), BorderWidths.DEFAULT)));
        container.setCursor(Cursor.HAND);
        container.setMaxHeight(Region.USE_PREF_SIZE);  // ✅ prevent stretching
        container.setMaxWidth(Region.USE_PREF_SIZE);   // ✅ optional to avoid horizontal stretch
        container.setMinHeight(Region.USE_PREF_SIZE);
        container.setMinWidth(Region.USE_PREF_SIZE);

        int slotCount = 3;
        for (int i = 0; i < slotCount; i++) {
            Rectangle rect = new Rectangle(isPortrait ? 20 : 30, isPortrait ? 30 : 20); // mini slot size
            rect.setFill(Color.DARKGRAY);
            rect.setStroke(Color.DARKVIOLET);
            rect.setStrokeWidth(1.5);
            container.getChildren().add(rect);
        }

        return container;
    }
}


