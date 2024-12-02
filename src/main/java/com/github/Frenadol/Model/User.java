package com.github.Frenadol.Model;

import java.util.Arrays;
import java.util.Objects;

public class User {
    /** Username of the user */
    private String username;

    /** ID of the user */
    private int id_user;

    /** Password of the user */
    private String password;

    /** Email of the user */
    private String gmail;

    /** Profile picture of the user */
    private byte[] profilePicture;

    /**
     * Constructor with all fields
     * @param username the username of the user
     * @param id_user the ID of the user
     * @param password the password of the user
     * @param gmail the email of the user
     * @param profilePicture the profile picture of the user
     */
    public User(String username, int id_user, String password, String gmail, byte[] profilePicture) {
        this.username = username;
        this.id_user = id_user;
        this.password = password;
        this.gmail = gmail;
        this.profilePicture = profilePicture;
    }

    /** Default constructor */
    public User() {
    }

    /** Getter and setter methods */

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
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

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    /**
     * Checks if this object is equal to another object
     * @param o the other object
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id_user == user.id_user && Objects.equals(username, user.username);
    }

    /**
     * Returns the hash code of the object
     * @return the hash code of the object
     */
    @Override
    public int hashCode() {
        return Objects.hash(username, id_user);
    }

    /**
     * Returns a string representation of the object
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", id_user=" + id_user +
                ", password='" + password + '\'' +
                ", gmail='" + gmail + '\'' +
                ", profilePicture=" + Arrays.toString(profilePicture) +
                '}';
    }
}