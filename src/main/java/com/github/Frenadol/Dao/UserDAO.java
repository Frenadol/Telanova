package com.github.Frenadol.Dao;

import com.github.Frenadol.DataBase.ConnectionDB;
import com.github.Frenadol.Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private static final String INSERT = "INSERT INTO usuario (nombre_usuario,contraseña,gmail,esadministrador,cartera) VALUES (?,?,?,?,?)";
    private static final String UPDATE = "UPDATE users SET nombre_usuario=?, contraseña=?, gmail=?, esadministrador=?,cartera=? WHERE Id_user=?";
    private static final String FIND_BY_NAME = "SELECT * FROM usuario WHERE nombre_usuario=?";
    private static final String FIND_BY_GMAIL = "SELECT * FROM usuario where gmail";


    private Connection conn;

    public UserDAO() {
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
            pst.setDouble(5, entity.getWallet());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateUser(User entity) {
        try (PreparedStatement pst = conn.prepareStatement(UPDATE)) {
            pst.setString(1, entity.getUsername());
            pst.setString(2, entity.getPassword());
            pst.setString(3, entity.getGmail());
            pst.setBoolean(4, entity.isAdmin());
            pst.setDouble(5, entity.getWallet());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findByName(String name) {
        User result = null;
        try (PreparedStatement pst = conn.prepareStatement(FIND_BY_NAME)) {
            pst.setString(1, name);
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                result = new User();
                result.setId_user(res.getInt("id_usuario"));
                result.setUsername(res.getString("nombre_usuario"));
                result.setPassword(res.getString("contraseña"));
                result.setGmail(res.getString("gmail"));
                result.setAdmin(res.getBoolean("Admin"));
                result.setWallet((int) res.getDouble("cartera"));
            }
            res.close();
        } catch (SQLException e) {
            System.out.println("No encuentra el usuario");
        }
        return result;
    }

    public User findByGmail(String gmail) {
        User result = null;
        try (PreparedStatement pst = conn.prepareStatement(FIND_BY_GMAIL)) {
            pst.setString(1, gmail);
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                result = new User();
                result.setId_user(res.getInt("id_usuario"));
                result.setUsername(res.getString("nombre_usuario"));
                result.setPassword(res.getString("contraseña"));
                result.setGmail(res.getString("gmail"));
                result.setAdmin(res.getBoolean("Admin"));
                result.setWallet((int) res.getDouble("cartera"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
