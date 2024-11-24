package org.example.Test;

import org.example.Dao.ClientDAO;
import org.example.Model.User;

public class TestInsertUser {
    public static void main(String[] args) {
        ClientDAO clientDAO = new ClientDAO();

        User testUser = new User();
        testUser.setUsername("hola");
        testUser.setPassword("password123");
        testUser.setGmail("testuser@gmail5.com");
        testUser.setAdmin(false);

        try {
            clientDAO.insertUser(testUser);
            System.out.println("Usuario insertado correctamente: " + testUser.getUsername());
        } catch (Exception e) {
            System.out.println("Error al insertar usuario: " + e.getMessage());
        }
    }
}
