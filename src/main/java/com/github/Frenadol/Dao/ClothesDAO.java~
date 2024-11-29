package com.github.Frenadol.Dao;

import com.github.Frenadol.DataBase.ConnectionDB;
import com.github.Frenadol.Model.Clothes;
import com.github.Frenadol.Utils.ErrorLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClothesDAO {
    private static final String INSERT_GARMENT = "INSERT INTO prendas (nombre_prenda, talla_prenda, color_prenda, descripcion, precio, imagen_prenda) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String FIND_CLOTHES_FOR_CLIENTS = "SELECT imagen_prenda, precio FROM prendas";
    private static final String INSERT_CREATED_CLOTHES = "INSERT INTO trabajadores_prenda (id_prenda, id_trabajador) VALUES (?, ?)";
    private static final String FIND_CLOTHES_LAZY = "SELECT id_prenda, nombre_prenda, precio, imagen_prenda,descripcion FROM prendas";

    private Connection conn;

    public ClothesDAO() {
        this.conn = ConnectionDB.getConnection();
    }

    public void insertGarment(Clothes garment) {
        try (PreparedStatement pst = conn.prepareStatement(INSERT_GARMENT, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, garment.getName_clothes());
            pst.setString(2, garment.getSize_clothes());
            pst.setString(3, garment.getColor_clothes());
            pst.setString(4, garment.getDescription_clothes());
            pst.setDouble(5, garment.getPrice_clothes());
            pst.setBytes(6, garment.getClothes_Visual());
            pst.executeUpdate();
        } catch (SQLException e) {
            ErrorLog.fileRead(e);
        }
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
                        res.getFloat("precio"),
                        res.getBytes("imagen_prenda"),
                        res.getString("descripcion")

                );
                result.add(clothes);
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
        public ClothesLazyAll(int idClothes, String nameClothes, float priceClothes, byte[] clothesVisual,String garmentDescription) {
            super(idClothes, nameClothes, null, null, garmentDescription, priceClothes, clothesVisual);
        }

        public ClothesLazyAll() {
            super();
        }
    }
}
