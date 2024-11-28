package com.github.Frenadol.Utils;

import com.github.Frenadol.Model.Client;
import com.github.Frenadol.Model.User;
import com.github.Frenadol.Model.Worker;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;
    private Worker currentWorker;
    private Client currentClient;

    private SessionManager() {}
    private User selectedUser;

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User user) {
        this.selectedUser = user;
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