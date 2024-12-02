package com.github.Frenadol.View;

import com.github.Frenadol.App;
import com.github.Frenadol.Dao.StorageDAO;
import com.github.Frenadol.Model.Storage;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class InsertStorageController {

    @FXML
    private TextField storageNameField;

    @FXML
    private Label errorMessage;

    private StorageDAO storageDAO = new StorageDAO();

    /**
     * Saves the storage to the database.
     * This method is called when the user clicks the save button.
     * It validates the input, checks for duplicates, and inserts the new storage.
     */
    @FXML
    public void saveStorage() {
        String storageName = storageNameField.getText();
        if (storageName == null || storageName.isEmpty()) {
            errorMessage.setText("Por favor, ingresa el nombre del almacén.");
            return;
        }

        Storage existingStorage = storageDAO.findByName(storageName);
        if (existingStorage != null) {
            errorMessage.setText("El almacén con este nombre ya existe.");
            return;
        }

        Storage storage = new Storage();
        storage.setStorageName(storageName);
        storageDAO.insertStorage(storage);
        showSuccessMessage();
        try {
            App.setRoot("View/AdminPanel");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Shows a success message when the storage is successfully created.
     */
    private void showSuccessMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText("El almacén ha sido creado con éxito.");
        alert.showAndWait();
    }

    /**
     * Closes the current window.
     * This method is called after the storage is successfully saved.
     */
    private void closeWindow() {
        Stage stage = (Stage) storageNameField.getScene().getWindow();
        stage.close();
    }
}