package com.github.Frenadol.View;

import com.github.Frenadol.Dao.StorageDAO;
import com.github.Frenadol.Model.Storage;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InsertStorageController {

    @FXML
    private TextField storageNameField;

    @FXML
    private Label errorMessage;

    private StorageDAO storageDAO = new StorageDAO();

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
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) storageNameField.getScene().getWindow();
        stage.close();
    }
}