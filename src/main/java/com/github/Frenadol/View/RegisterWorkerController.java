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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
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

        // Verificar si el correo ya existe
        Worker existingEmailWorker = workerDAO.findByEmail(email);
        if (existingEmailWorker != null) {
            String message = "El correo electrónico ya está en uso. Por favor, elija otro.";
            showAlert(AlertType.WARNING, message);
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
            showAlert(AlertType.ERROR, "Error al leer la imagen. Intente nuevamente.");
            return;
        }
        try {
            newWorker.setPassword(Security.hashPassword(pass));
        } catch (NoSuchAlgorithmException e) {
            ErrorLog.fileRead(e);
            ErrorLog.logMessage("Error al hashear la contraseña: " + e.getMessage());
            showAlert(AlertType.ERROR, "Error al procesar la contraseña. Intente nuevamente.");
            return;
        }

        try {
            workerDAO.insertWorker(newWorker);
        } catch (Exception e) {
            ErrorLog.fileRead(e);
            ErrorLog.logMessage("Error al insertar el trabajador: " + e.getMessage());
            showAlert(AlertType.ERROR, "No se pudo registrar al trabajador. Intente nuevamente.");
            return;
        }

        String message = "Trabajador registrado con éxito!";
        showAlert(AlertType.INFORMATION, message);
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
     * @param type The type of alert.
     * @param message The message to display in the alert.
     */
    private void showAlert(AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.setTitle(type == AlertType.INFORMATION ? "Éxito" : "Advertencia");
        alert.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void onClose() {
        System.exit(0);
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