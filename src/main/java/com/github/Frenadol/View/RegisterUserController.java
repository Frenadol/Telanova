package com.github.Frenadol.View;

import com.github.Frenadol.App;
import com.github.Frenadol.Dao.ClientDAO;
import com.github.Frenadol.Model.Client;
import com.github.Frenadol.Security.Security;
import com.github.Frenadol.Utils.ErrorLog;
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
import java.util.regex.Pattern;

public class RegisterUserController {

    @FXML
    private TextField textUsername;

    @FXML
    private PasswordField textPassword;

    @FXML
    private TextField textEmail;
    @FXML
    private ImageView perfilImage;
    private File imageFile;


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

        if (imageFile != null) {
            try {
                byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
                newClient.setProfilePicture(imageBytes);
            } catch (IOException e) {
                ErrorLog.fileRead(e);
                ErrorLog.logMessage("Error al leer la imagen: " + e.getMessage());
                showAlert("Error al leer la imagen. Intente nuevamente.");
                return;
            }
        }

        try {
            newClient.setPassword(Security.hashPassword(pass));
        } catch (NoSuchAlgorithmException e) {
            ErrorLog.fileRead(e);
            ErrorLog.logMessage("Error al hashear la contraseña: " + e.getMessage());
            showAlert("Error al hashear la contraseña. Intente nuevamente.");
            return;
        }

        try {
            clientDAO.insertClient(newClient);
        } catch (Exception e) {
            ErrorLog.fileRead(e);
            ErrorLog.logMessage("Error al insertar el usuario: " + e.getMessage());
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
        App.setRoot("View/InitialMenu");
    }


    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }
    @FXML
    private void selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        Stage stage = (Stage) perfilImage.getScene().getWindow();
        imageFile = fileChooser.showOpenDialog(stage);
        if (imageFile != null) {
            try {
                InputStream is = new FileInputStream(imageFile);
                Image image = new Image(is);
                perfilImage.setImage(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


}
