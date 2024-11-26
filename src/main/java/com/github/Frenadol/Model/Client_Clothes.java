package com.github.Frenadol.Model;

import java.util.Objects;

public class Client_Clothes {
    Client client;
    Clothes clothes;
    int cantidad;
    String date;
    public Client_Clothes(Client client, Clothes clothes, int cantidad, String date) {
        this.client = client;
        this.clothes = clothes;
        this.cantidad = cantidad;
        this.date = date;
    }
    public Client_Clothes(){

    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Clothes getClothes() {
        return clothes;
    }

    public void setClothes(Clothes clothes) {
        this.clothes = clothes;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client_Clothes that = (Client_Clothes) o;
        return Objects.equals(client, that.client) && Objects.equals(clothes, that.clothes) && Objects.equals(date, that.date);
    }

    @Override
    public String toString() {
        return "Client_Clothes{" +
                "client=" + client +
                ", clothes=" + clothes +
                ", cantidad=" + cantidad +
                ", date='" + date + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(client, clothes);
    }
}
