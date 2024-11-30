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

    private SessionManager() {
        details = new ArrayList<>();
        shoppingCart = new ArrayList<>();
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

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

    public void updateDetailQuantity(Client_Clothes clientClothes, int quantity) {
        for (Client_Clothes detail : details) {
            if (detail.equals(clientClothes)) {
                int currentQuantity = detail.getCantidad();
                detail.setCantidad(currentQuantity + quantity);
                break;
            }
        }
    }

    public List<Client_Clothes> getDetails() {
        return new ArrayList<>(details);
    }

    public void addToCart(Clothes clothes, int cantidad) {
        if (!shoppingCart.contains(clothes)) {
            shoppingCart.add(clothes);
        }
    }

    public List<Clothes> getShoppingCart() {
        return new ArrayList<>(shoppingCart);
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

    public User getCurrentUser() {
        return currentUser;
    }

    public Worker getCurrentWorker() {
        return currentWorker;
    }

    public Client getCurrentClient() {
        return currentClient;
    }
}