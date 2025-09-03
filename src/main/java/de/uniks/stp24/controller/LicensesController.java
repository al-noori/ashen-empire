package de.uniks.stp24.controller;

import de.uniks.stp24.App;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.fulib.fx.annotation.controller.Controller;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.controller.Title;
import org.fulib.fx.annotation.param.Param;

import javax.inject.Inject;
import java.util.Map;
import java.util.ResourceBundle;

@Title("Licenses")
@Controller

public class LicensesController {

    @FXML
    Button backButton;

    @Inject
    App app;

    @Inject
    @Resource
    ResourceBundle resources;

    @Param("username") //get values from login
    String username = "";
    @Param("password")
    String password = "";

    @Inject
    LicensesController() {
    }

    public void onBackButtonClicked() {
        app.show("/login", Map.of("username", username, "password", password));
    }

}
