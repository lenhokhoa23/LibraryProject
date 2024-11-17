package org.example.libraryfxproject.Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.libraryfxproject.Dao.AccountDAO;
import org.example.libraryfxproject.Dao.DatabaseConnection;
import org.example.libraryfxproject.Dao.UserDAO;
import org.example.libraryfxproject.Service.RegisterService;
import org.example.libraryfxproject.View.LoginView;
import org.example.libraryfxproject.View.RegisterView;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterController {
    private final RegisterView registerView;
    private final RegisterService registerService = new RegisterService();

    public RegisterController(RegisterView registerView) {
        this.registerView = registerView;
    }

    public void registerEvent() {
        registerView.getBackButton().setOnAction(event -> {
            openLoginView((Stage)((Node)event.getSource()).getScene().getWindow());
        });

        registerView.getRegister().setOnAction(event -> {
            String name = registerView.getName().getText();
            String email = registerView.getEmail().getText();
            String phoneNumber = registerView.getPhoneNumber().getText();
            String username = registerView.getUsername().getText();
            String password = registerView.getPassword().getText();

            if (!name.isEmpty() && !email.isEmpty() && !phoneNumber.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
                if (registerService.validateInput(username, phoneNumber, email) == 0) {
                    this.registerView.showSuccessMessage();
                    AccountDAO.saveUserToDatabase(name, email, phoneNumber, username, password);
                    openLoginView((Stage) ((Node) event.getSource()).getScene().getWindow());
                } else if (registerService.validateInput(username, phoneNumber, email) == 1) {
                    RegisterView.showAlert("Username must not contain spaces, must be 8-20 characters long, including letters and numbers!");
                } else if (registerService.validateInput(username, phoneNumber, email) == 2) {
                    RegisterView.showAlert("Phone number must be a numeric sequence, start with 0, and be at least 10 characters long!");
                } else if (registerService.validateInput(username, phoneNumber, email) == 3) {
                    RegisterView.showAlert("Email invalid or disposable email detected!");
                } else if (registerService.validateInput(username, phoneNumber, email) == 4) {
                    RegisterView.showAlert("This username has already been taken, please use another username!");
                }
            } else {
                this.registerView.showErrorMessFill();
                System.out.println("Please provide all required information!");
            }
        });
    }

    private void openLoginView(Stage stage) {
        LoginView loginView = new LoginView(stage);
    }
}
