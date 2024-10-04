module org.example.javaapplicationproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.javaapplicationproject to javafx.fxml;
    exports org.example.javaapplicationproject;
}