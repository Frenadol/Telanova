package com.github.Frenadol.Model;

public class Client extends User{
    private double wallet;


    public Client(String username, int id_user, String password, String gmail, byte[] profilePicture, double wallet) {
        super(username, id_user, password, gmail, profilePicture);
        this.wallet = wallet;
    }

    public Client(double wallet) {
        this.wallet = wallet;
    }

    public Client() {
    }


    public double getWallet() {
        return wallet;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }


    @Override
    public String toString() {
        return super.toString()+wallet;
    }
}
