package com.github.Frenadol.Dao;

import com.github.Frenadol.DataBase.ConnectionDB;
import com.github.Frenadol.Model.Client;
import com.github.Frenadol.Model.Clothes;
import com.github.Frenadol.Utils.ErrorLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClothesDAO {
    private static final String INSERT_GARMENT = "INSERT INTO prendas (nombre_prenda,talla_prenda,color_prenda,descripcion,precio,imagen_prenda) VALUES (?,?,?,?,?,?)";
    private static final String FIND_CLOTHES_FOR_CLIENTS = "Select imagen_prenda,precio from prendas ";
    private static final String FIND_BY_ID = "SELECT * FROM prendas WHERE id_prenda=?";
    private static final String INSERT_CREATED_CLOTHES = "INSERT INTO trabajadores_prenda (id_prenda, id_trabajador) VALUES (?, ?)";

    private Connection conn;

    public ClothesDAO() {
        conn = ConnectionDB.getConnection();
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
            System.out.println(e.getMessage());
        }
    }


    public List<Clothes> findAll() {
        List<Clothes> result = new ArrayList<>();
        try (PreparedStatement pst = ConnectionDB.getConnection().prepareStatement(FIND_CLOTHES_FOR_CLIENTS)) {
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                Clothes clothes = new Clothes();
                clothes.setClothes_Visual(res.getBytes("imagen_prenda"));
                clothes.setPrice_clothes(res.getFloat("precio"));
                result.add(clothes);
            }
            res.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public void insertCreatedClothes(int  idclothes, int idworker) {
        if (idworker != 0 && idclothes != 0) {
            try (PreparedStatement pst = conn.prepareStatement(INSERT_CREATED_CLOTHES)) {
                pst.setInt(1, idclothes);
                pst.setInt(2, idworker);
                pst.executeUpdate();
            } catch (SQLException e) {
                ErrorLog.fileRead(e);
            }
        }
    }
    public int getLastInsertedId() {
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
    public ClothesDAO build() {

        return new ClothesDAO();
    }


}