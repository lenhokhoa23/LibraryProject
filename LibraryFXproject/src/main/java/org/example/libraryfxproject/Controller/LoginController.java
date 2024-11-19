package org.example.libraryfxproject.Controller;

import com.mysql.cj.log.Log;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.libraryfxproject.Model.User;
import org.example.libraryfxproject.Service.LoginService;
import org.example.libraryfxproject.Util.AlertDisplayer;
import org.example.libraryfxproject.View.LoginView;
import org.example.libraryfxproject.View.MainMenuView;
import org.example.libraryfxproject.View.RegisterView;
import org.example.libraryfxproject.View.UserView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends BaseController {
    private final LoginView loginView;
    private final LoginService loginService;

    public LoginController(LoginView loginView, AlertDisplayer alertDisplayer) {
        super(alertDisplayer);
        this.loginView = loginView;
        this.loginService = LoginService.getInstance();
    }

    public void registerEvent() {
        loginView.getLoginButton().setOnAction(event -> {
            String username = loginView.getUsername().getText();
            String password = loginView.getPassword().getText();
            if (username.isEmpty() || password.isEmpty()) {
                showErrorMessage("Please fill all blank fields!");
            } else if (loginService.authenticate(username, password) > -1) {
                showSuccessMessage("Successfully login!");
                if (loginService.authenticate(username, password) == 0) {
                    openLibrarianView((Stage) ((Node) event.getSource()).getScene().getWindow());
                } else {
                    openUserView((Stage) ((Node)event.getSource()).getScene().getWindow());
                }
            } else {
                showErrorMessage("Wrong Username / Password!");
            }
        });
        loginView.getRegisterButton().setOnAction(event -> {
            openRegisterView((Stage)((Node)event.getSource()).getScene().getWindow());
        });
    }

    private void openLibrarianView(Stage stage) {
        MainMenuView menuView = new MainMenuView(stage);
    }

    private void openRegisterView(Stage stage) {
        RegisterView registerView = new RegisterView(stage);
    }

    private void openUserView(Stage stage) {
        UserView userView = new UserView(stage);
    }
}
