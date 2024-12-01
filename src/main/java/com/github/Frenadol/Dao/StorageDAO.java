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

    public void insertStorage(Storage storage) {
        try (PreparedStatement pst = conn.prepareStatement(INSERT)) {
            pst.setInt(1, storage.getId_storage());
            pst.setString(2, storage.getStorageName());
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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

    public void updateStorage(Storage storage) {
        try (PreparedStatement pst = conn.prepareStatement(UPDATE)) {
            pst.setString(1, storage.getStorageName());
            pst.setInt(2, storage.getId_storage());
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStorage(int id_storage) {
        try (PreparedStatement pst = conn.prepareStatement(DELETE)) {
            pst.setInt(1, id_storage);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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