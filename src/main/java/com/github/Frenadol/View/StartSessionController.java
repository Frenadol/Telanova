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
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
        String gmail = gmailField.getText();
        Worker worker = workerDAO.build().findByName(username);
        Client client = clienteDAO.build().findByName(username);
        SessionManager sessionManager = SessionManager.getInstance();
        if (worker != null) {
            Worker workerByGmail = workerDAO.build().findByGmail(gmail);
            if (workerByGmail != null) {
                if (security.checkPassword(password, workerByGmail.getPassword())) {
                    sessionManager.setCurrentWorker(workerByGmail); // Set current worker here
                    try {
                        App.setRoot("View/AdminPanel");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Contraseña incorrecta");
                }
            } else {
                System.out.println("Gmail incorrecto");
            }
        } else if (client != null) {
            Client clientByGmail = clienteDAO.build().findByGmail(gmail);
            if (clientByGmail != null) {
                if (security.checkPassword(password, clientByGmail.getPassword())) {
                    sessionManager.setCurrentUser(clientByGmail);
                    try {
                        App.setRoot("View/ClientMenu");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Contraseña incorrecta");
                }
            } else {
                System.out.println("Gmail incorrecto");
            }
        } else {
            System.out.println("Cliente no encontrado");
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

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }
}