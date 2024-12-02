package com.github.Frenadol.Utils;

import com.github.Frenadol.Model.*;

import java.util.ArrayList;
import java.util.List;

public class SessionManager {
    private static SessionManager instance;
    private List<Client_Clothes> details;
    private List<Clothes> shoppingCart;
    private User currentUser;
    private Worker currentWorker;
    private Client currentClient;
    private Storage currentStorage;

    private SessionManager() {
        details = new ArrayList<>();
        shoppingCart = new ArrayList<>();
    }

    /** Gets the singleton instance of the SessionManager class. */
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /** Adds a new detail to the list or updates the quantity if it already exists. */
    public void addDetail(Client_Clothes newDetail, int quantity) {
        for (Client_Clothes detail : details) {
            if (detail.getClothes().getName_clothes().equals(newDetail.getClothes().getName_clothes())) {
                updateDetailQuantity(detail, quantity);
                return;
            }
        }
        newDetail.setCantidad(quantity);
        details.add(newDetail);
    }

    /** Updates the quantity of an existing detail. */
    public void updateDetailQuantity(Client_Clothes clientClothes, int quantity) {
        for (Client_Clothes detail : details) {
            if (detail.equals(clientClothes)) {
                int currentQuantity = detail.getCantidad();
                detail.setCantidad(currentQuantity + quantity);
                break;
            }
        }
    }

    /** Gets the list of client clothes details. */
    public List<Client_Clothes> getDetails() {
        return new ArrayList<>(details);
    }

    /** Adds clothes to the shopping cart. */
    public void addToCart(Clothes clothes, int cantidad) {
        if (!shoppingCart.contains(clothes)) {
            shoppingCart.add(clothes);
        }
    }

    /** Gets the list of clothes in the shopping cart. */
    public List<Clothes> getShoppingCart() {
        return new ArrayList<>(shoppingCart);
    }

    /** Sets the current worker. */
    public void setCurrentWorker(Worker worker) {
        this.currentWorker = worker;
    }

    /** Sets the current client. */
    public void setCurrentClient(Client client) {
        this.currentClient = client;
    }

    /** Sets the current user. */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    /** Gets the current user. */
    public User getCurrentUser() {
        return currentUser;
    }

    /** Gets the current worker. */
    public Worker getCurrentWorker() {
        return currentWorker;
    }

    /** Gets the current client. */
    public Client getCurrentClient() {
        return currentClient;
    }

    /** Sets the current storage. */
    public void setCurrentStorage(Storage storage) {
        this.currentStorage = storage;
    }

    /** Gets the current storage. */
    public Storage getCurrentStorage() {
        return currentStorage;
    }

    public void clearSession() {
        this.currentClient = null;
        this.currentWorker = null;
        this.currentStorage = null;
    }
}