package com.github.Frenadol.Test;

import com.github.Frenadol.Dao.UserDAO;
import com.github.Frenadol.Model.User;

public class TestInsertUser {
    public static void main(String[] args) {
        UserDAO clientDAO = new UserDAO();

        User testUser = new User();
        testUser.setUsername("Yon deivi");
        testUser.setPassword("password123");
        testUser.setGmail("uanda@gmail.com ");
        testUser.setAdmin(false);

        try {
            clientDAO.insertUser(testUser);
            System.out.println("Usuario insertado correctamente: " + testUser.getUsername());
        } catch (Exception e) {
            System.out.println("Error al insertar usuario: " + e.getMessage());
        }
    }
}
