package com.github.Frenadol.View;

import com.github.Frenadol.Dao.ClientDAO;
import com.github.Frenadol.Model.User;
import com.github.Frenadol.Model.Worker;
import com.github.Frenadol.Utils.ErrorLog;
import com.github.Frenadol.Utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class AdminPanelController {

    @FXML
    public void goToAddClothes() {
        try {
            App.setRoot("AddClothes");
        } catch (IOException e) {
            ErrorLog.fileRead(e);
            showAlert("Error" + e.getMessage());
        }
    }

    private void showAlert(String s) {
    }
}
