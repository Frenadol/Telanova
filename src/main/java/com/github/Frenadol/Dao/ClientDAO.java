package com.github.Frenadol.Dao;

import com.github.Frenadol.DataBase.ConnectionDB;
import com.github.Frenadol.DataBase.ConnectionH2;
import com.github.Frenadol.Model.Client;

import java.sql.*;

public class ClientDAO {
    private static final String INSERT = "INSERT INTO usuario (nombre_usuario,contraseña,gmail,imagen_perfil) VALUES (?,?,?,?)";
    private static final String INSERT_CLIENTE = "INSERT INTO cliente (id_cliente, cartera) VALUES (?, ?)";
    private static final String UPDATE_WALLET = "UPDATE cliente SET cartera=? WHERE id_cliente=?";
    private static final String FIND_BY_NAME = "SELECT u.id_usuario, u.nombre_usuario, u.contraseña,u.gmail,c.cartera FROM usuario u JOIN cliente c ON c.id_cliente = u.id_usuario WHERE u.nombre_usuario=?";
    private static final String FIND_BY_GMAIL = "SELECT * FROM usuario WHERE gmail=?";
    // H2 SENTENCES
    private static final String H2_INSERT = "INSERT INTO \"usuario\" (\"nombre_usuario\", \"contraseña\", \"gmail\", \"imagen_perfil\") VALUES (?, ?, ?, ?)";
    private static final String H2_INSERT_CLIENTE = "INSERT INTO \"cliente\" (\"id_cliente\", \"cartera\") VALUES (?, ?)";
    private static final String H2_UPDATE_WALLET = "UPDATE \"cliente\" SET \"cartera\" = ? WHERE \"id_cliente\" = ?";
    private static final String H2_FIND_BY_NAME = "SELECT u.\"id_usuario\", u.\"nombre_usuario\", u.\"contraseña\", u.\"gmail\", c.\"cartera\" FROM \"usuario\" u JOIN \"cliente\" c ON c.\"id_cliente\" = u.\"id_usuario\" WHERE u.\"nombre_usuario\" = ?";
    private static final String H2_FIND_BY_GMAIL = "SELECT * FROM \"usuario\" WHERE \"gmail\" = ?";

    private Connection conn;
    private Connection connectionH2;
    public static boolean useH2 = false;

    public ClientDAO() {
        conn = ConnectionDB.getConnection();
        connectionH2 = ConnectionH2.getTEMPConnection();
    }

    /**
     * Inserts a new client into the database.
     * @param cliente The client to be inserted.
     */
    public void insertClient(Client cliente) {
        try (PreparedStatement pst = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, cliente.getUsername());
            pst.setString(2, cliente.getPassword());
            pst.setString(3, cliente.getGmail());
            pst.setBytes(4, cliente.getProfilePicture());
            pst.executeUpdate();

            if (useH2) {
                try (PreparedStatement pstH2 = connectionH2.prepareStatement(H2_INSERT, Statement.RETURN_GENERATED_KEYS)) {
                    pstH2.setString(1, cliente.getUsername());
                    pstH2.setString(2, cliente.getPassword());
                    pstH2.setString(3, cliente.getGmail());
                    pstH2.setBytes(4, cliente.getProfilePicture());
                    pstH2.executeUpdate();
                }
            }

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                int idUsuario = rs.getInt(1);
                try (PreparedStatement pstmt2 = conn.prepareStatement(INSERT_CLIENTE)) {
                    pstmt2.setInt(1, idUsuario);
                    pstmt2.setDouble(2, cliente.getWallet());
                    pstmt2.executeUpdate();

                    if (useH2) {
                        try (PreparedStatement pstmt2H2 = connectionH2.prepareStatement(H2_INSERT_CLIENTE)) {
                            pstmt2H2.setInt(1, idUsuario);
                            pstmt2H2.setDouble(2, cliente.getWallet());
                            pstmt2H2.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates the wallet balance of a client.
     * @param clientId The ID of the client.
     * @param newBalance The new balance to be set.
     */
    public void updateWallet(int clientId, double newBalance) {
        try (PreparedStatement pst = conn.prepareStatement(UPDATE_WALLET)) {
            pst.setDouble(1, newBalance);
            pst.setInt(2, clientId);
            pst.executeUpdate();

            if (useH2) {
                try (PreparedStatement pstH2 = connectionH2.prepareStatement(H2_UPDATE_WALLET)) {
                    pstH2.setDouble(1, newBalance);
                    pstH2.setInt(2, clientId);
                    pstH2.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Finds a client by their username.
     * @param name The username to search for.
     * @return The client with the specified username, or null if not found.
     */
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
                result.setWallet(res.getDouble("cartera"));
            }
            res.close();

            if (useH2) {
                try (PreparedStatement pstH2 = connectionH2.prepareStatement(H2_FIND_BY_NAME)) {
                    pstH2.setString(1, name);
                    ResultSet resH2 = pstH2.executeQuery();
                    if (resH2.next()) {
                        result = new Client();
                        result.setId_user(resH2.getInt("id_usuario"));
                        result.setUsername(resH2.getString("nombre_usuario"));
                        result.setPassword(resH2.getString("contraseña"));
                        result.setWallet(resH2.getDouble("cartera"));
                    }
                    resH2.close();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar el cliente por nombre de usuario: " + e.getMessage());
        }
        return result;
    }

    /**
     * Finds a client by their email.
     * @param gmail The email to search for.
     * @return The client with the specified email, or null if not found.
     */
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
            res.close();

            if (useH2) {
                try (PreparedStatement pstH2 = connectionH2.prepareStatement(H2_FIND_BY_GMAIL)) {
                    pstH2.setString(1, gmail);
                    ResultSet resH2 = pstH2.executeQuery();
                    if (resH2.next()) {
                        result = new Client();
                        result.setId_user(resH2.getInt("id_usuario"));
                        result.setUsername(resH2.getString("nombre_usuario"));
                        result.setPassword(resH2.getString("contraseña"));
                        result.setGmail(resH2.getString("gmail"));
                    }
                    resH2.close();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * Builds a new instance of ClientDAO.
     * @return A new ClientDAO instance.
     */
    public static ClientDAO build() {
        return new ClientDAO();
    }
}