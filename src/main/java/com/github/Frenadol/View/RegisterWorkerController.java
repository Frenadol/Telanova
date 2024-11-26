package com.github.Frenadol.View;

import com.github.Frenadol.Dao.WorkerDAO;
import com.github.Frenadol.Model.Worker;
import com.github.Frenadol.Security.Security;
import com.github.Frenadol.Utils.ErrorLog;
import com.github.Frenadol.Utils.UtilDate;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class RegisterWorkerController {

    @FXML
    private TextField textUsername;

    @FXML
    private PasswordField textPassword;

    @FXML
    private TextField textEmail;

    @FXML
    private AnchorPane anchorPane;

    private final WorkerDAO workerDAO = new WorkerDAO();

    public void registerWorker() {
        String username = textUsername.getText().trim();
        String pass = textPassword.getText().trim();
        String email = textEmail.getText().trim();

        // Validar campos vacíos
        if (username.isEmpty() || pass.isEmpty() || email.isEmpty()) {
            String message = "Por favor, complete todos los campos.";
            showAlert(AlertType.WARNING, message);
            ErrorLog.logMessage(message);
            return;
        }

        if (!isValidEmail(email)) {
            String message = "El correo electrónico no tiene un formato válido.";
            showAlert(AlertType.WARNING, message);
            ErrorLog.logMessage(message);
            return;
        }

        // Validar longitud del nombre de usuario y contraseña
        if (username.length() < 3 || pass.length() < 6) {
            String message = "El nombre de usuario debe tener al menos 3 caracteres y la contraseña al menos 6 caracteres.";
            showAlert(AlertType.WARNING, message);
            ErrorLog.logMessage(message);
            return;
        }

        // Verificar si el usuario ya existe
        Worker existingWorker = workerDAO.findByName(username);
        if (existingWorker != null) {
            String message = "El nombre de usuario ya está en uso. Por favor, elija otro.";
            showAlert(AlertType.WARNING, message);
            ErrorLog.logMessage(message);
            return;
        }

        // Crear y configurar nuevo trabajador
        Worker newWorker = new Worker();
        newWorker.setUsername(username);
        newWorker.setGmail(email);
        newWorker.setHireDate(String.valueOf(UtilDate.getCurrentDate()));
        newWorker.setWorker(true);

        try {
            newWorker.setPassword(Security.hashPassword(pass));
        } catch (NoSuchAlgorithmException e) {
            ErrorLog.fileRead(e);
            ErrorLog.logMessage("Error al hashear la contraseña: " + e.getMessage());
            showAlert(AlertType.ERROR, "Error al procesar la contraseña. Intente nuevamente.");
            return;
        }

        // Intentar insertar al nuevo trabajador en la base de datos
        try {
            workerDAO.insertWorker(newWorker);
        } catch (Exception e) {
            ErrorLog.fileRead(e);
            ErrorLog.logMessage("Error al insertar el trabajador: " + e.getMessage());
            showAlert(AlertType.ERROR, "No se pudo registrar al trabajador. Intente nuevamente.");
            return;
        }

        // Confirmación exitosa
        String message = "Trabajador registrado con éxito!";
        showAlert(AlertType.INFORMATION, message);
        ErrorLog.logMessage(message);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,7}$";
        return Pattern.matches(emailRegex, email);
    }

    private void showAlert(AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.setTitle(type == AlertType.INFORMATION ? "Éxito" : "Advertencia");
        alert.show();
    }

    @FXML
    private void onClose() {
        System.exit(0);
    }
}
