package com.github.Frenadol.Dao;

import com.github.Frenadol.DataBase.ConnectionDB;
import com.github.Frenadol.DataBase.ConnectionH2;
import com.github.Frenadol.Model.Client_Clothes;
import com.github.Frenadol.Model.Clothes;

import java.sql.*;
import java.util.ArrayList;
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

    // H2 SENTENCES
    private static final String H2_SELECT_IDCLIENTS_FOR_CLIENTS_SHOPPINGCART =
            "SELECT p.\"id_prenda\", p.\"precio\", p.\"nombre_prenda\", p.\"imagen_prenda\", cp.\"fecha_compra\", cp.\"cantidad\" " +
                    "FROM \"cliente\" c " +
                    "JOIN \"cliente_prenda\" cp ON c.\"id_cliente\" = cp.\"id_cliente\" " +
                    "JOIN \"prendas\" p ON cp.\"id_prenda\" = p.\"id_prenda\" " +
                    "WHERE c.\"id_cliente\" = ?";
    private static final String H2_CHECK_ENTRY_EXISTS =
            "SELECT \"cantidad\" FROM \"cliente_prenda\" WHERE \"id_cliente\" = ? AND \"id_prenda\" = ?";
    private static final String H2_INSERT_IDCLIENTE_CLIENTES_PRENDAS =
            "INSERT INTO \"cliente_prenda\" (\"id_prenda\", \"id_cliente\", \"fecha_compra\", \"cantidad\") VALUES (?, ?, ?, ?)";
    private static final String H2_UPDATE_CANTIDAD_PRENDA =
            "UPDATE \"cliente_prenda\" SET \"cantidad\" = ? WHERE \"id_cliente\" = ? AND \"id_prenda\" = ?";
    private static final String H2_DELETE_PRENDA_DEL_CARRITO =
            "DELETE FROM \"cliente_prenda\" WHERE \"id_cliente\" = ? AND \"id_prenda\" = ?";

    private Connection conn;
    private Connection connectionH2;
    public static boolean useH2 = false;

    public Clientes_PrendasDAO() {
        conn = ConnectionDB.getConnection();
        connectionH2 = ConnectionH2.getTEMPConnection();
    }

    /**
     * Retrieves the shopping cart for a specific client.
     * @param clientId The ID of the client.
     * @return A list of Client_Clothes objects representing the shopping cart.
     */
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

            if (useH2) {
                try (PreparedStatement pstH2 = connectionH2.prepareStatement(H2_SELECT_IDCLIENTS_FOR_CLIENTS_SHOPPINGCART)) {
                    pstH2.setInt(1, clientId);
                    try (ResultSet rsH2 = pstH2.executeQuery()) {
                        while (rsH2.next()) {
                            Client_Clothes clientClothes = new Client_Clothes();
                            Clothes clothes = new Clothes();
                            clothes.setId_clothes(rsH2.getInt("id_prenda"));
                            clothes.setName_clothes(rsH2.getString("nombre_prenda"));
                            clothes.setPrice_clothes(rsH2.getDouble("precio"));
                            clothes.setClothes_Visual(rsH2.getBytes("imagen_prenda"));
                            clientClothes.setClothes(clothes);
                            clientClothes.setCantidad(rsH2.getInt("cantidad"));
                            clientClothesList.add(clientClothes);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientClothesList;
    }

    /**
     * Checks if an entry exists in the shopping cart for a specific client and garment.
     * @param clientId The ID of the client.
     * @param prendaId The ID of the garment.
     * @return True if the entry exists, false otherwise.
     */
    public boolean entryExists(int clientId, int prendaId) {
        try (PreparedStatement pst = conn.prepareStatement(CHECK_ENTRY_EXISTS)) {
            pst.setInt(1, clientId);
            pst.setInt(2, prendaId);
            try (ResultSet rs = pst.executeQuery()) {
                boolean exists = rs.next();

                if (useH2) {
                    try (PreparedStatement pstH2 = connectionH2.prepareStatement(H2_CHECK_ENTRY_EXISTS)) {
                        pstH2.setInt(1, clientId);
                        pstH2.setInt(2, prendaId);
                        try (ResultSet rsH2 = pstH2.executeQuery()) {
                            exists = exists && rsH2.next();
                        }
                    }
                }
                return exists;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Adds clothes to the shopping cart for a specific client.
     * @param clientId The ID of the client.
     * @param prendaId The ID of the garment.
     * @param purchaseDate The purchase date.
     * @param quantity The quantity to be added.
     */
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

                if (useH2) {
                    try (PreparedStatement pstH2 = connectionH2.prepareStatement(H2_INSERT_IDCLIENTE_CLIENTES_PRENDAS)) {
                        pstH2.setInt(1, prendaId);
                        pstH2.setInt(2, clientId);
                        pstH2.setString(3, purchaseDate);
                        pstH2.setInt(4, quantity);
                        pstH2.executeUpdate();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates the quantity of a specific garment in the shopping cart for a client.
     * @param clientId The ID of the client.
     * @param prendaId The ID of the garment.
     * @param quantity The new quantity to be set.
     */
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

                        if (useH2) {
                            try (PreparedStatement updatePstH2 = connectionH2.prepareStatement(H2_UPDATE_CANTIDAD_PRENDA)) {
                                updatePstH2.setInt(1, newQuantity);
                                updatePstH2.setInt(2, clientId);
                                updatePstH2.setInt(3, prendaId);
                                updatePstH2.executeUpdate();
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes clothes from the shopping cart for a specific client.
     * @param clientId The ID of the client.
     * @param prendaId The ID of the garment.
     */
    public void removeClothesFromCart(int clientId, int prendaId) {
        try (PreparedStatement pst = conn.prepareStatement(DELETE_PRENDA_DEL_CARRITO)) {
            pst.setInt(1, clientId);
            pst.setInt(2, prendaId);
            pst.executeUpdate();

            if (useH2) {
                try (PreparedStatement pstH2 = connectionH2.prepareStatement(H2_DELETE_PRENDA_DEL_CARRITO)) {
                    pstH2.setInt(1, clientId);
                    pstH2.setInt(2, prendaId);
                    pstH2.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}