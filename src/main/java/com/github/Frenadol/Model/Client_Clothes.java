package com.github.Frenadol.Model;

import java.util.Objects;

public class Client_Clothes {
    /** The client associated with the clothes */
    private Client client;

    /** The clothes associated with the client */
    private Clothes clothes;

    /** The quantity of clothes */
    private int cantidad;

    /** The date of the transaction */
    private String date;

    /**
     * Constructor with all fields
     * @param client the client associated with the clothes
     * @param clothes the clothes associated with the client
     * @param cantidad the quantity of clothes
     * @param date the date of the transaction
     */
    public Client_Clothes(Client client, Clothes clothes, int cantidad, String date) {
        this.client = client;
        this.clothes = clothes;
        this.cantidad = cantidad;
        this.date = date;
    }

    /** Default constructor */
    public Client_Clothes() {
    }

    /**
     * Getter for client
     * @return the client associated with the clothes
     */
    public Client getClient() {
        return client;
    }

    /**
     * Setter for client
     * @param client the client associated with the clothes
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Getter for clothes
     * @return the clothes associated with the client
     */
    public Clothes getClothes() {
        return clothes;
    }

    /**
     * Setter for clothes
     * @param clothes the clothes associated with the client
     */
    public void setClothes(Clothes clothes) {
        this.clothes = clothes;
    }

    /**
     * Getter for cantidad
     * @return the quantity of clothes
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Setter for cantidad
     * @param cantidad the quantity of clothes
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Getter for date
     * @return the date of the transaction
     */
    public String getDate() {
        return date;
    }

    /**
     * Setter for date
     * @param date the date of the transaction
     */
    public void setDate(String date) {
        this.date = date;
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
        Client_Clothes that = (Client_Clothes) o;
        return Objects.equals(client, that.client) && Objects.equals(clothes, that.clothes) && Objects.equals(date, that.date);
    }

    /**
     * Returns a string representation of the object
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return "Client_Clothes{" +
                "client=" + client +
                ", clothes=" + clothes +
                ", cantidad=" + cantidad +
                ", date='" + date + '\'' +
                '}';
    }

    /**
     * Returns the hash code of the object
     * @return the hash code of the object
     */
    @Override
    public int hashCode() {
        return Objects.hash(client, clothes);
    }
}