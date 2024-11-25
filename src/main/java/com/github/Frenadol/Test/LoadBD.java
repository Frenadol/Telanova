package com.github.Frenadol.Test;


import com.github.Frenadol.DataBase.ConnectionProperties;
import com.github.Frenadol.DataBase.XMLManager;

public class LoadBD {
    public static void main(String[] args) {

        ConnectionProperties conn = XMLManager.readXML(new ConnectionProperties(),"connection.xml");
        System.out.println(conn);
    }
}
