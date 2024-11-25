package com.github.Frenadol.Test;

import com.github.Frenadol.DataBase.ConnectionProperties;
import com.github.Frenadol.DataBase.XMLManager;

public class SaveConnection {
    public static void main(String[] args) {
        ConnectionProperties c = new ConnectionProperties("localhost", "3306", "telanova", "root", "");
        XMLManager.writeXML(c, "connection.xml");
    }
}
