package com.github.Frenadol.Dao;

import com.github.Frenadol.DataBase.ConnectionDB;
import com.github.Frenadol.Model.Client;
import com.github.Frenadol.Model.User;
import com.github.Frenadol.Model.Worker;

import java.sql.*;

public class ClientDAO {
    private static final String INSERT = "INSERT INTO usuario (nombre_usuario,contraseña,gmail,imagen_perfil) VALUES (?,?,?,?)";
    private static final String INSERT_CLIENTE = "INSERT INTO cliente (id_cliente, cartera) VALUES (?, ?)";
    private static final String UPDATE = "UPDATE usuario SET nombre_usuario=?, contraseña=?, gmail=?, esadministrador=?, WHERE id_usuario=?";
    private static final String FIND_BY_NAME = "SELECT u.id_usuario, u.nombre_usuario, u.contraseña,u.gmail FROM usuario u  JOIN cliente c ON c.id_cliente = u.id_usuario WHERE u.nombre_usuario=?";
    private static final String FIND_BY_GMAIL = "SELECT * FROM usuario WHERE gmail=?";

    private Connection conn;

    public ClientDAO() {
        conn = ConnectionDB.getConnection();
    }

    public void insertClient(Client cliente) {
        try (PreparedStatement pst = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, cliente.getUsername());
            pst.setString(2, cliente.getPassword());
            pst.setString(3, cliente.getGmail());
            pst.setBytes(4, cliente.getProfilePicture());
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                int idUsuario = rs.getInt(1);
                try (PreparedStatement pstmt2 = conn.prepareStatement(INSERT_CLIENTE)) {
                    pstmt2.setInt(1, idUsuario);
                    pstmt2.setDouble(2, cliente.getWallet());
                    pstmt2.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void updateUser(User entity) {
        try (PreparedStatement pst = conn.prepareStatement(UPDATE)) {
            pst.setString(1, entity.getUsername());
            pst.setString(2, entity.getPassword());
            pst.setString(3, entity.getGmail());
            pst.setInt(5, entity.getId_user());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Client findByName(String name) {
        Client result = null;
        try (PreparedStatement pst = conn.prepareStatement(FIND_BY_NAME)) {
            pst.setString(1, name);
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                result = new Client();
                result.setId_user(res.getInt("id_usuario"));
                result.setUsername(res.getString("nombre_usuario"));
                result.setPassword(res.getString("contraseña"));
            }
            res.close();
        } catch (SQLException e) {
            System.err.println("Error al buscar el cliente por nombre de usuario: " + e.getMessage());
        }
        return result;
    }

    public Client findByGmail(String gmail) {
        Client result = null;
        try (PreparedStatement pst = conn.prepareStatement(FIND_BY_GMAIL)) {
            pst.setString(1, gmail);
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                result = new Client();
                result.setId_user(res.getInt("id_usuario"));
                result.setUsername(res.getString("nombre_usuario"));
                result.setPassword(res.getString("contraseña"));
                result.setGmail(res.getString("gmail"));
                
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    public static ClientDAO build(){
        return new ClientDAO();
    }
}