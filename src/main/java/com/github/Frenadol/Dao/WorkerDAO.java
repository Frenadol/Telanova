package com.github.Frenadol.Dao;

import com.github.Frenadol.DataBase.ConnectionDB;
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
    private Connection conn;

    public WorkerDAO() {
        conn = ConnectionDB.getConnection();
    }

    /**
     * Inserts a new worker into the database.
     * @param worker The worker to be inserted.
     */
    public void insertWorker(Worker worker) {
        try (PreparedStatement pst = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, worker.getUsername());
            pst.setString(2, worker.getPassword());
            pst.setString(3, worker.getGmail());
            pst.setBytes(4, worker.getProfilePicture());
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                int idUsuario = rs.getInt(1);
                try (PreparedStatement pstmt2 = conn.prepareStatement(INSERT_TRABAJADOR)) {
                    pstmt2.setInt(1, idUsuario);
                    pstmt2.setBoolean(2, worker.isWorker());
                    pstmt2.setString(3, worker.getHireDate());
                    pstmt2.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Finds a worker by their email.
     * @param email The email to search for.
     * @return The worker with the specified email, or null if not found.
     */
    public Worker findByEmail(String email) {
        Worker result = null;
        try (PreparedStatement pst = conn.prepareStatement(FIND_BY_GMAIL)) {
            pst.setString(1, email);
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                result = new Worker();
                result.setId_user(res.getInt("id_usuario"));
                result.setUsername(res.getString("nombre_usuario"));
                result.setPassword(res.getString("contraseña"));
                result.setGmail(res.getString("gmail"));
                result.setProfilePicture(res.getBytes("imagen_perfil"));
            }
            res.close();
        } catch (SQLException e) {
            System.err.println("Error al buscar trabajador por correo electrónico: " + e.getMessage());
        }
        return result;
    }

    /**
     * Finds a worker by their username.
     * @param name The username to search for.
     * @return The worker with the specified username, or null if not found.
     */
    public Worker findByName(String name) {
        Worker result = null;
        try (PreparedStatement pst = conn.prepareStatement(FIND_BY_NAME)) {
            pst.setString(1, name);
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                result = new Worker();
                result.setId_user(res.getInt("id_usuario"));
                result.setUsername(res.getString("nombre_usuario"));
                result.setPassword(res.getString("contraseña"));
                result.setGmail(res.getString("gmail"));
                result.setProfilePicture(res.getBytes("imagen_perfil"));
            }
            res.close();
        } catch (SQLException e) {
            System.err.println("Error al buscar trabajador por nombre de usuario: " + e.getMessage());
        }
        return result;
    }

    /**
     * Finds a worker by their ID.
     * @param id The ID to search for.
     * @return The worker with the specified ID, or null if not found.
     */
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
        } catch (SQLException e) {
            ErrorLog.fileRead(e);
        }
        return result;
    }

    /**
     * Updates the details of a worker.
     * @param worker The worker object with updated details.
     */
    public void updateWorker(Worker worker) {
        try (PreparedStatement pst = conn.prepareStatement(UPDATE)) {
            pst.setString(1, worker.getUsername());
            pst.setString(2, worker.getPassword());
            pst.setString(3, worker.getGmail());
            pst.setBytes(4, worker.getProfilePicture());
            pst.setInt(5, worker.getId_user());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Builds a new instance of WorkerDAO.
     * @return A new WorkerDAO instance.
     */
    public static WorkerDAO build() {
        return new WorkerDAO();
    }
}