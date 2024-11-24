package org.example.Test;

import org.example.DataBase.ConnectionProperties;
import org.example.DataBase.XMLManager;

public class SaveConnection {
    public static void main(String[] args) {
        ConnectionProperties c = new ConnectionProperties("localhost", "3306", "telanova", "root", "");
        XMLManager.writeXML(c, "connection.xml");
    }
}
