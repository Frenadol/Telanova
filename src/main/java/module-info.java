module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jakarta.xml.bind;
    requires org.glassfish.jaxb.runtime;
    requires java.desktop;
    requires java.prefs;
    requires kernel;
    requires layout;
    requires io;
    requires org.apache.xmlbeans; // Asegúrate de incluir esto si usas JAXB

    exports com.github.Frenadol;
    exports com.github.Frenadol.View;
    opens com.github.Frenadol.Model to javafx.base;

    // Abre el paquete org.example.DataBase a jakarta.xml.bind
    opens com.github.Frenadol.DataBase to jakarta.xml.bind;

    opens com.github.Frenadol to javafx.fxml, jakarta.xml.bind;
    opens com.github.Frenadol.View to javafx.fxml;
}
