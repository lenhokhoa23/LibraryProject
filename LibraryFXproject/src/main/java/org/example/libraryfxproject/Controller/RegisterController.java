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
                    RegisterView.showAlert("Username không được có dấu cách, phải chứa 8-20 ký tự, bao gồm cả chữ cái và số!");
                } else if (registerService.validateInput(username, phoneNumber, email) == 2) {
                    RegisterView.showAlert("Số điện thoại phải là dãy số, bắt đầu với số 0 và có ít nhất 10 ký tự!");
                } else if (registerService.validateInput(username, phoneNumber, email) == 3) {
                    RegisterView.showAlert("Email phải có định dạng hợp lệ và đuôi là @gmail.com!");
                } else if (registerService.validateInput(username, phoneNumber, email) == 4) {
                    RegisterView.showAlert("Username này đã được sử dụng, vui lòng sử dụng một username khác!");
                }
            } else {
                this.registerView.showErrorMessFill();
                System.out.println("Vui lòng nhập đầy đủ thông tin!");
            }
        });
    }

    private void openLoginView(Stage stage) {
        LoginView loginView = new LoginView(stage);
    }
}
