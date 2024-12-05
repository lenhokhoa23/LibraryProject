package org.example.libraryfxproject.Controller;

import javafx.scene.Node;
import javafx.stage.Stage;
import org.example.libraryfxproject.Dao.UserDAO;
import org.example.libraryfxproject.Service.RegisterService;
import org.example.libraryfxproject.Service.UpdateService;
import org.example.libraryfxproject.Util.AlertDisplayer;
import org.example.libraryfxproject.View.LoginView;
import org.example.libraryfxproject.View.RegisterView;

/**
 * RegisterController xử lý các sự kiện liên quan đến việc đăng ký tài khoản người dùng.
 * Controller này quản lý các sự kiện như xác thực thông tin đăng ký, hiển thị thông báo lỗi hoặc thành công,
 * và chuyển hướng người dùng đến màn hình đăng nhập sau khi hoàn tất đăng ký.
 */
public class RegisterController extends BaseController {
    private final RegisterView registerView;
    private final RegisterService registerService;
    private final UserDAO userDAO;
    private final UpdateService updateService;

    /**
     * Constructor khởi tạo RegisterController.
     *
     * @param registerView     View đăng ký tài khoản
     * @param alertDisplayer   Lớp hiển thị thông báo
     */
    public RegisterController(RegisterView registerView, AlertDisplayer alertDisplayer) {
        super(alertDisplayer);
        updateService = UpdateService.getInstance();
        registerService = RegisterService.getInstance();
        userDAO = UserDAO.getInstance();
        this.registerView = registerView;
    }

    /**
     * Đăng ký các sự kiện cho các nút trên màn hình đăng ký.
     * Bao gồm các hành động: quay lại màn hình đăng nhập và đăng ký tài khoản mới.
     */
    public void registerEvent() {
        // Sự kiện quay lại màn hình đăng nhập
        registerView.getBackButton().setOnAction(event -> {
            openLoginView((Stage)((Node)event.getSource()).getScene().getWindow());
        });

        // Sự kiện đăng ký tài khoản mới
        registerView.getRegister().setOnAction(event -> {
            String name = registerView.getName().getText();
            String email = registerView.getEmail().getText();
            String phoneNumber = registerView.getPhoneNumber().getText();
            String username = registerView.getUsername().getText();
            String password = registerView.getPassword().getText();

            // Kiểm tra thông tin nhập vào và tiến hành đăng ký
            if (!name.isEmpty() && !email.isEmpty() && !phoneNumber.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
                // Kiểm tra tính hợp lệ của thông tin nhập vào
                if (registerService.validateInput(username, phoneNumber, email) == 0) {
                    showSuccessMessage("Đăng kí tài khoản thành công!");
                    userDAO.saveUserToDatabase(name, email, phoneNumber, username, password, "Basic");
                    updateService.updateAccountDAO();
                    updateService.updateUserDAO();
                    openLoginView((Stage) ((Node) event.getSource()).getScene().getWindow());
                } else if (registerService.validateInput(username, phoneNumber, email) == 1) {
                    showErrorMessage("Username must not contain spaces, must be 8-20 characters long, including letters and numbers!");
                } else if (registerService.validateInput(username, phoneNumber, email) == 2) {
                    showErrorMessage("Phone number must be a numeric sequence, start with 0, and be at least 10 characters long!");
                } else if (registerService.validateInput(username, phoneNumber, email) == 3) {
                    showErrorMessage("Email invalid or disposable email detected!");
                } else if (registerService.validateInput(username, phoneNumber, email) == 4) {
                    showErrorMessage("This username has already been taken, please use another username!");
                }
            } else {
                showErrorMessage("Please fill all blank fields!");
            }
        });
    }

    /**
     * Mở màn hình đăng nhập.
     *
     * @param stage Stage của ứng dụng
     */
    private void openLoginView(Stage stage) {
        LoginView loginView = new LoginView(stage);
    }
}
