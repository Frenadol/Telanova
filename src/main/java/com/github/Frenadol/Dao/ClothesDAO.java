package com.github.Frenadol.Dao;

import com.github.Frenadol.DataBase.ConnectionDB;
import com.github.Frenadol.Model.Client;
import com.github.Frenadol.Model.Clothes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClothesDAO {
    private static final String INSERT_GARMENT = "INSERT INTO prendas (nombre_prenda,talla_prenda,color_prenda,descripcion,precio,imagen_prenda,idtrabajador) VALUES (?,?,?,?,?,?,?)";
    private static final String FIND_CLOTHES_FOR_CLIENTS = "Select imagen_prenda,precio from prendas ";
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
            pst.setInt(7, garment.getWorker().getId_user());


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

    public ClothesDAO build() {

        return new ClothesDAO();
    }


}