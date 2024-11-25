package com.github.Frenadol.Model;

import java.util.Objects;

public class User {
    private int id_user;
    private String username;
    private String password;
    private String gmail;
    private boolean isAdmin;
    private int wallet;

    public User(String username, int id_user, String password, String gmail, boolean isAdmin, int wallet) {
        this.username = username;
        this.id_user = id_user;
        this.password = password;
        this.gmail = gmail;
        this.isAdmin = isAdmin;
        this.wallet=wallet;
    }

    public double getWallet() {
        return wallet;
    }

    public void setWallet(int wallet) {
        this.wallet = wallet;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) || Objects.equals(gmail, user.gmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, gmail);
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id_user=" + id_user +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", gmail='" + gmail + '\'' +
                '}';
    }
}