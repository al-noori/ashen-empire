package de.uniks.stp24;

import de.uniks.stp24.service.PrefService;
import javafx.stage.Stage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.internal.JavaVersionAdapter;

import java.util.Locale;
import java.util.concurrent.TimeoutException;

import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class) // Use MockitoExtension to Mock
public class ControllerTest extends ApplicationTest {

    @Spy
    public App app = spy(new App());
    @Spy
    public PrefService prefService = spy(PrefService.class);
    protected Stage stage;

    @Override
    public void init() throws TimeoutException {
        FxToolkit.registerStage(Stage::new);
    }

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
        this.stage = stage;
        stage.setX(0);
        stage.setY(0);
        app.start(stage);
        stage.requestFocus();
        prefService.setRefreshToken("");
        prefService.setLocale(Locale.ENGLISH);
    }

    @Override
    public void stop() throws Exception {
        app.autoRefresher().close();
        FxToolkit.cleanupStages();
        stage.eventDispatcherProperty().unbind();
        stage.setEventDispatcher(null);
        stage.hide();
        stage.close();
        super.stop();
        JavaVersionAdapter.getWindows().clear();
        Mockito.reset(prefService, app);
        Mockito.framework().clearInlineMocks();
        app.stopWithoutLeavingGame();
        app = null;
        stage = null;
        Mockito.clearAllCaches();
        FxToolkit.hideStage();
        Mockito.clearAllCaches();
        System.gc();
    }
}

