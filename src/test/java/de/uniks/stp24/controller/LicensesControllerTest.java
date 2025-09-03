package de.uniks.stp24.controller;

import de.uniks.stp24.ControllerTest;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Locale;
import java.util.ResourceBundle;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

@ExtendWith(MockitoExtension.class)
public class LicensesControllerTest extends ControllerTest {

    @Spy
    ResourceBundle resources = spy(ResourceBundle.getBundle("de/uniks/stp24/lang/main", Locale.ENGLISH));

    @InjectMocks
    LicensesController licensesController;

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
        app.show(licensesController);
    }

    @Test
    void onBackButtonClicked() {
        //Title: A user wants to go back to the login screen
        //Start: A user is in the Licenses window.
        //waitForFxEvents();
        assertEquals("Licenses", stage.getTitle());
        waitForFxEvents();
        //Action: He clicks on the back button.
        clickOn("#backButton");
        waitForFxEvents();
        //Result: The user is now back in the login window.
        assertEquals("Log in", stage.getTitle());
    }
    @Override
    public void stop() throws Exception {
        Mockito.reset(resources);
        resources = null;
        licensesController = null;
        super.stop();
    }
}
