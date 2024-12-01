package com.github.Frenadol.View;

import com.github.Frenadol.Dao.StorageDAO;
import com.github.Frenadol.Model.Storage;
import com.github.Frenadol.Model.Worker;
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
            closeWindow();
        }
    }


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

    private void closeWindow() {
        Stage stage = (Stage) storageNameField.getScene().getWindow();
        stage.close();
    }
}