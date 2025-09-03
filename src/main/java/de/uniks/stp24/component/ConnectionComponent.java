package de.uniks.stp24.component;

import de.uniks.stp24.model.game.Empire;
import de.uniks.stp24.model.game.Fraction;
import de.uniks.stp24.model.game.Jobs;
import de.uniks.stp24.service.GameService;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;
import java.util.List;

@Component
public class ConnectionComponent extends Line {
    Fraction fraction1;
    Fraction fraction2;
    @Inject
    Subscriber subscriber;
    @Inject
    public ConnectionComponent() {
        super();
    }

    public void setFractions(Fraction fraction1, Fraction fraction2) {
        this.fraction1 = fraction1;
        this.fraction2 = fraction2;
        this.setStartX(fraction1.getX());
        this.setStartY(fraction1.getY());
        this.setEndX(fraction2.getX());
        this.setEndY(fraction2.getY());
        subscriber.listen(fraction1.listeners(), Fraction.PROPERTY_CURRENT_TRAVELING, evt -> {
            handleTravelers();
        });
        subscriber.listen(fraction2.listeners(), Fraction.PROPERTY_CURRENT_TRAVELING, evt -> {
            handleTravelers();
        });
        handleTravelers();
    }

    private void handleTravelers() {
        boolean hasJob = false;
        for(Jobs job : fraction1.getCurrentTraveling()) {
            if(job.getPath().contains(fraction2)) {
                hasJob = true;
                break;
            }
        }
        if(hasJob) {
            this.setStroke(Color.valueOf("#00fdbd"));
        } else {
            this.setStroke(Color.BLACK);
        }
    }

    @OnDestroy
    void onDestroy() {
        this.fraction1 = null;
        this.fraction2 = null;
        subscriber.dispose();
    }
}
