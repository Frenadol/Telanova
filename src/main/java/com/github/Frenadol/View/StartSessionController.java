package com.github.Frenadol.View;

import com.github.Frenadol.Dao.ClientDAO;
import com.github.Frenadol.Dao.WorkerDAO;
import com.github.Frenadol.Model.Client;
import com.github.Frenadol.Model.Session;
import com.github.Frenadol.Model.User;
import com.github.Frenadol.Model.Worker;
import com.github.Frenadol.Security.Security;
import com.github.Frenadol.Utils.ErrorLog;
import com.github.Frenadol.Utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.io.IOException;

public class StartSessionController {
    private ClientDAO clienteDAO;
    private WorkerDAO workerDAO;
    private Security security;
    @FXML
    private TextField textUsername;

    @FXML
    private PasswordField textPassword;

    @FXML
    public void Login() {
        String username = textUsername.getText();
        String password = textPassword.getText();
        Worker worker = workerDAO.findByName(username);
        Client client = clienteDAO.findByName(username);
        SessionManager sessionManager = SessionManager.getInstance();

        if (worker != null) {
            if (security.checkPassword(password, worker.getPassword())) {
                sessionManager.setCurrentUser(worker);
                try {
                    App.setRoot("AdminPanel");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Contraseña incorrecta");
            }
        } else if (client != null) {
            sessionManager.setCurrentUser(client);
            if (security.checkPassword(password, client.getPassword())) {
               try{
                   App.setRoot("ClientMenu");
               } catch (IOException e) {
                   e.printStackTrace();
               }
            }
        } else {
            System.out.println("Cliente no encontrado");
        }
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }
    public StartSessionController() {
        this.clienteDAO = new ClientDAO();
        this.workerDAO = new WorkerDAO();
        this.security = new Security();
    }

}