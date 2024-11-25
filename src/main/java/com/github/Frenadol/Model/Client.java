package com.github.Frenadol.Model;

public class Client extends User{
    private double wallet;

    public Client(String username, int id_user, String password, String gmail, boolean isAdmin, int wallet) {
        super(username, id_user, password, gmail, isAdmin, wallet);
    }

    public Client() {
    }

    @Override
    public double getWallet() {
        return wallet;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }
}
