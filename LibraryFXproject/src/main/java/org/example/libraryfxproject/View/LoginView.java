package org.example.libraryfxproject.View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
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
            //stage.setResizable(false);
            // Cài đặt thuộc tính cho username và password
            username.setPromptText("Username");
            password.setPromptText("Password");

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

    // Hiển thị thông báo thành công.
    public void showSuccessMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Message");
        alert.setHeaderText(null);
        alert.setContentText("Successfully login!");
        alert.show();
    }

    // Hiển thị thông báo cần điền thêm.
    public void showErrorMessFill() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText("Please fill all blank fields!");
        alert.show();
    }

    public void showErrorMessWrong() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText("Wrong Username / Password!");
        alert.show();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
