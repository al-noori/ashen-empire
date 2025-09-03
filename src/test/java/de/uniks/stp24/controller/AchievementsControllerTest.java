package de.uniks.stp24.controller;

import de.uniks.stp24.ControllerTest;
import de.uniks.stp24.model.Achievement;
import de.uniks.stp24.service.AchievementsService;
import io.reactivex.rxjava3.core.Observable;
import javafx.application.Platform;
import javafx.scene.control.TableView;
import org.fulib.fx.controller.Subscriber;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static de.uniks.stp24.constants.AchievementsConstants.ACHIEVEMENTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

@ExtendWith(MockitoExtension.class) // Use MockitoExtension to Mock
public class AchievementsControllerTest extends ControllerTest {
    @Spy
    Subscriber subscriber = spy(new Subscriber());
    @Spy
    DateTimeFormatter dateTimeFormatter = spy(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
    AchievementsService achievementsService = mock(AchievementsService.class);
    @InjectMocks
    AchievementsController achievementsController;
    @Spy
    ResourceBundle resources = spy(ResourceBundle.getBundle("de/uniks/stp24/lang/main", Locale.ENGLISH));

    @Test
    public void viewDefaultAchievements() {
        List<Achievement> achievements = ACHIEVEMENTS.values().stream().toList();
        when(achievementsService.getAchievementsFromUser()).thenReturn(
                Observable.just(achievements)
        );
        Platform.runLater(() -> app.show(achievementsController, Map.of("backTo", "/lobby")));
        waitForFxEvents();
        assertEquals("Achievements", stage.getTitle());
        TableView table = lookup("#table").queryTableView();
        assertEquals(table.getItems(), achievements);
    }
    @Override
    public void stop() throws Exception {
        Mockito.reset(subscriber, achievementsService, dateTimeFormatter);
        subscriber = null;
        achievementsService = null;
        dateTimeFormatter = null;
        achievementsController = null;
        super.stop();
    }
}
