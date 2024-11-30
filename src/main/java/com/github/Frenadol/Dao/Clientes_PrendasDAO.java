package com.github.Frenadol.Dao;

import com.github.Frenadol.DataBase.ConnectionDB;
import com.github.Frenadol.Model.Client;
import com.github.Frenadol.Model.Client_Clothes;
import com.github.Frenadol.Model.Clothes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Clientes_PrendasDAO {
    private static final String SELECT_IDCLIENTS_FOR_CLIENTS_SHOPPINGCART =
            "SELECT p.*, cp.cantidad FROM cliente c " +
                    "JOIN cliente_prenda cp ON c.id_cliente = cp.id_cliente " +
                    "JOIN prenda p ON cp.id_prenda = p.id_prenda WHERE c.id_cliente = ?";

    private static final String INSERT_IDCLIENTE_CLIENTES_PRENDAS =
            "INSERT INTO cliente_prenda (id_prenda, id_cliente, fecha_compra,cantidad) VALUES (?, ?, ?, ?)";

    private static final String UPDATE_CANTIDAD_PRENDA =
            "UPDATE cliente_prenda SET cantidad = cantidad + ? WHERE id_cliente = ? AND id_prenda = ?";
    private static final String UPDATE_DATE =
            "UPDATE cliente_prenda SET fecha_compra = ? WHERE id_cliente = ? AND id_prenda = ?";

    private static final String DELETE_PRENDA_DEL_CARRITO =
            "DELETE FROM cliente_prenda WHERE id_cliente = ? AND id_prenda = ?";

    private Connection conn;


    Client_Clothes clientClothes = new Client_Clothes();

    public Clientes_PrendasDAO() {
        conn = ConnectionDB.getConnection();
    }
    public  void UpdateClothesDate(int clientId, int prendaId, Date purchaseDate) {
        try (PreparedStatement pst = conn.prepareStatement(UPDATE_DATE)) {
            pst.setDate(1, purchaseDate);
            pst.setInt(2, clientId);
            pst.setInt(3, prendaId);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para ver el carrito de compras de un cliente
    public List<Clothes> viewShoppingCart(int clientId) {
        List<Clothes> clothesList = new ArrayList<>();
        try (PreparedStatement pst = conn.prepareStatement(SELECT_IDCLIENTS_FOR_CLIENTS_SHOPPINGCART)) {
            pst.setInt(1, clientId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Clothes clothes = new Clothes();
                    clothes.setId_clothes(rs.getInt("id_prenda"));
                    clothes.setName_clothes(rs.getString("nombre"));
                    clothes.setDescription_clothes(rs.getString("descripcion"));
                    clothes.setPrice_clothes(rs.getDouble("precio"));
                    clientClothes.setCantidad(rs.getInt("cantidad"));
                    clothesList.add(clothes);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clothesList;
    }

    public void addClothesToCart(int clientId, int prendaId, Date purchaseDate, int quantity) {
        try (PreparedStatement pst = conn.prepareStatement(INSERT_IDCLIENTE_CLIENTES_PRENDAS)) {
            pst.setInt(1, prendaId);
            pst.setInt(2, clientId);
            pst.setDate(3, purchaseDate);  // Utiliza la fecha de compra proporcionada
            pst.setInt(4, quantity);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateClothesQuantity(int clientId, int prendaId, int quantity) {
        try (PreparedStatement pst = conn.prepareStatement(UPDATE_CANTIDAD_PRENDA)) {
            pst.setInt(1, quantity);
            pst.setInt(2, clientId);
            pst.setInt(3, prendaId);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para eliminar una prenda del carrito de compras de un cliente
    public void removeClothesFromCart(int clientId, int prendaId) {
        try (PreparedStatement pst = conn.prepareStatement(DELETE_PRENDA_DEL_CARRITO)) {
            pst.setInt(1, clientId);
            pst.setInt(2, prendaId);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para obtener la información de la prenda en el carrito por cliente (si es necesario)
    public Clothes getClothesInCart(int clientId, int prendaId) {
        Clothes clothes = null;
        try (PreparedStatement pst = conn.prepareStatement(SELECT_IDCLIENTS_FOR_CLIENTS_SHOPPINGCART)) {
            pst.setInt(1, clientId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    if (rs.getInt("id_prenda") == prendaId) {
                        clothes = new Clothes();
                        clothes.setId_clothes(rs.getInt("id_prenda"));
                        clothes.setName_clothes(rs.getString("nombre"));
                        clothes.setDescription_clothes(rs.getString("descripcion"));
                        clothes.setPrice_clothes( rs.getDouble("precio"));
                        clientClothes.setCantidad(rs.getInt("cantidad"));
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clothes;
    }
}