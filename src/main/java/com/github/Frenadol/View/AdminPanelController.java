package com.github.Frenadol.View;

import com.github.Frenadol.App;
import com.github.Frenadol.Utils.ErrorLog;
import javafx.fxml.FXML;

import java.io.IOException;
import java.security.PublicKey;

public class AdminPanelController {

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
    private void showAlert(String s) {
    }
}
