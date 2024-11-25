package com.github.Frenadol.View;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class PrimaryController {
    @FXML
    private Button registerButton;
    @FXML
    private Button sessionButton;

    @FXML
    private void switchToRegister() throws IOException {
        App.setRoot("RegisterUser");
    }

    @FXML
    private void switchToStartSession() throws IOException {
        App.setRoot("StartSession");
    }
}
