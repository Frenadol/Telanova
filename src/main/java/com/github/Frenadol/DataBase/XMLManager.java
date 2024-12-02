package com.github.Frenadol.DataBase;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;

public class XMLManager {

    /**
     * Writes an object to an XML file.
     * @param <T> The type of the object.
     * @param c The object to be written to XML.
     * @param filename The name of the XML file.
     * @return true if the operation was successful, false otherwise.
     */
    public static <T> boolean writeXML(T c, String filename) {
        boolean result = false;
        JAXBContext context;
        try {
            context = JAXBContext.newInstance(c.getClass());
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            m.marshal(c, new File(filename));
            result = true;
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Reads an object from an XML file.
     * @param <T> The type of the object.
     * @param c The object to be populated with data from the XML file.
     * @param filename The name of the XML file.
     * @return The populated object.
     */
    public static <T> T readXML(T c, String filename) {
        T result = c;
        JAXBContext context;
        try {
            context = JAXBContext.newInstance(c.getClass());
            Unmarshaller um = context.createUnmarshaller();
            result = (T) um.unmarshal(new File(filename));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return result;
    }
}