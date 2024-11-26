package com.github.Frenadol.Dao;

import com.github.Frenadol.DataBase.ConnectionDB;
import com.github.Frenadol.Model.Client;
import com.github.Frenadol.Model.Clothes;

import java.sql.*;

public class ClothesDAO {
    private static final String INSERT_GARMENT = "INSERT INTO prendas (nombre_prenda,talla_prenda,color_prenda,descripcion,precio,imagen_prenda) VALUES (?,?,?,?,?,?)";
    private static final String FIND_CLOTHES_FOR_CLIENTS="Select imagen_prenda,precio from prenda ";
    private Connection conn;
    public ClothesDAO() {
        conn = ConnectionDB.getConnection();
    }

    public void insertGarment(Clothes garment) {
        try (PreparedStatement pst = conn.prepareStatement(INSERT_GARMENT, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1,garment.getName_clothes());
            pst.setString(2,garment.getSize_clothes());
            pst.setString(3,garment.getColor_clothes());
            pst.setString(4,garment.getDescription_clothes());
            pst.setDouble(5,garment.getPrice_clothes());
            pst.setBytes(6,garment.getClothes_Visual());

            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void findClothesForClients(Clothes garments) {
        try (PreparedStatement pst = conn.prepareStatement(INSERT_GARMENT, Statement.RETURN_GENERATED_KEYS)) {
            pst.setBytes(1,garments.getClothes_Visual());
            pst.setDouble(5,garments.getPrice_clothes());



            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
