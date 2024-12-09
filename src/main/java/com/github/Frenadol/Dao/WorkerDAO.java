package com.github.Frenadol.Dao;

import com.github.Frenadol.DataBase.ConnectionDB;
import com.github.Frenadol.DataBase.ConnectionH2;
import com.github.Frenadol.Model.Worker;
import com.github.Frenadol.Utils.ErrorLog;

import java.sql.*;

public class WorkerDAO {
    private static final String INSERT = "INSERT INTO usuario (nombre_usuario,contraseña,gmail,imagen_perfil) VALUES (?,?,?,?)";
    private static final String INSERT_TRABAJADOR = "INSERT INTO trabajador (id_trabajador, estrabajador,fechacontrato) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE usuario SET nombre_usuario=?, contraseña=?, gmail=?, imagen_perfil=? WHERE id_usuario=?";
    private static final String FIND_BY_ID = "SELECT * FROM trabajador WHERE id_trabajador=?";
    private static final String FIND_BY_NAME = "SELECT u.id_usuario, u.nombre_usuario, u.contraseña,u.gmail, u.imagen_perfil FROM usuario u JOIN trabajador t ON t.id_trabajador = u.id_usuario WHERE u.nombre_usuario=?";
    private static final String FIND_BY_GMAIL = "SELECT u.id_usuario,u.nombre_usuario,u.contraseña,u.gmail, u.imagen_perfil FROM usuario u JOIN trabajador t ON t.id_trabajador=u.id_usuario WHERE u.gmail=?";

    // H2 SENTENCES
    private static final String H2_INSERT = "INSERT INTO \"usuario\" (\"nombre_usuario\",\"contraseña\",\"gmail\",\"imagen_perfil\") VALUES (?,?,?,?)";
    private static final String H2_INSERT_TRABAJADOR = "INSERT INTO \"trabajador\" (\"id_trabajador\", \"estrabajador\",\"fechacontrato\") VALUES (?, ?, ?)";
    private static final String H2_UPDATE = "UPDATE \"usuario\" SET \"nombre_usuario\"=?, \"contraseña\"=?, \"gmail\"=?, \"imagen_perfil\"=? WHERE \"id_usuario\"=?";
    private static final String H2_FIND_BY_ID = "SELECT * FROM \"trabajador\" WHERE \"id_trabajador\"=?";
    private static final String H2_FIND_BY_NAME = "SELECT u.\"id_usuario\", u.\"nombre_usuario\", u.\"contraseña\",u.\"gmail\", u.\"imagen_perfil\" FROM \"usuario\" u JOIN \"trabajador\" t ON t.\"id_trabajador\" = u.\"id_usuario\" WHERE u.\"nombre_usuario\"=?";
    private static final String H2_FIND_BY_GMAIL = "SELECT u.\"id_usuario\",u.\"nombre_usuario\",u.\"contraseña\",u.\"gmail\", u.\"imagen_perfil\" FROM \"usuario\" u JOIN \"trabajador\" t ON t.\"id_trabajador\"=u.\"id_usuario\" WHERE u.\"gmail\"=?";

    private Connection conn;
    private Connection connectionH2;
    public static boolean useH2 = false;

    public WorkerDAO() {
        conn = ConnectionDB.getConnection();
        connectionH2 = ConnectionH2.getTEMPConnection();
    }

