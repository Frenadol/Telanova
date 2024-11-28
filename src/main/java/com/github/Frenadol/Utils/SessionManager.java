package com.github.Frenadol.Utils;

import com.github.Frenadol.Model.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;
    private Worker currentWorker;
    private Client currentClient;
    private ArrayList<Client_Clothes> details = new ArrayList<>();

    private SessionManager() {
    }

    private User selectedUser;

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User user) {
        this.selectedUser = user;
    }

    public void addDetail(Clothes garment, int cantidad) {
        LocalDate date = LocalDate.now();
        String dateString = date.getDayOfMonth()+"/"+date.getMonthValue()+"/"+date.getYear();
        details.add(new Client_Clothes(currentClient, garment, cantidad,dateString));
    }

    public ArrayList<Client_Clothes> getDetails() {
        return details;
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Worker getCurrentWorker() {
        return currentWorker;
    }

    public Client getCurrentClient() {
        return currentClient;
    }

    public void setCurrentWorker(Worker worker) {
        this.currentWorker = worker;
    }

    public void setCurrentClient(Client client) {
        this.currentClient = client;
    }


    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
}