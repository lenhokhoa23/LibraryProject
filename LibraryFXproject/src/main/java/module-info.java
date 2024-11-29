module org.example.libraryfxproject {
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires com.jfoenix;
    requires org.apache.poi.ooxml;
    requires org.controlsfx.controls;
    requires java.desktop;
    requires com.google.zxing;
    requires org.apache.commons.io;
    requires com.google.zxing.javase;


    opens org.example.libraryfxproject to javafx.fxml;
    opens org.example.libraryfxproject.View to javafx.fxml;
    opens org.example.libraryfxproject.Model to javafx.base;
    exports org.example.libraryfxproject;
    exports org.example.libraryfxproject.Dao;
}