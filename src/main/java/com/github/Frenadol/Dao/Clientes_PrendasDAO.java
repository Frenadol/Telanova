package com.github.Frenadol.Dao;

import com.github.Frenadol.DataBase.ConnectionDB;
import com.github.Frenadol.Model.Client_Clothes;
import com.github.Frenadol.Model.Clothes;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Clientes_PrendasDAO {
    private static final String SELECT_IDCLIENTS_FOR_CLIENTS_SHOPPINGCART =
            "SELECT p.id_prenda, p.precio, p.nombre_prenda, p.imagen_prenda, cp.fecha_compra, cp.cantidad " +
                    "FROM cliente c " +
                    "JOIN cliente_prenda cp ON c.id_cliente = cp.id_cliente " +
                    "JOIN prendas p ON cp.id_prenda = p.id_prenda " +
                    "WHERE c.id_cliente = ?";
    private static final String CHECK_ENTRY_EXISTS =
            "SELECT cantidad FROM cliente_prenda WHERE id_cliente = ? AND id_prenda = ?";
    private static final String INSERT_IDCLIENTE_CLIENTES_PRENDAS =
            "INSERT INTO cliente_prenda (id_prenda, id_cliente, fecha_compra, cantidad) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_CANTIDAD_PRENDA =
            "UPDATE cliente_prenda SET cantidad = ? WHERE id_cliente = ? AND id_prenda = ?";
    private static final String DELETE_PRENDA_DEL_CARRITO =
            "DELETE FROM cliente_prenda WHERE id_cliente = ? AND id_prenda = ?";

    private Connection conn;

    public Clientes_PrendasDAO() {
        conn = ConnectionDB.getConnection();
    }

    public List<Client_Clothes> viewShoppingCart(int clientId) {
        List<Client_Clothes> clientClothesList = new ArrayList<>();
        try (PreparedStatement pst = conn.prepareStatement(SELECT_IDCLIENTS_FOR_CLIENTS_SHOPPINGCART)) {
            pst.setInt(1, clientId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Client_Clothes clientClothes = new Client_Clothes();
                    Clothes clothes = new Clothes();
                    clothes.setId_clothes(rs.getInt("id_prenda"));
                    clothes.setName_clothes(rs.getString("nombre_prenda"));
                    clothes.setPrice_clothes(rs.getDouble("precio"));
                    clothes.setClothes_Visual(rs.getBytes("imagen_prenda"));
                    clientClothes.setClothes(clothes);
                    clientClothes.setCantidad(rs.getInt("cantidad"));
                    clientClothesList.add(clientClothes);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientClothesList;
    }

    public boolean entryExists(int clientId, int prendaId) {
        try (PreparedStatement pst = conn.prepareStatement(CHECK_ENTRY_EXISTS)) {
            pst.setInt(1, clientId);
            pst.setInt(2, prendaId);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addClothesToCart(int clientId, int prendaId, String purchaseDate, int quantity) {
        if (entryExists(clientId, prendaId)) {
            updateClothesQuantity(clientId, prendaId, quantity);
        } else {
            try (PreparedStatement pst = conn.prepareStatement(INSERT_IDCLIENTE_CLIENTES_PRENDAS)) {
                pst.setInt(1, prendaId);
                pst.setInt(2, clientId);
                pst.setString(3, purchaseDate);
                pst.setInt(4, quantity);
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateClothesQuantity(int clientId, int prendaId, int quantity) {
        try (PreparedStatement pst = conn.prepareStatement(CHECK_ENTRY_EXISTS)) {
            pst.setInt(1, clientId);
            pst.setInt(2, prendaId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    int existingQuantity = rs.getInt("cantidad");
                    int newQuantity = existingQuantity + quantity;
                    try (PreparedStatement updatePst = conn.prepareStatement(UPDATE_CANTIDAD_PRENDA)) {
                        updatePst.setInt(1, newQuantity);
                        updatePst.setInt(2, clientId);
                        updatePst.setInt(3, prendaId);
                        updatePst.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeClothesFromCart(int clientId, int prendaId) {
        try (PreparedStatement pst = conn.prepareStatement(DELETE_PRENDA_DEL_CARRITO)) {
            pst.setInt(1, clientId);
            pst.setInt(2, prendaId);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}