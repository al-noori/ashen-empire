package de.uniks.stp24;

import de.uniks.stp24.dagger.DaggerMainComponent;
import de.uniks.stp24.dagger.MainComponent;
import de.uniks.stp24.service.GameService;
import de.uniks.stp24.service.TimerService;
import fr.brouillard.oss.cssfx.CSSFX;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import okhttp3.OkHttpClient;
import org.fulib.fx.FulibFxApp;

import javax.imageio.ImageIO;
import java.awt.*;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static javafx.scene.input.KeyEvent.KEY_PRESSED;

public class App extends FulibFxApp {
    private MainComponent component;
    private Runnable cssStop;
    private final boolean startCss;

    public App() {
        this(true);
    }

    public App(boolean startCss) {
        super();
        this.startCss = startCss;
        this.component = DaggerMainComponent.builder().mainApp(this).build();
    }

    // package-private - only for testing
    void setComponent(MainComponent component) {
        this.component = component;
    }

    @Override
    public void start(Stage primaryStage) {
        RxJavaPlugins.setErrorHandler(throwable -> LOGGER.log(Level.SEVERE, "An error occurred in RxJava: " + throwable.getMessage(), throwable));

        try {

            super.start(primaryStage);

            registerRoutes(component.routes());

            stage().addEventHandler(KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.F5) {
                    this.refresh();
                }
            });
            if (startCss) {
                primaryStage.getScene().getStylesheets().add(Objects.requireNonNull(App.class.getResource("styles.css")).toExternalForm());
                cssStop = CSSFX.start(primaryStage);
            }

            // Set default window size
            primaryStage.setWidth(1200);
            primaryStage.setHeight(800);

            // Set minimal window size
            primaryStage.setMinWidth(720);
            primaryStage.setMinHeight(480);

            setAppIcon(primaryStage);
            setTaskbarIcon();

            autoRefresher().setup(Path.of("src/main/resources/de/uniks/stp24"));
            Parameters params = getParameters();

            List<String> rawParams = (params != null) ? params.getRaw() : List.of();
            if (rawParams.size() >= 2){
                component.gameService().getGame().setId(rawParams.get(1));
                if (component.loginService().autoLogin(rawParams.get(0))){
                    show("/gameOverview");
                } else {
                    show("/login");
                }
            }
            else if (component.loginService().autoLogin()) {
                component.timerService().setupTimer();
                show("/lobby");
            } else {
                show("/login");
            }

            component.clientChangeService().loadClients();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An error occurred while starting the application: " + e.getMessage(), e);
        }
    }


    private void setAppIcon(Stage stage) {
        final Image image = new Image(Objects.requireNonNull(App.class.getResource("image/icon.png")).toString());
        stage.getIcons().add(image);
    }

    private void setTaskbarIcon() {
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }

        try {
            final Taskbar taskbar = Taskbar.getTaskbar();
            final java.awt.Image image = ImageIO.read(Objects.requireNonNull(App.class.getResource("image/icon.png")));
            taskbar.setIconImage(image);
        } catch (Exception ignored) {
        }
    }

    public MainComponent component() {
        return component;
    }

    @Override
    public void stop() {
        stopWithoutLeavingGame();
    }

    public void stopWithoutLeavingGame() {
        super.stop();
        if (cssStop != null) {
            cssStop.run();
        }
        final OkHttpClient client = component.okHttpClient();
        if (client != null) {
            if (client.connectionPool() != null) {
                client.connectionPool().evictAll();
            }
            if (client.dispatcher() != null) {
                client.dispatcher().cancelAll();
                client.dispatcher().executorService().close();
            }
        }
        final TimerService timerService = component.timerService();
        if (timerService != null) {
            timerService.stopTimer();
        }
    }
}
