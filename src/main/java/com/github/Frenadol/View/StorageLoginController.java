package com.github.Frenadol.View;

import com.github.Frenadol.App;
import com.github.Frenadol.Dao.StorageDAO;
import com.github.Frenadol.Model.Storage;
import com.github.Frenadol.Utils.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class StorageLoginController {

    @FXML
    private TextField storageNameField;

    @FXML
    private Label errorMessage;

    private StorageDAO storageDAO = new StorageDAO();

    /**
     * Handles the process of entering a storage.
     * Validates the storage name, sets the current storage in the session, and loads the storage panel.
     */
    @FXML
    public void enterStorage() {
        String storageName = storageNameField.getText();
        if (storageName == null || storageName.isEmpty()) {
            errorMessage.setText("Por favor, ingresa el nombre del almacén.");
            return;
        }

        Storage storage = storageDAO.findByName(storageName);
        if (storage == null) {
            errorMessage.setText("El almacén no existe.");
        } else {
            SessionManager.getInstance().setCurrentStorage(storage);
            showStoragePanel();
            try {
                App.setRoot("View/StoragePanel");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Loads and displays the storage panel.
     */
    private void showStoragePanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StoragePanel.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Panel de Almacén");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the current window.
     */
    private void closeWindow() {
        Stage stage = (Stage) storageNameField.getScene().getWindow();
        stage.close();
    }
}