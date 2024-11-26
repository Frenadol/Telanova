package com.github.Frenadol.View;

import com.github.Frenadol.Dao.ClientDAO;
import com.github.Frenadol.Model.Client;
import com.github.Frenadol.Model.User;
import com.github.Frenadol.Security.Security;
import com.github.Frenadol.Utils.ErrorLog;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class RegisterUserController {

    @FXML
    private TextField textUsername;

    @FXML
    private PasswordField textPassword;

    @FXML
    private TextField textEmail;

    @FXML
    private AnchorPane anchorPane;

    private ClientDAO clientDAO= new ClientDAO();

    /**
     * Este método se utiliza para registrar un nuevo usuario.
     * Valida los campos obligatorios (nombre de usuario, contraseña y correo), comprueba si el usuario ya existe,
     * hashea la contraseña, crea un objeto User, lo configura y lo inserta en la base de datos.
     */
    public void registerUser() {
        String username = textUsername.getText();
        String pass = textPassword.getText();
        String email = textEmail.getText();

        if (username.isEmpty() || pass.isEmpty() || email.isEmpty()) {
            String message = "Por favor, complete todos los campos.";
            showAlert(message);
            ErrorLog.logMessage(message);
            return;
        }

        if (!isValidEmail(email)) {
            String message = "El correo electrónico no tiene un formato válido.";
            showAlert(message);
            ErrorLog.logMessage(message);
            return;
        }

        Client existingClient = clientDAO.findByName(username);
        if (existingClient != null) {
            String message = "El nombre de usuario ya está en uso. Por favor, elija otro.";
            showAlert(message);
            ErrorLog.logMessage(message);
            return;
        }

        Client newClient = new Client();
        newClient.setUsername(username);
        newClient.setGmail(email);
        try {
            newClient.setPassword(Security.hashPassword(pass));
        } catch (NoSuchAlgorithmException e) {
            ErrorLog.fileRead(e);
            ErrorLog.logMessage("Error al hashear la contraseña: " + e.getMessage());
            showAlert("Error al hashear la contraseña.");
            return;
        }

        try {
            clientDAO.insertClient(newClient);
        } catch (Exception e) {
            ErrorLog.fileRead(e);
            ErrorLog.logMessage("Error al insertar el usuario hay un error aqui: " + e.getMessage());
            System.out.println(e.getMessage());
            return;
        }

        String message = "Usuario registrado con éxito!";
        showAlert(message);
        ErrorLog.logMessage(message);
    }


    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@[\\w-\\.]+\\.[a-z]{2,}$";
        return Pattern.matches(emailRegex, email);
    }


    @FXML
    public void goToInitialMenu() throws IOException {
        App.setRoot("initialMenu");
    }


    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    private void onClose() {
        System.exit(0);
    }
}
