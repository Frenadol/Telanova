package com.github.Frenadol.Dao;

import com.github.Frenadol.DataBase.ConnectionDB;
import com.github.Frenadol.Model.Clothes;
import com.github.Frenadol.Utils.ErrorLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClothesDAO {
    private static final String FIND_BY_STORAGE_ID = "SELECT * FROM prendas WHERE id_almacen=?";
    private static final String INSERT_GARMENT = "INSERT INTO prendas (nombre_prenda, talla_prenda, color_prenda, descripcion, precio, imagen_prenda, categoria, cantidad, id_almacen) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_CREATED_CLOTHES = "INSERT INTO trabajadores_prenda (id_prenda, id_trabajador) VALUES (?, ?)";
    private static final String FIND_CLOTHES_LAZY = "SELECT id_prenda, nombre_prenda, precio, imagen_prenda, descripcion, categoria, cantidad FROM prendas";
    private static final String FIND_BY_NAME = "SELECT id_prenda, nombre_prenda, precio, imagen_prenda, descripcion, categoria, cantidad FROM prendas WHERE nombre_prenda LIKE ?";
    private static final String FIND_ALL = "SELECT * from prendas";
    private static final String GET_QUANTITY = "SELECT cantidad FROM prendas WHERE id_prenda = ?";
    private static final String GET_BY_ID = "SELECT * FROM prendas WHERE id_prenda = ?";
    private static final String UPDATE_QUANTITY = "UPDATE prendas SET cantidad=? WHERE id_prenda=?";
    private static final String UPDATE = "UPDATE prendas SET nombre_prenda=?, talla_prenda=?, color_prenda=?, descripcion=?, precio=?, imagen_prenda=?, categoria=?, cantidad=? WHERE id_prenda=?";
    private static final String COUNT_CATEGORY = "SELECT categoria, COUNT(*) AS count FROM prendas GROUP BY categoria";
    private static final String COUNT_BY_SIZE = "SELECT talla_prenda, COUNT(*) AS count FROM prendas GROUP BY talla_prenda";
    private static final String COUNT_BY_COLOR = "SELECT color_prenda, COUNT(*) AS count FROM prendas GROUP BY color_prenda";
    private static final String FIND_BY_CATEGORY="SELECT * FROM prendas WHERE categoria = ?";
    private static final String COUNTS_GARMENTS = "SELECT COUNT(*) FROM prendas WHERE nombre_prenda = ?";

    private Connection conn;

    public ClothesDAO() {
        this.conn = ConnectionDB.getConnection();
    }

    /**
     * Finds clothes by their ID.
     * @param id The ID of the clothes.
     * @return The clothes with the specified ID, or null if not found.
     */
    public Clothes findClothesById(int id) {
        Clothes clothes = null;
        try (PreparedStatement pst = conn.prepareStatement(GET_BY_ID)) {
            pst.setInt(1, id);
            try (ResultSet res = pst.executeQuery()) {
                if (res.next()) {
                    clothes = new Clothes();
                    clothes.setId_clothes(res.getInt("id_prenda"));
                    clothes.setName_clothes(res.getString("nombre_prenda"));
                    clothes.setSize_clothes(res.getString("talla_prenda"));
                    clothes.setColor_clothes(res.getString("color_prenda"));
                    clothes.setDescription_clothes(res.getString("descripcion"));
                    clothes.setPrice_clothes(res.getDouble("precio"));
                    clothes.setClothes_Visual(res.getBytes("imagen_prenda"));
                    clothes.setCategory(res.getString("categoria"));
                    clothes.setCantidad(res.getInt("cantidad"));
                }
            }
        } catch (SQLException e) {
            ErrorLog.fileRead(e);
        }
        return clothes;
    }

    /**
     * Finds clothes by their category.
     * @param category The category to search for.
     * @return A list of clothes in the specified category.
     */
    public List<Clothes> findClothesByCategory(String category) {
        List<Clothes> result = new ArrayList<>();
        try (PreparedStatement pst = conn.prepareStatement(FIND_BY_CATEGORY)) {
            pst.setString(1, category);
            try (ResultSet res = pst.executeQuery()) {
                while (res.next()) {
                    Clothes clothes = new Clothes();
                    clothes.setId_clothes(res.getInt("id_prenda"));
                    clothes.setName_clothes(res.getString("nombre_prenda"));
                    clothes.setSize_clothes(res.getString("talla_prenda"));
                    clothes.setColor_clothes(res.getString("color_prenda"));
                    clothes.setDescription_clothes(res.getString("descripcion"));
                    clothes.setPrice_clothes(res.getDouble("precio"));
                    clothes.setClothes_Visual(res.getBytes("imagen_prenda"));
                    clothes.setCategory(res.getString("categoria"));
                    clothes.setCantidad(res.getInt("cantidad"));
                    result.add(clothes);
                }
            }
        } catch (SQLException e) {
            ErrorLog.fileRead(e);
        }
        return result;
    }

    /**
     * Checks if a garment exists by its name.
     * @param garmentName The name of the garment.
     * @return True if the garment exists, false otherwise.
     */
    public boolean garmentExists(String garmentName) {
        try (PreparedStatement pst = conn.prepareStatement(COUNTS_GARMENTS)) {
            pst.setString(1, garmentName);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            ErrorLog.fileRead(e);
        }
        return false;
    }

    /**
     * Counts clothes by their size.
     * @return A map of sizes and their respective counts.
     */
    public Map<String, Integer> countClothesBySize() {
        Map<String, Integer> sizeCounts = new HashMap<>();
        try (PreparedStatement pst = conn.prepareStatement(COUNT_BY_SIZE);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                sizeCounts.put(rs.getString("talla_prenda"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            ErrorLog.fileRead(e);
        }
        return sizeCounts;
    }

    /**
     * Counts clothes by their category.
     * @return A map of categories and their respective counts.
     */
    public Map<String, Integer> countClothesByCategory() {
        Map<String, Integer> categoryCounts = new HashMap<>();
        try (PreparedStatement pst = conn.prepareStatement(COUNT_CATEGORY);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                categoryCounts.put(rs.getString("categoria"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            ErrorLog.fileRead(e);
        }
        return categoryCounts;
    }

    /**
     * Counts clothes by their color.
     * @return A map of colors and their respective counts.
     */
    public Map<String, Integer> countClothesByColor() {
        Map<String, Integer> colorCounts = new HashMap<>();
        try (PreparedStatement pst = conn.prepareStatement(COUNT_BY_COLOR);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                colorCounts.put(rs.getString("color_prenda"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            ErrorLog.fileRead(e);
        }
        return colorCounts;
    }

    /**
     * Inserts a new garment into the database.
     * @param garment The garment to be inserted.
     * @param idAlmacen The storage ID where the garment is stored.
     */
    public void insertGarment(Clothes garment, int idAlmacen) {
        try (PreparedStatement pst = conn.prepareStatement(INSERT_GARMENT, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, garment.getName_clothes());
            pst.setString(2, garment.getSize_clothes());
            pst.setString(3, garment.getColor_clothes());
            pst.setString(4, garment.getDescription_clothes());
            pst.setDouble(5, garment.getPrice_clothes());
            pst.setBytes(6, garment.getClothes_Visual());
            pst.setString(7, garment.getCategory());
            pst.setInt(8, garment.getCantidad());
            pst.setInt(9, idAlmacen); // Set id_almacen
            pst.executeUpdate();
        } catch (SQLException e) {
            ErrorLog.fileRead(e);
        }
    }

    /**
     * Finds clothes by their storage ID.
     * @param storageId The storage ID to search for.
     * @return A list of clothes in the specified storage.
     */
    public List<Clothes> findByStorageId(int storageId) {
        List<Clothes> clothesList = new ArrayList<>();
        try (PreparedStatement pst = conn.prepareStatement(FIND_BY_STORAGE_ID)) {
            pst.setInt(1, storageId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Clothes clothes = new Clothes();
                    clothes.setId_clothes(rs.getInt("id_prenda"));
                    clothes.setName_clothes(rs.getString("nombre_prenda"));
                    clothes.setPrice_clothes(rs.getDouble("precio"));
                    clothes.setClothes_Visual(rs.getBytes("imagen_prenda"));
                    clothesList.add(clothes);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clothesList;
    }

    /**
     * Updates the details of a garment.
     * @param clothes The clothes object with updated details.
     */
    public void updateClothes(Clothes clothes) {
        try (PreparedStatement pst = conn.prepareStatement(UPDATE)) {
            pst.setString(1, clothes.getName_clothes());
            pst.setString(2, clothes.getSize_clothes());
            pst.setString(3, clothes.getColor_clothes());
            pst.setString(4, clothes.getDescription_clothes());
            pst.setDouble(5, clothes.getPrice_clothes());
            pst.setBytes(6, clothes.getClothes_Visual());
            pst.setString(7, clothes.getCategory());
            pst.setInt(8, clothes.getCantidad());
            pst.setInt(9, clothes.getId_clothes());
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the available quantity of a specific garment.
     * @param idClothes The ID of the garment.
     * @return The available quantity of the garment.
     */
    public int getAvailableQuantity(int idClothes) {
        try (PreparedStatement pst = conn.prepareStatement(GET_QUANTITY)) {
            pst.setInt(1, idClothes);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cantidad");
                }
            }
        } catch (SQLException e) {
            ErrorLog.fileRead(e);
        }
        return 0;
    }

    /**
     * Inserts a record of created clothes by a worker.
     * @param idClothes The ID of the clothes.
     * @param idWorker The ID of the worker.
     */
    public void insertCreatedClothes(int idClothes, int idWorker) {
        if (idWorker > 0 && idClothes > 0) {
            try (PreparedStatement pst = conn.prepareStatement(INSERT_CREATED_CLOTHES)) {
                pst.setInt(1, idClothes);
                pst.setInt(2, idWorker);
                pst.executeUpdate();
            } catch (SQLException e) {
                ErrorLog.fileRead(e);
            }
        }
    }

    /**
     * Gets the last inserted ID in the database.
     * @return The last inserted ID.
     */
    public int getLastInsertedId() {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()")) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            ErrorLog.fileRead(e);
        }
        return 0;
    }

    /**
     * Finds all clothes with lazy loading.
     * @return A list of clothes with limited details.
     */
    public List<ClothesLazyAll> findAllClothesLazy() {
        List<ClothesLazyAll> result = new ArrayList<>();
        try (PreparedStatement pst = conn.prepareStatement(FIND_CLOTHES_LAZY);
             ResultSet res = pst.executeQuery()) {
            while (res.next()) {
                ClothesLazyAll clothes = new ClothesLazyAll(
                        res.getInt("id_prenda"),
                        res.getString("nombre_prenda"),
                        res.getDouble("precio"),
                        res.getBytes("imagen_prenda")
                );
                result.add(clothes);
            }
        } catch (SQLException e) {
            ErrorLog.fileRead(e);
        }
        return result;
    }

    /**
     * Finds all clothes in the database.
     * @return A list of all clothes.
     */
    public List<Clothes> findAll() {
        List<Clothes> result = new ArrayList<>();
        try (PreparedStatement pst = conn.prepareStatement(FIND_ALL)) {
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                Clothes clothes = new Clothes();
                clothes.setId_clothes(res.getInt("id_prenda"));
                clothes.setName_clothes(res.getString("nombre_prenda"));
                clothes.setSize_clothes(res.getString("talla_prenda")); // Ensure size is set
                clothes.setColor_clothes(res.getString("color_prenda")); // Ensure color is set
                clothes.setDescription_clothes(res.getString("descripcion"));
                clothes.setPrice_clothes(res.getDouble("precio"));
                clothes.setClothes_Visual(res.getBytes("imagen_prenda"));
                clothes.setCategory(res.getString("categoria"));
                clothes.setCantidad(res.getInt("cantidad"));
                result.add(clothes);
            }
            res.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Finds clothes by their name.
     * @param name The name to search for.
     * @return A list of clothes with the specified name.
     */
    public List<Clothes> findClothesByName(String name) {
        List<Clothes> result = new ArrayList<>();
        try (PreparedStatement pst = conn.prepareStatement(FIND_BY_NAME)) {
            pst.setString(1, "%" + name + "%");
            try (ResultSet res = pst.executeQuery()) {
                while (res.next()) {
                    ClothesLazyAll clothes = new ClothesLazyAll(
                            res.getInt("id_prenda"),
                            res.getString("nombre_prenda"),
                            res.getFloat("precio"),
                            res.getBytes("imagen_prenda")
                    );
                    result.add(clothes);
                }
            }
        } catch (SQLException e) {
            ErrorLog.fileRead(e);
        }
        return result;
    }

    /**
     * Updates the quantity of a specific garment.
     * @param idClothes The ID of the garment.
     * @param quantity The new quantity to be set.
     */
    public void updateQuantity(int idClothes, int quantity) {
        try (PreparedStatement pst = conn.prepareStatement(UPDATE_QUANTITY)) {
            pst.setInt(1, quantity);
            pst.setInt(2, idClothes);
            pst.executeUpdate();
        } catch (SQLException e) {
            ErrorLog.fileRead(e);
        }
    }

    /**
     * Builds a new instance of ClothesDAO.
     * @return A new ClothesDAO instance.
     */
    public ClothesDAO build() {
        return this;
    }

    public class ClothesLazyAll extends Clothes {
        public ClothesLazyAll(int idClothes, String nameClothes, double priceClothes, byte[] clothesVisual) {
            super(idClothes, nameClothes, null, priceClothes, clothesVisual, null, 0);
        }

        public ClothesLazyAll() {
            super();
        }
    }
}