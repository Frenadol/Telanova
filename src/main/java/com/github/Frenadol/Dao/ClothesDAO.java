package com.github.Frenadol.Dao;

import com.github.Frenadol.DataBase.ConnectionDB;
import com.github.Frenadol.DataBase.ConnectionH2;
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

    // H2 SENTENCES
    private static final String H2_FIND_BY_STORAGE_ID = "SELECT * FROM \"prendas\" WHERE \"id_almacen\"=?";
    private static final String H2_INSERT_GARMENT = "INSERT INTO \"prendas\" (\"nombre_prenda\", \"talla_prenda\", \"color_prenda\", \"descripcion\", \"precio\", \"imagen_prenda\", \"categoria\", \"cantidad\", \"id_almacen\") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String H2_INSERT_CREATED_CLOTHES = "INSERT INTO \"trabajadores_prenda\" (\"id_prenda\", \"id_trabajador\") VALUES (?, ?)";
    private static final String H2_FIND_CLOTHES_LAZY = "SELECT \"id_prenda\", \"nombre_prenda\", \"precio\", \"imagen_prenda\", \"descripcion\", \"categoria\", \"cantidad\" FROM \"prendas\"";
    private static final String H2_FIND_BY_NAME = "SELECT \"id_prenda\", \"nombre_prenda\", \"precio\", \"imagen_prenda\", \"descripcion\", \"categoria\", \"cantidad\" FROM \"prendas\" WHERE \"nombre_prenda\" LIKE ?";
    private static final String H2_FIND_ALL = "SELECT * from \"prendas\"";
    private static final String H2_GET_QUANTITY = "SELECT \"cantidad\" FROM \"prendas\" WHERE \"id_prenda\" = ?";
    private static final String H2_GET_BY_ID = "SELECT * FROM \"prendas\" WHERE \"id_prenda\" = ?";
    private static final String H2_UPDATE_QUANTITY = "UPDATE \"prendas\" SET \"cantidad\"=? WHERE \"id_prenda\"=?";
    private static final String H2_UPDATE = "UPDATE \"prendas\" SET \"nombre_prenda\"=?, \"talla_prenda\"=?, \"color_prenda\"=?, \"descripcion\"=?, \"precio\"=?, \"imagen_prenda\"=?, \"categoria\"=?, \"cantidad\"=? WHERE \"id_prenda\"=?";
    private static final String H2_COUNT_CATEGORY = "SELECT \"categoria\", COUNT(*) AS count FROM \"prendas\" GROUP BY \"categoria\"";
    private static final String H2_COUNT_BY_SIZE = "SELECT \"talla_prenda\", COUNT(*) AS count FROM \"prendas\" GROUP BY \"talla_prenda\"";
    private static final String H2_COUNT_BY_COLOR = "SELECT \"color_prenda\", COUNT(*) AS count FROM \"prendas\" GROUP BY \"color_prenda\"";
    private static final String H2_FIND_BY_CATEGORY="SELECT * FROM \"prendas\" WHERE \"categoria\" = ?";
    private static final String H2_COUNTS_GARMENTS = "SELECT COUNT(*) FROM \"prendas\" WHERE \"nombre_prenda\" = ?";

    private Connection conn;
    private Connection connectionH2;
    public static boolean useH2 = false;

    public ClothesDAO() {
        conn = ConnectionDB.getConnection();
        connectionH2 = ConnectionH2.getTEMPConnection();
    }

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

            if (useH2) {
                try (PreparedStatement pstH2 = connectionH2.prepareStatement(H2_GET_BY_ID)) {
                    pstH2.setInt(1, id);
                    try (ResultSet resH2 = pstH2.executeQuery()) {
                        if (resH2.next()) {
                            clothes = new Clothes();
                            clothes.setId_clothes(resH2.getInt("id_prenda"));
                            clothes.setName_clothes(resH2.getString("nombre_prenda"));
                            clothes.setSize_clothes(resH2.getString("talla_prenda"));
                            clothes.setColor_clothes(resH2.getString("color_prenda"));
                            clothes.setDescription_clothes(resH2.getString("descripcion"));
                            clothes.setPrice_clothes(resH2.getDouble("precio"));
                            clothes.setClothes_Visual(resH2.getBytes("imagen_prenda"));
                            clothes.setCategory(resH2.getString("categoria"));
                            clothes.setCantidad(resH2.getInt("cantidad"));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            ErrorLog.fileRead(e);
        }
        return clothes;
    }

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

            if (useH2) {
                try (PreparedStatement pstH2 = connectionH2.prepareStatement(H2_FIND_BY_CATEGORY)) {
                    pstH2.setString(1, category);
                    try (ResultSet resH2 = pstH2.executeQuery()) {
                        while (resH2.next()) {
                            Clothes clothes = new Clothes();
                            clothes.setId_clothes(resH2.getInt("id_prenda"));
                            clothes.setName_clothes(resH2.getString("nombre_prenda"));
                            clothes.setSize_clothes(resH2.getString("talla_prenda"));
                            clothes.setColor_clothes(resH2.getString("color_prenda"));
                            clothes.setDescription_clothes(resH2.getString("descripcion"));
                            clothes.setPrice_clothes(resH2.getDouble("precio"));
                            clothes.setClothes_Visual(resH2.getBytes("imagen_prenda"));
                            clothes.setCategory(resH2.getString("categoria"));
                            clothes.setCantidad(resH2.getInt("cantidad"));
                            result.add(clothes);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            ErrorLog.fileRead(e);
        }
        return result;
    }

    public boolean garmentExists(String garmentName) {
        try (PreparedStatement pst = conn.prepareStatement(COUNTS_GARMENTS)) {
            pst.setString(1, garmentName);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    if (useH2) {
                        try (PreparedStatement pstH2 = connectionH2.prepareStatement(H2_COUNTS_GARMENTS)) {
                            pstH2.setString(1, garmentName);
                            try (ResultSet rs2 = pstH2.executeQuery()) {
                                if (rs2.next()) {
                                    return rs.getInt(1) > 0 && rs2.getInt(1) > 0;
                                }
                            }
                        }
                    } else {
                        return rs.getInt(1) > 0;
                    }
                }
            }
        } catch (SQLException e) {
            ErrorLog.fileRead(e);
        }
        return false;
    }

    public Map<String, Integer> countClothesBySize() {
        Map<String, Integer> sizeCounts = new HashMap<>();
        try (PreparedStatement pst = conn.prepareStatement(COUNT_BY_SIZE);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                sizeCounts.put(rs.getString("talla_prenda"), rs.getInt("count"));
            }

            if (useH2) {
                try (PreparedStatement pstH2 = connectionH2.prepareStatement(H2_COUNT_BY_SIZE);
                     ResultSet rs2 = pstH2.executeQuery()) {
                    while (rs2.next()) {
                        sizeCounts.put(rs2.getString("talla_prenda"), rs2.getInt("count"));
                    }
                }
            }
        } catch (SQLException e) {
            ErrorLog.fileRead(e);
        }
        return sizeCounts;
    }

    public Map<String, Integer> countClothesByCategory() {
        Map<String, Integer> categoryCounts = new HashMap<>();
        try (PreparedStatement pst = conn.prepareStatement(COUNT_CATEGORY);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                categoryCounts.put(rs.getString("categoria"), rs.getInt("count"));
            }

            if (useH2) {
                try (PreparedStatement pstH2 = connectionH2.prepareStatement(H2_COUNT_CATEGORY);
                     ResultSet rs2 = pstH2.executeQuery()) {
                    while (rs2.next()) {
                        categoryCounts.put(rs2.getString("categoria"), rs2.getInt("count"));
                    }
                }
            }
        } catch (SQLException e) {
            ErrorLog.fileRead(e);
        }
        return categoryCounts;
    }

    public Map<String, Integer> countClothesByColor() {
        Map<String, Integer> colorCounts = new HashMap<>();
        try (PreparedStatement pst = conn.prepareStatement(COUNT_BY_COLOR);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                colorCounts.put(rs.getString("color_prenda"), rs.getInt("count"));
            }

            if (useH2) {
                try (PreparedStatement pstH2 = connectionH2.prepareStatement(H2_COUNT_BY_COLOR);
                     ResultSet rs2 = pstH2.executeQuery()) {
                    while (rs2.next()) {
                        colorCounts.put(rs2.getString("color_prenda"), rs2.getInt("count"));
                    }
                }
            }
        } catch (SQLException e) {
            ErrorLog.fileRead(e);
        }
        return colorCounts;
    }

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
            pst.setInt(9, idAlmacen);
            pst.executeUpdate();

            if (useH2) {
                try (PreparedStatement pstH2 = connectionH2.prepareStatement(H2_INSERT_GARMENT, Statement.RETURN_GENERATED_KEYS)) {
                    pstH2.setString(1, garment.getName_clothes());
                    pstH2.setString(2, garment.getSize_clothes());
                    pstH2.setString(3, garment.getColor_clothes());
                    pstH2.setString(4, garment.getDescription_clothes());
                    pstH2.setDouble(5, garment.getPrice_clothes());
                    pstH2.setBytes(6, garment.getClothes_Visual());
                    pstH2.setString(7, garment.getCategory());
                    pstH2.setInt(8, garment.getCantidad());
                    pstH2.setInt(9, idAlmacen);
                    pstH2.executeUpdate();
                }
            }
        } catch (SQLException e) {
            ErrorLog.fileRead(e);
        }
    }

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

            if (useH2) {
                try (PreparedStatement pstH2 = connectionH2.prepareStatement(H2_FIND_BY_STORAGE_ID)) {
                    pstH2.setInt(1, storageId);
                    try (ResultSet rsH2 = pstH2.executeQuery()) {
                        while (rsH2.next()) {
                            Clothes clothes = new Clothes();
                            clothes.setId_clothes(rsH2.getInt("id_prenda"));
                            clothes.setName_clothes(rsH2.getString("nombre_prenda"));
                            clothes.setPrice_clothes(rsH2.getDouble("precio"));
                            clothes.setClothes_Visual(rsH2.getBytes("imagen_prenda"));
                            clothesList.add(clothes);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clothesList;
    }

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

            if (useH2) {
                try (PreparedStatement pstH2 = connectionH2.prepareStatement(H2_UPDATE)) {
                    pstH2.setString(1, clothes.getName_clothes());
                    pstH2.setString(2, clothes.getSize_clothes());
                    pstH2.setString(3, clothes.getColor_clothes());
                    pstH2.setString(4, clothes.getDescription_clothes());
                    pstH2.setDouble(5, clothes.getPrice_clothes());
                    pstH2.setBytes(6, clothes.getClothes_Visual());
                    pstH2.setString(7, clothes.getCategory());
                    pstH2.setInt(8, clothes.getCantidad());
                    pstH2.setInt(9, clothes.getId_clothes());
                    pstH2.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getAvailableQuantity(int idClothes) {
        try (PreparedStatement pst = conn.prepareStatement(GET_QUANTITY)) {
            pst.setInt(1, idClothes);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    if (useH2) {
                        try (PreparedStatement pstH2 = connectionH2.prepareStatement(H2_GET_QUANTITY)) {
                            pstH2.setInt(1, idClothes);
                            try (ResultSet rsH2 = pstH2.executeQuery()) {
                                if (rsH2.next()) {
                                    return rs.getInt("cantidad");
                                }
                            }
                        }
                    } else {
                        return rs.getInt("cantidad");
                    }
                }
            }
        } catch (SQLException e) {
            ErrorLog.fileRead(e);
        }
        return 0;
    }

    public void insertCreatedClothes(int idClothes, int idWorker) {
        if (idWorker > 0 && idClothes > 0) {
            try (PreparedStatement pst = conn.prepareStatement(INSERT_CREATED_CLOTHES)) {
                pst.setInt(1, idClothes);
                pst.setInt(2, idWorker);
                pst.executeUpdate();

                if (useH2) {
                    try (PreparedStatement pstH2 = connectionH2.prepareStatement(H2_INSERT_CREATED_CLOTHES)) {
                        pstH2.setInt(1, idClothes);
                        pstH2.setInt(2, idWorker);
                        pstH2.executeUpdate();
                    }
                }
            } catch (SQLException e) {
                ErrorLog.fileRead(e);
            }
        }
    }

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

            if (useH2) {
                try (PreparedStatement pstH2 = connectionH2.prepareStatement(H2_FIND_CLOTHES_LAZY);
                     ResultSet resH2 = pstH2.executeQuery()) {
                    while (resH2.next()) {
                        ClothesLazyAll clothes = new ClothesLazyAll(
                                resH2.getInt("id_prenda"),
                                resH2.getString("nombre_prenda"),
                                resH2.getDouble("precio"),
                                resH2.getBytes("imagen_prenda")
                        );
                        result.add(clothes);
                    }
                }
            }
        } catch (SQLException e) {
            ErrorLog.fileRead(e);
        }
        return result;
    }

    public List<Clothes> findAll() {
        List<Clothes> result = new ArrayList<>();
        try (PreparedStatement pst = conn.prepareStatement(FIND_ALL);
             ResultSet res = pst.executeQuery()) {
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

            if (useH2) {
                try (PreparedStatement pstH2 = connectionH2.prepareStatement(H2_FIND_ALL);
                     ResultSet resH2 = pstH2.executeQuery()) {
                    while (resH2.next()) {
                        Clothes clothes = new Clothes();
                        clothes.setId_clothes(resH2.getInt("id_prenda"));
                        clothes.setName_clothes(resH2.getString("nombre_prenda"));
                        clothes.setSize_clothes(resH2.getString("talla_prenda"));
                        clothes.setColor_clothes(resH2.getString("color_prenda"));
                        clothes.setDescription_clothes(resH2.getString("descripcion"));
                        clothes.setPrice_clothes(resH2.getDouble("precio"));
                        clothes.setClothes_Visual(resH2.getBytes("imagen_prenda"));
                        clothes.setCategory(resH2.getString("categoria"));
                        clothes.setCantidad(resH2.getInt("cantidad"));
                        result.add(clothes);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Clothes> findClothesByName(String name) {
        List<Clothes> result = new ArrayList<>();
        try (PreparedStatement pst = conn.prepareStatement(FIND_BY_NAME)) {
            pst.setString(1, "%" + name + "%");
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

            if (useH2) {
                try (PreparedStatement pstH2 = connectionH2.prepareStatement(H2_FIND_BY_NAME)) {
                    pstH2.setString(1, "%" + name + "%");
                    try (ResultSet resH2 = pstH2.executeQuery()) {
                        while (resH2.next()) {
                            Clothes clothes = new Clothes();
                            clothes.setId_clothes(resH2.getInt("id_prenda"));
                            clothes.setName_clothes(resH2.getString("nombre_prenda"));
                            clothes.setSize_clothes(resH2.getString("talla_prenda"));
                            clothes.setColor_clothes(resH2.getString("color_prenda"));
                            clothes.setDescription_clothes(resH2.getString("descripcion"));
                            clothes.setPrice_clothes(resH2.getDouble("precio"));
                            clothes.setClothes_Visual(resH2.getBytes("imagen_prenda"));
                            clothes.setCategory(resH2.getString("categoria"));
                            clothes.setCantidad(resH2.getInt("cantidad"));
                            result.add(clothes);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            ErrorLog.fileRead(e);
        }
        return result;
    }

    public void updateQuantity(int idClothes, int quantity) {
        try (PreparedStatement pst = conn.prepareStatement(UPDATE_QUANTITY)) {
            pst.setInt(1, quantity);
            pst.setInt(2, idClothes);
            pst.executeUpdate();

            if (useH2) {
                try (PreparedStatement pstH2 = connectionH2.prepareStatement(H2_UPDATE_QUANTITY)) {
                    pstH2.setInt(1, quantity);
                    pstH2.setInt(2, idClothes);
                    pstH2.executeUpdate();
                }
            }
        } catch (SQLException e) {
            ErrorLog.fileRead(e);
        }
    }

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