package com.github.Frenadol.Dao;

import com.github.Frenadol.DataBase.ConnectionDB;
import com.github.Frenadol.Model.Clothes;
import com.github.Frenadol.Utils.ErrorLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClothesDAO {
    private static final String INSERT_GARMENT = "INSERT INTO prendas (nombre_prenda, talla_prenda, color_prenda, descripcion, precio, imagen_prenda, categoria, cantidad) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String FIND_CLOTHES_FOR_CLIENTS = "SELECT imagen_prenda, precio FROM prendas";
    private static final String INSERT_CREATED_CLOTHES = "INSERT INTO trabajadores_prenda (id_prenda, id_trabajador) VALUES (?, ?)";
    private static final String FIND_CLOTHES_LAZY = "SELECT id_prenda, nombre_prenda, precio, imagen_prenda, descripcion, categoria, cantidad FROM prendas";
    private static final String FIND_BY_NAME = "SELECT id_prenda, nombre_prenda, precio, imagen_prenda, descripcion, categoria, cantidad FROM prendas WHERE nombre_prenda LIKE ?";
    private static final String FIND_ALL = "SELECT * from prendas";
    private static final String  GET_QUANTITY  = "SELECT cantidad FROM prendas WHERE id_prenda = ?";
    private static final String GET_BY_ID = "SELECT * FROM prendas WHERE id_prenda = ?";


    private Connection conn;

    public ClothesDAO() {
        this.conn = ConnectionDB.getConnection();
    }
    public Clothes findClothesById(int id) {
        Clothes clothes = null;
        try (PreparedStatement pst = conn.prepareStatement(GET_BY_ID)) {
            pst.setInt(1, id);
            try (ResultSet res = pst.executeQuery()) {
                if (res.next()) {
                    clothes = new Clothes();
                    clothes.setId_clothes(res.getInt("id_prenda"));
                    clothes.setName_clothes(res.getString("nombre_prenda"));
                    clothes.setSize_clothes(res.getString("talla_prenda"));
                    clothes.setColor_clothes(res.getString("color_prenda"));
                    clothes.setDescription_clothes(res.getString("descripcion"));
                    clothes.setPrice_clothes(res.getDouble("precio"));
                    clothes.setClothes_Visual(res.getBytes("imagen_prenda"));
                    clothes.setCategory(res.getString("categoria"));
                    clothes.setCantidad(res.getInt("cantidad"));
                }
            }
        } catch (SQLException e) {
            ErrorLog.fileRead(e);
        }
        return clothes;
    }
    public void insertGarment(Clothes garment) {
        try (PreparedStatement pst = conn.prepareStatement(INSERT_GARMENT, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, garment.getName_clothes());
            pst.setString(2, garment.getSize_clothes());
            pst.setString(3, garment.getColor_clothes());
            pst.setString(4, garment.getDescription_clothes());
            pst.setDouble(5, garment.getPrice_clothes());
            pst.setBytes(6, garment.getClothes_Visual());
            pst.setString(7, garment.getCategory());
            pst.setInt(8, garment.getCantidad()); // Set cantidad
            pst.executeUpdate();
        } catch (SQLException e) {
            ErrorLog.fileRead(e);
        }
    }
    public int getAvailableQuantity(int idClothes) {
        try (PreparedStatement pst = conn.prepareStatement(GET_QUANTITY)) {
            pst.setInt(1, idClothes);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cantidad");
                }
            }
        } catch (SQLException e) {
            ErrorLog.fileRead(e);
        }
        return 0;
    }

    public void insertCreatedClothes(int idClothes, int idWorker) {
        if (idWorker > 0 && idClothes > 0) {
            try (PreparedStatement pst = conn.prepareStatement(INSERT_CREATED_CLOTHES)) {
                pst.setInt(1, idClothes);
                pst.setInt(2, idWorker);
                pst.executeUpdate();
            } catch (SQLException e) {
                ErrorLog.fileRead(e);
            }
        }
    }

    public int getLastInsertedId() {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()")) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            ErrorLog.fileRead(e);
        }
        return 0;
    }

    public List<ClothesLazyAll> findAllClothesLazy() {
        List<ClothesLazyAll> result = new ArrayList<>();
        try (PreparedStatement pst = conn.prepareStatement(FIND_CLOTHES_LAZY);
             ResultSet res = pst.executeQuery()) {
            while (res.next()) {
                ClothesLazyAll clothes = new ClothesLazyAll(
                        res.getInt("id_prenda"),
                        res.getString("nombre_prenda"),
                        res.getDouble("precio"),
                        res.getBytes("imagen_prenda")
                );
                result.add(clothes);
            }
        } catch (SQLException e) {
            ErrorLog.fileRead(e);
        }
        return result;
    }

    public List<Clothes> findAll() {
        List<Clothes> result = new ArrayList<>();
        try (PreparedStatement pst = conn.prepareStatement(FIND_ALL)) {
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                Clothes clothes = new Clothes();
                clothes.setId_clothes(res.getInt("id_prenda"));
                clothes.setName_clothes(res.getString("nombre_prenda"));
                clothes.setSize_clothes(res.getString("talla_prenda")); // Ensure size is set
                clothes.setColor_clothes(res.getString("color_prenda")); // Ensure color is set
                clothes.setDescription_clothes(res.getString("descripcion"));
                clothes.setPrice_clothes(res.getDouble("precio"));
                clothes.setClothes_Visual(res.getBytes("imagen_prenda"));
                clothes.setCategory(res.getString("categoria"));
                clothes.setCantidad(res.getInt("cantidad"));
                result.add(clothes);
            }
            res.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public List<Clothes> findClothesByName(String name) {
        List<Clothes> result = new ArrayList<>();
        try (PreparedStatement pst = conn.prepareStatement(FIND_BY_NAME)) {
            pst.setString(1, "%" + name + "%");
            try (ResultSet res = pst.executeQuery()) {
                while (res.next()) {
                    ClothesLazyAll clothes = new ClothesLazyAll(
                            res.getInt("id_prenda"),
                            res.getString("nombre_prenda"),
                            res.getFloat("precio"),
                            res.getBytes("imagen_prenda")
                    );
                    result.add(clothes);
                }
            }
        } catch (SQLException e) {
            ErrorLog.fileRead(e);
        }
        return result;
    }

    public ClothesDAO build() {
        return this;
    }

    public class ClothesLazyAll extends Clothes {
        public ClothesLazyAll(int idClothes, String nameClothes, double priceClothes, byte[] clothesVisual) {
            super(idClothes, nameClothes, null, priceClothes, clothesVisual, null, 0);
        }

        public ClothesLazyAll() {
            super();
        }
    }
}