package org.example.Test;


import org.example.DataBase.ConnectionProperties;
import org.example.DataBase.XMLManager;

public class LoadBD {
    public static void main(String[] args) {

        ConnectionProperties conn = XMLManager.readXML(new ConnectionProperties(),"connection.xml");
        System.out.println(conn);
    }
}
