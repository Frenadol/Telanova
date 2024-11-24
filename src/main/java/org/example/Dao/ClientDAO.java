package org.example.Dao;

import org.example.DataBase.ConnectionDB;
import org.example.Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClientDAO {
    private static final String INSERT = "INSERT INTO usuario (nombre_usuario,contraseña,gmail,esadministrador) VALUES (?,?,?,?)";
    private static final String UPDATE = "UPDATE users SET Name_user=?, Password=?, Dragon_stones=?, Admin=? WHERE Id_user=?";

    private Connection conn;

    public ClientDAO(){
        conn = ConnectionDB.getConnection();
    }
    public void insertUser(User entity) {
        try (PreparedStatement pst = conn.prepareStatement(INSERT)) {
            pst.setString(1, entity.getUsername());
            pst.setString(2, entity.getPassword());
            pst.setString(3, entity.getGmail());
            if (entity.isAdmin()) {
                pst.setInt(4, 1);
            } else {
                pst.setInt(4, 0);
            }
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("no fufa");
        }
    }

    public void updateUser(User entity){
        try(PreparedStatement pst=conn.prepareStatement(UPDATE)){
            pst.setString(1,entity.getUsername());
            pst.setString(2,entity.getPassword());
            pst.setString(3,entity.getGmail());
            pst.setBoolean(4,entity.isAdmin());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    }
