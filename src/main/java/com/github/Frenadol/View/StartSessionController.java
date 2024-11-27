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
    private ClientDAO clienteDAO= new ClientDAO();
    private WorkerDAO workerDAO= new WorkerDAO();
    private Security security=new Security();
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
                    sessionManager.setCurrentUser(workerByGmail);
                    try {
                        App.setRoot("AdminPanel");
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
                        App.setRoot("ClientMenu");
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
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }
}