    public void insertWorker(Worker worker) {
        try (PreparedStatement pst = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, worker.getUsername());
            pst.setString(2, worker.getPassword());
            pst.setString(3, worker.getGmail());
            pst.setBytes(4, worker.getProfilePicture());
            pst.executeUpdate();

            if (useH2) {
                try (PreparedStatement pstH2 = connectionH2.prepareStatement(H2_INSERT, Statement.RETURN_GENERATED_KEYS)) {
                    pstH2.setString(1, worker.getUsername());
                    pstH2.setString(2, worker.getPassword());
                    pstH2.setString(3, worker.getGmail());
                    pstH2.setBytes(4, worker.getProfilePicture());
                    pstH2.executeUpdate();
                }
            }

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                int idUsuario = rs.getInt(1);
                try (PreparedStatement pstmt2 = conn.prepareStatement(INSERT_TRABAJADOR)) {
                    pstmt2.setInt(1, idUsuario);
                    pstmt2.setBoolean(2, worker.isWorker());
                    pstmt2.setString(3, worker.getHireDate());
                    pstmt2.executeUpdate();

                    if (useH2) {
                        try (PreparedStatement pstmt2H2 = connectionH2.prepareStatement(H2_INSERT_TRABAJADOR)) {
                            pstmt2H2.setInt(1, idUsuario);
                            pstmt2H2.setBoolean(2, worker.isWorker());
                            pstmt2H2.setString(3, worker.getHireDate());
                            pstmt2H2.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Worker findByEmail(String email) {
        Worker result = null;
        try (PreparedStatement pst = conn.prepareStatement(FIND_BY_GMAIL)) {
            pst.setString(1, email);
            try (ResultSet res = pst.executeQuery()) {
                if (res.next()) {
                    result = new Worker();
                    result.setId_user(res.getInt("id_usuario"));
                    result.setUsername(res.getString("nombre_usuario"));
                    result.setPassword(res.getString("contraseña"));
                    result.setGmail(res.getString("gmail"));
                    result.setProfilePicture(res.getBytes("imagen_perfil"));
                }
            }

            if (useH2) {
                try (PreparedStatement pstH2 = connectionH2.prepareStatement(H2_FIND_BY_GMAIL)) {
                    pstH2.setString(1, email);
                    try (ResultSet resH2 = pstH2.executeQuery()) {
                        if (resH2.next()) {
                            result = new Worker();
                            result.setId_user(resH2.getInt("id_usuario"));
                            result.setUsername(resH2.getString("nombre_usuario"));
                            result.setPassword(resH2.getString("contraseña"));
                            result.setGmail(resH2.getString("gmail"));
                            result.setProfilePicture(resH2.getBytes("imagen_perfil"));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar trabajador por correo electrónico: " + e.getMessage());
        }
        return result;
    }

    public Worker findByName(String name) {
        Worker result = null;
        try (PreparedStatement pst = conn.prepareStatement(FIND_BY_NAME)) {
            pst.setString(1, name);
            try (ResultSet res = pst.executeQuery()) {
                if (res.next()) {
                    result = new Worker();
                    result.setId_user(res.getInt("id_usuario"));
                    result.setUsername(res.getString("nombre_usuario"));
                    result.setPassword(res.getString("contraseña"));
                    result.setGmail(res.getString("gmail"));
                    result.setProfilePicture(res.getBytes("imagen_perfil"));
                }
            }

            if (useH2) {
                try (PreparedStatement pstH2 = connectionH2.prepareStatement(H2_FIND_BY_NAME)) {
                    pstH2.setString(1, name);
                    try (ResultSet resH2 = pstH2.executeQuery()) {
                        if (resH2.next()) {
                            result = new Worker();
                            result.setId_user(resH2.getInt("id_usuario"));
                            result.setUsername(resH2.getString("nombre_usuario"));
                            result.setPassword(resH2.getString("contraseña"));
                            result.setGmail(resH2.getString("gmail"));
                            result.setProfilePicture(resH2.getBytes("imagen_perfil"));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar trabajador por nombre de usuario: " + e.getMessage());
        }
        return result;
    }

    public Worker findById(String id) {
        Worker result = null;
        try (PreparedStatement pst = conn.prepareStatement(FIND_BY_ID)) {
            pst.setInt(1, Integer.parseInt(id));
            try (ResultSet res = pst.executeQuery()) {
                if (res.next()) {
                    Worker worker = new Worker();
                    worker.setId_user(res.getInt("id_usuario"));
                    worker.setUsername(res.getString("nombre_usuario"));
                    worker.setPassword(res.getString("contraseña"));
                    worker.setGmail(res.getString("gmail"));
                    worker.setHireDate(res.getString("fechacontrato"));
                    result = worker;
                }
            }

            if (useH2) {
                try (PreparedStatement pstH2 = connectionH2.prepareStatement(H2_FIND_BY_ID)) {
                    pstH2.setInt(1, Integer.parseInt(id));
                    try (ResultSet resH2 = pstH2.executeQuery()) {
                        if (resH2.next()) {
                            Worker worker = new Worker();
                            worker.setId_user(resH2.getInt("id_usuario"));
                            worker.setUsername(resH2.getString("nombre_usuario"));
                            worker.setPassword(resH2.getString("contraseña"));
                            worker.setGmail(resH2.getString("gmail"));
                            worker.setHireDate(resH2.getString("fechacontrato"));
                            result = worker;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            ErrorLog.fileRead(e);
        }
        return result;
    }

    public void updateWorker(Worker worker) {
        try (PreparedStatement pst = conn.prepareStatement(UPDATE)) {
            pst.setString(1, worker.getUsername());
            pst.setString(2, worker.getPassword());
            pst.setString(3, worker.getGmail());
            pst.setBytes(4, worker.getProfilePicture());
            pst.setInt(5, worker.getId_user());
            pst.executeUpdate();

            if (useH2) {
                try (PreparedStatement pstH2 = connectionH2.prepareStatement(H2_UPDATE)) {
                    pstH2.setString(1, worker.getUsername());
                    pstH2.setString(2, worker.getPassword());
                    pstH2.setString(3, worker.getGmail());
                    pstH2.setBytes(4, worker.getProfilePicture());
                    pstH2.setInt(5, worker.getId_user());
                    pstH2.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static WorkerDAO build() {
        return new WorkerDAO();
    }
}