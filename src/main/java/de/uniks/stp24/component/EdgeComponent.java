package de.uniks.stp24.component;

import de.uniks.stp24.model.TechnologieNode;
import javafx.scene.shape.Line;
import org.fulib.fx.annotation.controller.Component;

@Component
public class EdgeComponent extends Line {

    TechnologieNode parent;
    TechnologieNode child;

    EdgeComponent(TechnologieNode parent, TechnologieNode child) {
        super();
        this.parent = parent;
        this.child = child;
        this.setStrokeWidth(20);
        this.setStartX(parent.getX() + 175);
        this.setStartY(parent.getY() + 175);
        this.setEndX(child.getX() + 175);
        this.setEndY(child.getY()+ 175);
    }
}
