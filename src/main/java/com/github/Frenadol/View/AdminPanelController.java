package com.github.Frenadol.View;

import com.github.Frenadol.App;
import com.github.Frenadol.Model.Worker;
import com.github.Frenadol.Utils.ErrorLog;
import com.github.Frenadol.Utils.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminPanelController implements Initializable {

    @FXML
    private ImageView workerImageView;

    @FXML
    public void goToAddClothes() {
        try {
            App.setRoot("View/AddClothes");
        } catch (IOException e) {
            ErrorLog.fileRead(e);
            showAlert("Error" + e.getMessage());
        }
    }

    @FXML
    public void gotoAddWorker() {
        try {
            App.setRoot("View/RegisterWorker");
        } catch (IOException e) {
            ErrorLog.fileRead(e);
            showAlert("Error" + e.getMessage());
        }
    }


    @FXML
    public void openInsertStorageWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InsertStorage.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Insertar Almacén");
            stage.show();
        } catch (IOException e) {
            ErrorLog.fileRead(e);
            showAlert("Error" + e.getMessage());
        }
    }

    private void showAlert(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(s);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("initialize method called");
        SessionManager sessionManager = SessionManager.getInstance();
        Worker currentWorker = sessionManager.getCurrentWorker();
        if (currentWorker != null) {
            byte[] imageBytes = currentWorker.getProfilePicture();
            if (imageBytes != null && imageBytes.length > 0) {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
                Image image = new Image(inputStream);
                workerImageView.setImage(image);
            }
        } else {
            System.out.println("Current worker is null");
        }
    }
    @FXML
    public void gotoStorage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StorageLogin.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Ingresar al Almacén");
            stage.show();
        } catch (IOException e) {
            ErrorLog.fileRead(e);
            showAlert("Error" + e.getMessage());
        }
    }
}