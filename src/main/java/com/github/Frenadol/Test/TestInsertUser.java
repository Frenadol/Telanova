package com.github.Frenadol.Test;

import com.github.Frenadol.Dao.ClientDAO;
import com.github.Frenadol.Model.Client;
import com.github.Frenadol.Model.User;

public class TestInsertUser {
    public static void main(String[] args) {
        ClientDAO clientDAO = new ClientDAO();

        Client testClient = new Client();
        testClient.setUsername("Yon deivi");
        testClient.setPassword("password123");
        testClient.setGmail("uanda@gmail.com ");

        try {
            clientDAO.insertClient(testClient);
            System.out.println("Usuario insertado correctamente: " + testClient.getUsername());
        } catch (Exception e) {
            System.out.println("Error al insertar usuario: " + e.getMessage());
        }
    }
}
