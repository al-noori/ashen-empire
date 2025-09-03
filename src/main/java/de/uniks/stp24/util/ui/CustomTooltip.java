package de.uniks.stp24.util.ui;

import javafx.scene.control.Tooltip;
import javafx.scene.layout.Region;
import javafx.stage.Popup;
import javafx.util.Duration;

public class CustomTooltip extends Tooltip {
    private final Popup popup;

    public CustomTooltip(Region content) {
        super();
        super.setStyle("""
                -fx-background-color: transparent;
                -fx-border-color: transparent;
                -fx-text-fill: transparent;
                -fx-padding: 0;
                -fx-background-insets: 0;
                -fx-background-radius: 0;
                """);

        content.getStyleClass().add("tooltip-round");

        this.popup = new Popup();
        this.popup.getContent().add(content);
        popup.setAutoHide(true);

        this.setShowDelay(Duration.seconds(0.5));  // Beispiel für eine Verzögerung
        this.setHideDelay(Duration.seconds(0.2));  // Beispiel für eine Verzögerung

        this.setOnShown(e -> this.popup.show(getOwnerWindow(), getAnchorX(), getAnchorY() + 10));

        this.setOnHiding(e -> this.popup.hide());
    }

}