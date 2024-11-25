package com.github.Frenadol.View;

import com.github.Frenadol.Dao.UserDAO;
import com.github.Frenadol.Model.Session;
import com.github.Frenadol.Model.User;
import com.github.Frenadol.Security.Security;
import com.github.Frenadol.Utils.ErrorLog;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;

import java.awt.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class StartSession {
    @FXML
    private TextField textUsername;
    @FXML
    private PasswordField textPassword;
    @FXML
    private TextField usernameGmail;

    private Button BackButton;

    private UserDAO userDAO = new UserDAO();
    Security security;

    @FXML
    public void Login() {
        try {
            String username = textUsername.getText();
            String pass = textPassword.getText();
            String gmail = usernameGmail.getText();

            User userLogin = userDAO.findByName(username);

            if (userLogin != null) {
                if (security.checkPassword(pass, userLogin.getPassword()) && username.equals(userLogin.getUsername())) {
                    Session.getInstance().logIn(userLogin);
                    App.setRoot("mainMenu");
                } else {
                    String message = "Contraseña invalida, introduzca una contraseña valida";
                    showAlert(message);
                    ErrorLog.logMessage(message);
                }
            } else {
                String message = "Usuario incorrecto, introduzca bien el nombre de usuario";
                ErrorLog.fileRead(new IOException());
                showAlert(message);
                ErrorLog.logMessage(message);
            }
        } catch (IOException e) {
            ErrorLog.fileRead(e);
            ErrorLog.logMessage("Error al iniciar sesion: " + e.getMessage());
        }
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }


}
