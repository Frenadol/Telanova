package com.github.Frenadol.Model;


public class Session {
    private static Session _instance;
    private static User userLogged;
    private Session(){

    }
    /**
     * Gets the singleton instance of the Session class.
     * If the instance doesn't exist, it creates one and logs in the user.
     * @return The singleton instance of Session.
     */
    public static Session getInstance(){
        if (_instance==null){
            _instance = new Session();
            _instance.logIn(userLogged);
        }
        return _instance;
    }
    public void logIn(User user){
        userLogged=user;
    }

    public User getUserLogged(){
        return userLogged;
    }

    public void logOut(){
        userLogged=null;
    }
}