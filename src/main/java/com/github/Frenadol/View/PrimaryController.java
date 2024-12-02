package com.github.Frenadol.View;

import com.github.Frenadol.App;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class PrimaryController {
    @FXML
    private Button registerButton;
    @FXML
    private Button sessionButton;

    /**
     * Switches the view to the Register User screen.
     * This method is called when the register button is clicked.
     * @throws IOException if the view cannot be loaded.
     */
    @FXML
    private void switchToRegister() throws IOException {
        App.setRoot("View/RegisterUser");

    }

    /**
     * Switches the view to the Start Session screen.
     * This method is called when the session button is clicked.
     * @throws IOException if the view cannot be loaded.
     */
    @FXML
    private void switchToStartSession() throws IOException {
        App.setRoot("View/StartSession");
    }
}