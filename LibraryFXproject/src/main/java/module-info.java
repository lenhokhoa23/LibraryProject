module org.example.libraryfxproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.libraryfxproject to javafx.fxml;
    exports org.example.libraryfxproject;
}