package com.github.Frenadol.Model;

import java.util.ArrayList;
import java.util.Objects;

public class Storage {
    /** ID of the storage */
    private int id_storage;

    /** Name of the storage */
    private String storageName;

    /** List of clothes in the storage */
    private ArrayList<Clothes> clothes = new ArrayList<>();

    /**
     * Constructor with all fields
     * @param id_storage the ID of the storage
     * @param storageName the name of the storage
     * @param clothes the list of clothes in the storage
     */
    public Storage(int id_storage, String storageName, ArrayList<Clothes> clothes) {
        this.id_storage = id_storage;
        this.storageName = storageName;
        this.clothes = clothes;
    }

    /** Default constructor */
    public Storage() {
    }

    /** Getter and setter methods */

    public int getId_storage() {
        return id_storage;
    }

    public void setId_storage(int id_storage) {
        this.id_storage = id_storage;
    }

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

    public ArrayList<Clothes> getClothes() {
        return clothes;
    }

    public void setClothes(ArrayList<Clothes> clothes) {
        this.clothes = clothes;
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
        Storage storage = (Storage) o;
        return Objects.equals(storageName, storage.storageName);
    }

    /**
     * Returns the hash code of the object
     * @return the hash code of the object
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(storageName);
    }

    /**
     * Returns a string representation of the object
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return "Almacen{" +
                 + id_storage +
                 ": "+ storageName + '\'' +
                '}';
    }
}