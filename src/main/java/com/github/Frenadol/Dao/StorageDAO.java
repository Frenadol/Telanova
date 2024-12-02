package com.github.Frenadol.Dao;

import com.github.Frenadol.Model.Storage;
import com.github.Frenadol.DataBase.ConnectionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StorageDAO {
    private static final String INSERT = "INSERT INTO almacen (id_almacen, nombre_almacen) VALUES (?, ?)";
    private static final String UPDATE = "UPDATE almacen SET nombre_almacen=? WHERE id_almacen=?";
    private static final String DELETE = "DELETE FROM almacen WHERE id_almacen=?";
    private static final String FIND_BY_ID = "SELECT * FROM almacen WHERE id_almacen=?";
    private static final String FIND_BY_NAME = "SELECT * FROM almacen WHERE nombre_almacen=?";
    private static final String FIND_ALL_STORAGES = "SELECT * FROM almacen";
    private Connection conn;

    public StorageDAO() {
        this.conn = ConnectionDB.getConnection();
    }

    /**
     * Inserts a new storage into the database.
     * @param storage The storage to be inserted.
     */
    public void insertStorage(Storage storage) {
        try (PreparedStatement pst = conn.prepareStatement(INSERT)) {
            pst.setInt(1, storage.getId_storage());
            pst.setString(2, storage.getStorageName());
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds a storage by its name.
     * @param storageName The name of the storage to search for.
     * @return The storage with the specified name, or null if not found.
     */
    public Storage findByName(String storageName) {
        Storage storage = null;
        try (PreparedStatement pst = conn.prepareStatement(FIND_BY_NAME)) {
            pst.setString(1, storageName);
            try (ResultSet res = pst.executeQuery()) {
                if (res.next()) {
                    storage = new Storage();
                    storage.setId_storage(res.getInt("id_almacen"));
                    storage.setStorageName(res.getString("nombre_almacen"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return storage;
    }

    /**
     * Updates the details of a storage.
     * @param storage The storage object with updated details.
     */
    public void updateStorage(Storage storage) {
        try (PreparedStatement pst = conn.prepareStatement(UPDATE)) {
            pst.setString(1, storage.getStorageName());
            pst.setInt(2, storage.getId_storage());
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a storage from the database.
     * @param id_storage The ID of the storage to be deleted.
     */
    public void deleteStorage(int id_storage) {
        try (PreparedStatement pst = conn.prepareStatement(DELETE)) {
            pst.setInt(1, id_storage);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds a storage by its ID.
     * @param id_storage The ID of the storage to search for.
     * @return The storage with the specified ID, or null if not found.
     */
    public Storage findById(int id_storage) {
        Storage storage = null;
        try (PreparedStatement pst = conn.prepareStatement(FIND_BY_ID)) {
            pst.setInt(1, id_storage);
            try (ResultSet res = pst.executeQuery()) {
                if (res.next()) {
                    storage = new Storage();
                    storage.setId_storage(res.getInt("id_almacen"));
                    storage.setStorageName(res.getString("nombre_almacen"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return storage;
    }

    /**
     * Finds all storages in the database.
     * @return A list of all storages.
     */
    public List<Storage> findAllStorages() {
        List<Storage> storages = new ArrayList<>();
        try (PreparedStatement pst = conn.prepareStatement(FIND_ALL_STORAGES);
             ResultSet res = pst.executeQuery()) {
            while (res.next()) {
                Storage storage = new Storage();
                storage.setId_storage(res.getInt("id_almacen"));
                storage.setStorageName(res.getString("nombre_almacen"));
                storages.add(storage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return storages;
    }
}