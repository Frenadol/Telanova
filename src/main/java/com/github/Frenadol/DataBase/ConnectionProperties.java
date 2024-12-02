package com.github.Frenadol.DataBase;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name="connection")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConnectionProperties implements Serializable {
    private static final long serialVersionUID=1L;

    /** Server address for the database connection */
    private String server;

    /** Port number for the database connection */
    private String port;

    /** Database name */
    private String database;

    /** Username for the database connection */
    private String user;

    /** Password for the database connection */
    private String password;

    /**
     * Constructor with all fields
     * @param server the server address
     * @param port the port number
     * @param database the database name
     * @param user the username
     * @param password the password
     */
    public ConnectionProperties(String server, String port, String database, String user, String password) {
        this.server = server;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    /** Default constructor */
    public ConnectionProperties() {
    }

    /**
     * Getter for server
     * @return the server address
     */
    public String getServer() {
        return server;
    }

    /**
     * Setter for server
     * @param server the server address
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * Getter for port
     * @return the port number
     */
    public String getPort() {
        return port;
    }

    /**
     * Setter for port
     * @param port the port number
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * Getter for database
     * @return the database name
     */
    public String getDatabase() {
        return database;
    }

    /**
     * Setter for database
     * @param database the database name
     */
    public void setDatabase(String database) {
        this.database = database;
    }

    /**
     * Getter for user
     * @return the username
     */
    public String getUser() {
        return user;
    }

    /**
     * Setter for user
     * @param user the username
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Getter for password
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for password
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns a string representation of the connection properties
     * @return a string representation of the connection properties
     */
    @Override
    public String toString() {
        return "ConnectionProperties{" +
                "server='" + server + '\'' +
                ", port='" + port + '\'' +
                ", database='" + database + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    /**
     * Constructs the JDBC URL from the connection properties
     * @return the JDBC URL
     */
    public String getURL(){
        return "jdbc:MySql://"+server+":"+port+"/"+database;
    }
}