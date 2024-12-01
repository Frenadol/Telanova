package com.github.Frenadol.Model;

import java.util.ArrayList;

public class Client extends User {
    private double wallet;
    private ArrayList<Clothes> clothes = new ArrayList<>();

    public Client(String username, int id_user, String password, String gmail, byte[] profilePicture, double wallet, ArrayList<Clothes> clothes) {
        super(username, id_user, password, gmail, profilePicture);
        this.wallet = wallet;
        this.clothes = clothes;
    }

    public Client(double wallet, ArrayList<Clothes> clothes) {
        this.wallet = wallet;
        this.clothes = clothes;
    }

    public Client(String username, int id_user, String password, String gmail, byte[] profilePicture, double wallet) {
        super(username, id_user, password, gmail, profilePicture);
        this.wallet = wallet;
    }

    public Client(double wallet) {
        this.wallet = wallet;
    }

    public Client() {
    }

    public void addMoneyToWallet(double amount) {
        this.wallet += amount;
    }

    public double getWallet() {
        return wallet;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }

    public ArrayList<Clothes> getClothes() {
        return clothes;
    }

    public void setClothes(ArrayList<Clothes> clothes) {
        this.clothes = clothes;
    }

    @Override
    public String toString() {
        return super.toString() + wallet;
    }
}