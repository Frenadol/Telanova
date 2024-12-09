package com.github.Frenadol.DataBase;

import org.apache.xmlbeans.impl.soap.SOAPConnection;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionH2 {
    private static final String URL = "jdbc:h2:./data/centroDB";
    private static final String ROOT = "sa";
    private static final String PASS = "";
    private static Connection con = null;

    // Constructor vacío
    public ConnectionH2() {}


    public static Connection getTEMPConnection() {
        try {
            return DriverManager.getConnection(URL, ROOT, PASS);
        } catch (SQLException SQLe) {
            showAlert("ERROR", "No se encuentra la base de datos", "No se ha podido establecer conexión con la base de datos.");
            throw new RuntimeException("Error al obtener conexión con H2: ", SQLe);
        }
    }

    //onsola
    private static void showAlert(String title, String header, String content) {
        System.err.println(title + ": " + header + " - " + content);
    }

    // Cargar la base de datos desde un script SQL
    public static void loadDB() {
        try (Connection con = getTEMPConnection();
             Statement stmt = con.createStatement()) {
                File db_script = new File("src/main/resources/telanova.sql");

                if (!db_script.exists()) {
                    System.out.println("NO FUURLA"); return;
                }

                try (FileInputStream fis = new FileInputStream(db_script);
                     BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {

                    StringBuilder sqlScript = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sqlScript.append(line).append("\n");
                    }

                    stmt.execute(sqlScript.toString());
                } catch (IOException e) {
                    System.out.println("no se puede leer");
                    e.printStackTrace();
                }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}