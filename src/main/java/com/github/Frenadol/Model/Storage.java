package com.github.Frenadol.Model;

import java.util.Objects;

public class Storage {
    private int id_storage;
    private String storageName;

    public Storage(int id_storage, String storageName) {
        this.id_storage = id_storage;
        this.storageName = storageName;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Storage storage = (Storage) o;
        return id_storage == storage.id_storage;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id_storage);
    }

    @Override
    public String toString() {
        return "Storage{" +
                "id_storage=" + id_storage +
                ", storageName='" + storageName + '\'' +
                '}';
    }

    public Storage() {

    }
}
