module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jakarta.xml.bind;
    requires org.glassfish.jaxb.runtime; // Asegúrate de incluir esto si usas JAXB

    exports org.example;
    exports org.example.View;

    // Abre el paquete org.example.DataBase a jakarta.xml.bind
    opens org.example.DataBase to jakarta.xml.bind;

    opens org.example to javafx.fxml, jakarta.xml.bind;
    opens org.example.View to javafx.fxml;
}
