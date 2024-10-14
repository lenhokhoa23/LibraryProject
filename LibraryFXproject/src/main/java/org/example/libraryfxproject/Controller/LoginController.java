package org.example.libraryfxproject.Controller;

import com.mysql.cj.log.Log;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.libraryfxproject.Service.LoginService;
import org.example.libraryfxproject.View.LoginView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController {
    private final LoginView loginView;
    private final LoginService loginService;

    public LoginController(LoginView loginView) {
        this.loginView = loginView;
        this.loginService = new LoginService();
    }

    public void registerEvent() {
        loginView.getLoginButton().setOnAction(event -> {
            String username = loginView.getUsername().getText();
            String password = loginView.getPassword().getText();
            if (loginService.authenticate(username, password)) {
                loginView.showSuccessMessage();
                openLibraryView((Stage) ((Node) event.getSource()).getScene().getWindow());
            } else {
                loginView.showErrorMessage();
            }
        });
    }

    private void openLibraryView(Stage stage) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/libraryfxproject/MainMenuView.fxml"));
        try {
            Parent mainViewParent = fxmlLoader.load();
            Scene scene = new Scene(mainViewParent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
