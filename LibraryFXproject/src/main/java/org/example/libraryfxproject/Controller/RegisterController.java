package org.example.libraryfxproject.Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.libraryfxproject.Dao.AccountDAO;
import org.example.libraryfxproject.Dao.UserDAO;
import org.example.libraryfxproject.View.LoginView;
import org.example.libraryfxproject.View.RegisterView;

import java.io.IOException;

public class RegisterController {
    private final RegisterView registerView;

    public RegisterController(RegisterView registerView) {
        this.registerView = registerView;
    }

    public void registerEvent() {
        registerView.getBackButton().setOnAction(event -> {
            openLoginView((Stage)((Node)event.getSource()).getScene().getWindow());
        });
    }

    private void openLoginView(Stage stage) {
        LoginView loginView = new LoginView(stage);
    }
}
