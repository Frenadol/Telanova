package com.github.Frenadol.View;

import com.github.Frenadol.App;
import com.github.Frenadol.DataBase.ConnectionH2;
import com.github.Frenadol.Dao.ClientDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

import java.io.IOException;

public class InitialMenuController {
    @FXML
    private Button registerButton;
    @FXML
    private Button sessionButton;
    @FXML
    private CheckBox useSQLlite;

    /**
     * Switches the view to the Register User screen.
     * This method is called when the register button is clicked.
     * @throws IOException if the view cannot be loaded.
     */
    @FXML
    private void switchToRegister() throws IOException {
        App.setRoot("View/RegisterUser");
    }

    @FXML
    private void initialize() {
        useSQLlite.setOnAction(event -> {
            ClientDAO.useH2 = useSQLlite.isSelected();
            if (useSQLlite.isSelected()) {
                ConnectionH2.loadDB();
            }
        });
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