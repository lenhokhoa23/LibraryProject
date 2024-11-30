module org.example.libraryfxproject {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;
    requires mysql.connector.j;
    requires com.jfoenix;
    requires org.apache.poi.ooxml;
    requires org.controlsfx.controls;
    requires com.google.zxing;
    requires org.apache.commons.io;
    requires com.google.zxing.javase;
    requires java.desktop;
    requires com.google.api.services.books;
    requires google.api.client;
    requires com.google.api.client;
    requires com.google.api.client.json.jackson2;

    opens org.example.libraryfxproject to javafx.fxml;
    opens org.example.libraryfxproject.View to javafx.fxml;
    opens org.example.libraryfxproject.Model to javafx.base;
    exports org.example.libraryfxproject;
    exports org.example.libraryfxproject.Dao;
    exports org.example.libraryfxproject.Model;
}
