package org.example.libraryfxproject.View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.libraryfxproject.Controller.RegisterController;
import org.example.libraryfxproject.Util.AlertDisplayer;
import org.example.libraryfxproject.Util.JavaFXAlertDisplayer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterView implements Initializable {

    /** FXML buttons declaration with getters and setters. */
    @FXML
    private TextField name;
    @FXML
    private TextField email;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button registerButton;
    @FXML
    private Button backButton;

    private AlertDisplayer alertDisplayer;

    private final Stage stage;

    public TextField getName() {
        return name;
    }

    public void setName(TextField name) {
        this.name = name;
    }

    public TextField getEmail() {
        return email;
    }

    public void setEmail(TextField email) {
        this.email = email;
    }

    public TextField getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(TextField phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public TextField getUsername() {
        return username;
    }

    public void setUsername(TextField username) {
        this.username = username;
    }

    public PasswordField getPassword() {
        return password;
    }

    public void setPassword(PasswordField password) {
        this.password = password;
    }

    public Button getRegister() {
        return registerButton;
    }

    public void setRegister(Button registerButton) {
        this.registerButton = registerButton;
    }

    public Button getBackButton() {
        return backButton;
    }

    public void setBackButton(Button backButton) {
        this.backButton = backButton;
    }

    /**
     * Constructor khởi tạo đối tượng RegisterView.
     * Phương thức này nhận một đối tượng `Stage` để hiển thị giao diện và gọi phương thức
     * `initializeRegisterView()` để khởi tạo giao diện đăng kí tài khoản.
     * @param stage Đối tượng `Stage` dùng để hiển thị giao diện.
     */
    public RegisterView(Stage stage) {
        this.stage = stage;
        alertDisplayer = JavaFXAlertDisplayer.getInstance();
        initializeRegisterView();
    }

    /** Khởi tạo cửa sổ cho view đăng kí tài khoản.
     * Ném ra ngoại lệ nếu lỗi khi load view
     *
     */
    public void initializeRegisterView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/libraryfxproject/views/Register.fxml"));
        fxmlLoader.setController(this); // Đặt controller là đối tượng hiện tại
        try {
            Parent mainViewParent = fxmlLoader.load();
            Scene scene = new Scene(mainViewParent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Khởi tạo và đăng ký các sự kiện cho giao diện người.
     * Phương thức này thiết lập bộ điều khiển RegisterController và gọi phương thức
     * registerEvent() để đăng ký các sự kiện cho register.
     * @param url URL của tệp FXML.
     * @param resourceBundle Đối tượng chứa các tài nguyên được sử dụng trong FXML.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alertDisplayer = JavaFXAlertDisplayer.getInstance();
        RegisterController registerController = new RegisterController(this, alertDisplayer);
        registerController.registerEvent();
    }
}
