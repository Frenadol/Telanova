package com.github.Frenadol.Model;

import java.util.Arrays;
import java.util.Objects;

public class Clothes {
    private int id_clothes;
    private String name_clothes;
    private String size_clothes;
    private String color_clothes;
    private String description_clothes;
    private Double price_clothes;
    private byte[] clothes_Visual;
    private Worker worker;
    private String category; // Nuevo atributo
    private int cantidad;


    public Clothes(int id_clothes, String name_clothes, String size_clothes, String color_clothes, String description_clothes, Double price_clothes, int cantidad, String category, byte[] clothes_Visual, Worker worker) {
        this.id_clothes = id_clothes;
        this.name_clothes = name_clothes;
        this.size_clothes = size_clothes;
        this.color_clothes = color_clothes;
        this.description_clothes = description_clothes;
        this.price_clothes = price_clothes;
        this.cantidad = cantidad;
        this.category = category;
        this.clothes_Visual = clothes_Visual;
        this.worker = worker;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public Clothes() {
    }

    public String getSize_clothes() {
        return size_clothes;
    }

    public void setSize_clothes(String size_clothes) {
        this.size_clothes = size_clothes;
    }

    public int getId_clothes() {
        return id_clothes;
    }

    public void setId_clothes(int id_clothes) {
        this.id_clothes = id_clothes;
    }

    public String getName_clothes() {
        return name_clothes;
    }

    public void setName_clothes(String name_clothes) {
        this.name_clothes = name_clothes;
    }

    public String getColor_clothes() {
        return color_clothes;
    }

    public void setColor_clothes(String color_clothes) {
        this.color_clothes = color_clothes;
    }

    public String getDescription_clothes() {
        return description_clothes;
    }

    public void setDescription_clothes(String description_clothes) {
        this.description_clothes = description_clothes;
    }

    public byte[] getClothes_Visual() {
        return clothes_Visual;
    }

    public void setClothes_Visual(byte[] clothes_Visual) {
        this.clothes_Visual = clothes_Visual;
    }

    public Double getPrice_clothes() {
        return price_clothes;
    }

    public void setPrice_clothes(Double price_clothes) {
        this.price_clothes = price_clothes;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Clothes(String name_clothes, Double price_clothes, byte[] clothes_Visual) {
        this.name_clothes = name_clothes;
        this.price_clothes = price_clothes;
        this.clothes_Visual = clothes_Visual;
    }

    public Clothes(int id_clothes, String name_clothes, String size_clothes, String color_clothes, String description_clothes, Double price_clothes, byte[] clothes_Visual, String category) {
        this.id_clothes = id_clothes;
        this.name_clothes = name_clothes;
        this.size_clothes = size_clothes;
        this.color_clothes = color_clothes;
        this.description_clothes = description_clothes;
        this.price_clothes = price_clothes;
        this.clothes_Visual = clothes_Visual;
        this.category = category;
    }
    public Clothes(int id_clothes, String name_clothes, String description_clothes, double price_clothes, byte[] clothes_Visual, String category, int cantidad) {
        this.id_clothes = id_clothes;
        this.name_clothes = name_clothes;
        this.description_clothes = description_clothes;
        this.price_clothes = price_clothes;
        this.clothes_Visual = clothes_Visual;
        this.category = category;
        this.cantidad = cantidad;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clothes clothes = (Clothes) o;
        return Objects.equals(name_clothes, clothes.name_clothes);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name_clothes);
    }

    @Override
    public String toString() {
        return "Clothes{" +
                "id_clothes=" + id_clothes +
                ", name_clothes='" + name_clothes + '\'' +
                ", size_clothes='" + size_clothes + '\'' +
                ", color_clothes='" + color_clothes + '\'' +
                ", description_clothes='" + description_clothes + '\'' +
                ", price_clothes=" + price_clothes +
                ", clothes_Visual=" + Arrays.toString(clothes_Visual) +
                ", worker=" + worker +
                ", category='" + category + '\'' +
                ", cantidad=" + cantidad +
                '}';
    }
}