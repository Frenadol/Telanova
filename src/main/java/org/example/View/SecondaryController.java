package org.example.View;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.example.App;

public class SecondaryController extends Controller implements Initializable {

    @FXML
    private void switchToPrimary() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void onOpen(Object input) throws IOException {

    }
    @FXML
    public void GoPrimari() throws IOException {
        App.currentController.changeScene(Scenes.PRIMARY, null);
    }
}