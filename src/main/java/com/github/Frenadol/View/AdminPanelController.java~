package com.github.Frenadol.View;

import com.github.Frenadol.App;
import com.github.Frenadol.Utils.ErrorLog;
import javafx.fxml.FXML;

import java.io.IOException;

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

    private void showAlert(String s) {
    }
}
