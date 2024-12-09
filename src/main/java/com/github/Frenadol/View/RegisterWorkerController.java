package com.github.Frenadol.View;

import com.github.Frenadol.App;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class RegisterWorkerController {

    @FXML
    private TextField textUsername;

    @FXML
    private PasswordField textPassword;

    @FXML
    private TextField textEmail;

    @FXML
    private ImageView imageView;

    private File imageFile;

    private final WorkerDAO workerDAO = new WorkerDAO();

    /**
     * Registers a new worker.
     * Validates the input fields, checks for existing workers, and saves the new worker.
     */
    public void registerWorker() {
        String username = textUsername.getText().trim();
        String pass = textPassword.getText().trim();
        String email = textEmail.getText().trim();

        if (username.isEmpty() || pass.isEmpty() || email.isEmpty() || imageFile == null) {
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

        if (username.length() < 3 || pass.length() < 6) {
            String message = "El nombre de usuario debe tener al menos 3 caracteres y la contraseña al menos 6 caracteres.";
            showAlert(message);
            ErrorLog.logMessage(message);
            return;
        }

        Worker existingWorkerByName = workerDAO.findByName(username);
        Worker existingWorkerByEmail = workerDAO.findByEmail(email);
        if (existingWorkerByName != null) {
            String message = "El nombre de usuario ya está en uso. Por favor, elija otro.";
            showAlert(message);
            ErrorLog.logMessage(message);
            return;
        }
        if (existingWorkerByEmail != null) {
            String message = "El correo electrónico ya está en uso. Por favor, elija otro.";
            showAlert(message);
            ErrorLog.logMessage(message);
            return;
        }

        Worker newWorker = new Worker();
        newWorker.setUsername(username);
        newWorker.setGmail(email);
        newWorker.setHireDate(String.valueOf(UtilDate.getCurrentDate()));
        newWorker.setWorker(true);

        try {
            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
            newWorker.setProfilePicture(imageBytes);
        } catch (IOException e) {
            ErrorLog.fileRead(e);
            ErrorLog.logMessage("Error al leer la imagen: " + e.getMessage());
            showAlert("Error al leer la imagen. Intente nuevamente.");
            return;
        }

        try {
            newWorker.setPassword(Security.hashPassword(pass));
        } catch (NoSuchAlgorithmException e) {
            ErrorLog.fileRead(e);
            ErrorLog.logMessage("Error al hashear la contraseña: " + e.getMessage());
            showAlert("Error al hashear la contraseña. Intente nuevamente.");
            return;
        }

        try {
            workerDAO.insertWorker(newWorker);
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry for email")) {
                showAlert("El correo electrónico ya está en uso. Por favor, elija otro.");
            } else {
                ErrorLog.fileRead(e);
                ErrorLog.logMessage("Error al insertar el trabajador: " + e.getMessage());
                showAlert("No se pudo registrar al trabajador. Intente nuevamente.");
            }
            return;
        }

        String message = "Trabajador registrado con éxito!";
        showAlert(message);
        ErrorLog.logMessage(message);
    }

    /**
     * Validates the email format.
     * @param email The email to validate.
     * @return true if the email format is valid, false otherwise.
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,7}$";
        return Pattern.matches(emailRegex, email);
    }

    /**
     * Shows an alert with the given message.
     * @param message The message to display in the alert.
     */
    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }

    /**
     * Go to AdminPanel again.
     */
    @FXML
    private void onClose() {
        try {
            App.setRoot("View/AdminPanel");
        } catch (IOException e) {
            showAlert("Error al volver al panel de administración: " + e.getMessage());
        }
    }

    /**
     * Opens a file chooser to select an image and sets it to the profile image view.
     */
    @FXML
    private void selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        Stage stage = (Stage) imageView.getScene().getWindow();
        imageFile = fileChooser.showOpenDialog(stage);
        if (imageFile != null) {
            try {
                InputStream is = new FileInputStream(imageFile);
                Image image = new Image(is);
                imageView.setImage(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}