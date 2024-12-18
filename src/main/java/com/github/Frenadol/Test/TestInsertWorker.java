package com.github.Frenadol.Test;

import com.github.Frenadol.Dao.ClientDAO;
import com.github.Frenadol.Dao.WorkerDAO;
import com.github.Frenadol.Model.User;
import com.github.Frenadol.Model.Worker;

public class TestInsertWorker {
    public static void main(String[] args) {
        WorkerDAO workerDAO = new WorkerDAO();

        Worker testWorker = new Worker();
        testWorker.setUsername("Yon deivi");
        testWorker.setPassword("password123");
        testWorker.setGmail("uanda@gmail.com ");
        testWorker.setWorker(true);
        testWorker.setHireDate("2022-01-01");


        try {
            workerDAO.insertWorker(testWorker);
            System.out.println("Usuario insertado correctamente: " + testWorker.getUsername());
        } catch (Exception e) {
            System.out.println("Error al insertar usuario: " + e.getMessage());
        }
    }
}

