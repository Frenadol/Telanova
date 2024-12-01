package com.github.Frenadol.View;

import com.github.Frenadol.App;
import com.github.Frenadol.Dao.ClientDAO;
import com.github.Frenadol.Dao.WorkerDAO;
import com.github.Frenadol.Model.Client;
import com.github.Frenadol.Model.Worker;
import com.github.Frenadol.Security.Security;
import com.github.Frenadol.Utils.SessionManager;
import java.util.prefs.Preferences;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartSessionController {
    private ClientDAO clienteDAO = new ClientDAO();
    private WorkerDAO workerDAO = new WorkerDAO();
    private Security security = new Security();

    @FXML
    private TextField textUsername;

    @FXML
    private PasswordField textPassword;

    @FXML
    private TextField gmailField;

    @FXML
    public void Login() {
        String username = textUsername.getText();
        String password = textPassword.getText();
        Worker worker = workerDAO.build().findByName(username);
        Client client = clienteDAO.build().findByName(username);
        SessionManager sessionManager = SessionManager.getInstance();
        if (worker != null) {
            if (security.checkPassword(password, worker.getPassword())) {
                sessionManager.setCurrentWorker(worker); // Set current worker here
                showAlert("Bienvenido a Telanova", "Bienvenido a Telanova, " + worker.getUsername(), Alert.AlertType.INFORMATION);
                try {
                    App.setRoot("View/AdminPanel");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                showAlert("Error", "Contraseña incorrecta", Alert.AlertType.ERROR);
            }
        } else if (client != null) {
            if (security.checkPassword(password, client.getPassword())) {
                sessionManager.setCurrentClient(client);
                showAlert("Bienvenido a Telanova", "Bienvenido a Telanova, " + client.getUsername(), Alert.AlertType.INFORMATION);
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientMenu.fxml"));
                    Stage stage = (Stage) textUsername.getScene().getWindow();
                    Scene scene = new Scene(loader.load());
                    stage.setScene(scene);
                    stage.show();

                    ClientMenuController clientMenuController = loader.getController();
                    sessionManager.getCurrentClient().setWallet(client.getWallet());
                    clientMenuController.updateWalletBalance();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                showAlert("Error", "Contraseña incorrecta", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Error", "Cliente no encontrado", Alert.AlertType.ERROR);
        }
        saveFields();
    }

    private void loadFields() {
        textUsername.setText(Preferences.userRoot().node("username").get("username", ""));
        textPassword.setText(Preferences.userRoot().node("password").get("password", ""));
        gmailField.setText(Preferences.userRoot().node("gmail").get("gmail", ""));
    }

    private void saveFields() {
        Preferences.userRoot().node("username").put("username", textUsername.getText());
        Preferences.userRoot().node("password").put("password", textPassword.getText());
        Preferences.userRoot().node("gmail").put("gmail", gmailField.getText());
    }

    @FXML
    public void initialize() {
        loadFields();
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public int getClientId() {
        Client currentClient = SessionManager.getInstance().getCurrentClient();
        if (currentClient != null) {
            return currentClient.getId_user();
        } else {
            throw new IllegalStateException("No client is currently logged in.");
        }
    }
}