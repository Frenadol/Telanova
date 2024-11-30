package com.github.Frenadol.View;

import com.github.Frenadol.App;
import com.github.Frenadol.Model.Worker;
import com.github.Frenadol.Utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class StoragePanelController {
    private Worker currentWorker;

    @FXML
    private ImageView workerImage;
    @FXML
    private Button ViewClothesButton;

    @FXML
    public void initialize() {
        currentWorker = SessionManager.getInstance().getCurrentWorker();

        if (currentWorker != null) {
            System.out.println("Worker logueado: " + currentWorker.getUsername());
            byte[] visualData = currentWorker.getProfilePicture();

            if (visualData != null && visualData.length > 0) {
                System.out.println("Tamaño del array de bytes: " + visualData.length);
                ByteArrayInputStream bis = new ByteArrayInputStream(visualData);
                Image image = new Image(bis);
                workerImage.setImage(image);
            } else {
                System.out.println("La variable currentWorker es null");
            }
        }


    }

    @FXML
    public void ViewClothes() {
        try {
            App.setRoot("View/Clothes");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}