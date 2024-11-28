package com.github.jcapitanmoreno.view;

import com.github.jcapitanmoreno.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AppController extends Controller implements Initializable {

    @FXML
    private BorderPane borderPane;
    private Controller centerController;

    /**
     * This method is called when the scene is opened.
     * It changes the scene to the USERSNAME scene.
     * @param input An object representing any input data required for the scene (not used in this case).
     * @throws IOException if there is an error while changing the scene.
     */
    @Override
    public void onOpen(Object input) throws IOException {
        changeScene(Scenes.USERSNAME, null);
    }

    @Override
    public void onClose(Object output) {

    }

    @Override
    public void informationAlert(String text1, String text2, String text3) {

    }

    @Override
    public void errorAlert(String text1, String text2, String text3) {

    }

    @Override
    public void warningAlert(String text1, String text2, String text3) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    /**
     * Opens a modal dialog window with the specified scene.
     *
     * @param scene  The scene to be displayed in the modal window.
     * @param title  The title of the modal window.
     * @param parent The controller of the parent scene.
     * @param data   Any data that needs to be passed to the modal scene.
     * @throws IOException if there is an error while loading the scene.
     */
    public void openModal(Scenes scene, String title,Controller parent, Object data) throws IOException {
        View view = loadFXML(scene);
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(App.stage);
        Scene _scene = new Scene(view.scene);
        stage.setScene(_scene);
        view.controller.onOpen(parent);
        stage.showAndWait();
    }
    /**
     * Loads the FXML file corresponding to the specified scene and initializes its controller.
     *
     * @param scenes The scene enum representing the FXML file to load.
     * @return A View object containing the loaded scene and its associated controller.
     * @throws IOException if an error occurs while loading the FXML file.
     */
        public static View loadFXML (Scenes scenes) throws IOException {
            String url = scenes.getURL();
            FXMLLoader loader = new FXMLLoader(App.class.getResource(url));
            Parent p = loader.load();
            Controller c = loader.getController();
            View view = new View();
            view.scene = p;
            view.controller = c;
            return view;
        }
    /**
     * Changes the scene displayed in the center of the border pane to the specified scene.
     *
     * @param scene The scene enum representing the FXML file to load.
     * @param data  Any additional data to be passed to the controller of the new scene.
     * @throws IOException if an error occurs while loading the FXML file.
     */
        public void changeScene (Scenes scene, Object data) throws IOException {
            View view = loadFXML(scene);
            borderPane.setCenter(view.scene);
            this.centerController = view.controller;
            this.centerController.onOpen(data);
        }
    }
