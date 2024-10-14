package org.example.libraryfxproject.View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.libraryfxproject.Controller.LoginController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginView implements Initializable {
    private final Stage stage;

    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;

    // Constructor
    public LoginView(Stage stage) {
        this.stage = stage;
        initializeLoginView();
    }

    // Khởi tạo giao diện
    private void initializeLoginView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/libraryfxproject/Login.fxml"));
            fxmlLoader.setController(this); // Đặt controller là đối tượng hiện tại (LoginView)
            Parent loginViewParent = fxmlLoader.load();
            Scene scene = new Scene(loginViewParent);
            stage.setTitle("Thư viện LICVNU");
            stage.setScene(scene);

            // Cài đặt thuộc tính cho username và password
            username.setPromptText("Username");
            password.setPromptText("Password");

            // Thêm CSS
            scene.getStylesheets().add(getClass().getResource("/org/example/libraryfxproject/style.css").toExternalForm());
            stage.show();

            // Tạo LoginController và đăng ký các sự kiện điều khiển
            LoginController loginController = new LoginController(this);
            loginController.registerEvent();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getter cho các nút và text field
    public Button getLoginButton() {
        return loginButton;
    }

    public Button getRegisterButton() {
        return registerButton;
    }

    public TextField getUsername() {
        return username;
    }

    public TextField getPassword() {
        return password;
    }

    // Hiển thị thông báo thành công
    public void showSuccessMessage() {
        System.out.println("Đăng nhập thành công!");
    }

    // Hiển thị thông báo thất bại
    public void showErrorMessage() {
        System.out.println("Đăng nhập thất bại!");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
