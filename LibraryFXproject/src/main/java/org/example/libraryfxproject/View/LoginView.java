package org.example.libraryfxproject.View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginView implements Initializable {
    private final Stage stage;

    @FXML
    private TextField username;
    @FXML
    private TextField password;

    public LoginView(Stage stage) {
        this.stage = stage;
        initializeLoginView();
    }

    private void initializeLoginView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/libraryfxproject/Login.fxml"));
            fxmlLoader.setController(this); // Đặt controller là đối tượng hiện tại
            Parent loginViewParent = fxmlLoader.load();
            Scene scene = new Scene(loginViewParent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }



}
