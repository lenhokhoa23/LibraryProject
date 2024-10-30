module org.example.libraryfxproject {
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires com.jfoenix;
    requires javafx.graphics;
    requires javafx.controls;
    requires java.desktop;


    opens org.example.libraryfxproject to javafx.fxml;
    exports org.example.libraryfxproject;
    opens org.example.libraryfxproject.View to javafx.fxml;
}