module org.example.libraryfxproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.libraryfxproject to javafx.fxml;
    exports org.example.libraryfxproject;
}