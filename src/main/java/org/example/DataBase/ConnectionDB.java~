package org.example.BaseDatos;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    private final static String FILE="connection.xml";
    private static ConnectionDB _instance;
    private static Connection conn;

    private ConnectionDB(){
        ConnectionProperties properties = (ConnectionProperties) XMLManager.readXML(new ConnectionProperties(),FILE);

        try {
            conn = DriverManager.getConnection(properties.getURL(),properties.getUser(),properties.getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
            conn=null;
        }
    }

    public static Connection getConnection(){
        if(_instance==null){
            _instance = new ConnectionDB();
        }
        return conn;
    }

    public static void closeConnection(){
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}

