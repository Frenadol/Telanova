package com.github.Frenadol.Model;

import java.util.ArrayList;

public class Client extends User {
    /** Wallet balance of the client */
    private double wallet;

    /** List of clothes owned by the client */
    private ArrayList<Clothes> clothes = new ArrayList<>();

    /**
     * Constructor with all fields
     * @param username the username of the client
     * @param id_user the ID of the client
     * @param password the password of the client
     * @param gmail the email of the client
     * @param profilePicture the profile picture of the client
     * @param wallet the wallet balance of the client
     * @param clothes the list of clothes owned by the client
     */
    public Client(String username, int id_user, String password, String gmail, byte[] profilePicture, double wallet, ArrayList<Clothes> clothes) {
        super(username, id_user, password, gmail, profilePicture);
        this.wallet = wallet;
        this.clothes = clothes;
    }

    /**
     * Constructor with wallet and clothes
     * @param wallet the wallet balance of the client
     * @param clothes the list of clothes owned by the client
     */
    public Client(double wallet, ArrayList<Clothes> clothes) {
        this.wallet = wallet;
        this.clothes = clothes;
    }

    /**
     * Constructor with user details and wallet
     * @param username the username of the client
     * @param id_user the ID of the client
     * @param password the password of the client
     * @param gmail the email of the client
     * @param profilePicture the profile picture of the client
     * @param wallet the wallet balance of the client
     */
    public Client(String username, int id_user, String password, String gmail, byte[] profilePicture, double wallet) {
        super(username, id_user, password, gmail, profilePicture);
        this.wallet = wallet;
    }

    /**
     * Constructor with wallet
     * @param wallet the wallet balance of the client
     */
    public Client(double wallet) {
        this.wallet = wallet;
    }

    /** Default constructor */
    public Client() {
    }

    /**
     * Adds money to the client's wallet
     * @param amount the amount to be added
     */
    public void addMoneyToWallet(double amount) {
        this.wallet += amount;
    }

    /**
     * Getter for wallet
     * @return the wallet balance
     */
    public double getWallet() {
        return wallet;
    }

    /**
     * Setter for wallet
     * @param wallet the wallet balance
     */
    public void setWallet(double wallet) {
        this.wallet = wallet;
    }

    /**
     * Getter for clothes
     * @return the list of clothes owned by the client
     */
    public ArrayList<Clothes> getClothes() {
        return clothes;
    }

    /**
     * Setter for clothes
     * @param clothes the list of clothes owned by the client
     */
    public void setClothes(ArrayList<Clothes> clothes) {
        this.clothes = clothes;
    }

    /**
     * Returns a string representation of the client
     * @return a string representation of the client
     */
    @Override
    public String toString() {
        return super.toString() + wallet;
    }
}