module org.example.libraryfxproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens org.example.libraryfxproject to javafx.fxml;
    exports org.example.libraryfxproject;
    opens org.example.libraryfxproject.View to javafx.fxml;
}