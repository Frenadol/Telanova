package com.github.Frenadol.Model;

public class Session {
    /** Singleton instance of the Session class */
    private static Session _instance;

    /** The user currently logged in */
    private static User userLogged;

    /** Private constructor to prevent instantiation */
    private Session() {
    }

    /**
     * Gets the singleton instance of the Session class.
     * If the instance doesn't exist, it creates one and logs in the user.
     * @return The singleton instance of Session.
     */
    public static Session getInstance() {
        if (_instance == null) {
            _instance = new Session();
            _instance.logIn(userLogged);
        }
        return _instance;
    }

    /**
     * Logs in a user by setting the userLogged field.
     * @param user The user to be logged in.
     */
    public void logIn(User user) {
        userLogged = user;
    }



}