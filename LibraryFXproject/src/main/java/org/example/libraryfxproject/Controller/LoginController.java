package org.example.libraryfxproject.Controller;

import javafx.scene.Node;
import javafx.stage.Stage;
import org.example.libraryfxproject.Service.LoginService;
import org.example.libraryfxproject.Util.AlertDisplayer;
import org.example.libraryfxproject.View.LoginView;
import org.example.libraryfxproject.View.MainMenuView;
import org.example.libraryfxproject.View.RegisterView;
import org.example.libraryfxproject.View.UserView;

/**
 * LoginController điều khiển các sự kiện liên quan đến màn hình đăng nhập.
 * Controller này xử lý các sự kiện đăng nhập của người dùng, bao gồm xác thực tài khoản,
 * mở các màn hình chính khác nhau (Librarian hoặc User) và chuyển hướng đến màn hình đăng ký.
 */
public class LoginController extends BaseController {
    private final LoginView loginView;
    private final LoginService loginService;

    /**
     * Constructor khởi tạo LoginController.
     *
     * @param loginView      View đăng nhập
     * @param alertDisplayer Lớp hiển thị thông báo
     */
    public LoginController(LoginView loginView, AlertDisplayer alertDisplayer) {
        super(alertDisplayer);
        this.loginView = loginView;
        this.loginService = LoginService.getInstance();
    }

    /**
     * Đăng ký các sự kiện cho các nút trên màn hình đăng nhập.
     */
    public void registerEvent() {
        loginView.getLoginButton().setOnAction(event -> {
            String username = loginView.getUsername().getText();
            String password = loginView.getPassword().getText();
            // Kiểm tra tài khoản và mật khẩu
            if (username.isEmpty() || password.isEmpty()) {
                showErrorMessage("Please fill all blank fields!");
            } else if (loginService.authenticate(username, password) > -1) {
                showSuccessMessage("Successfully login!");
                // Mở view khác nhau dựa trên vai trò
                if (loginService.authenticate(username, password) == 0) {
                    openLibrarianView((Stage) ((Node) event.getSource()).getScene().getWindow(), username);
                } else {
                    openUserView((Stage) ((Node)event.getSource()).getScene().getWindow(), username);
                }
            } else {
                showErrorMessage("Wrong Username / Password!");
            }
        });
        loginView.getRegisterButton().setOnAction(event -> {
            openRegisterView((Stage)((Node)event.getSource()).getScene().getWindow());
        });
    }

    /**
     * Mở màn hình chính cho thư viện (Librarian).
     *
     * @param stage   Stage của ứng dụng
     * @param username Tên người dùng
     */
    private void openLibrarianView(Stage stage, String username) {
        MainMenuView menuView = new MainMenuView(stage, username);
    }

    /**
     * Mở màn hình đăng ký.
     *
     * @param stage Stage của ứng dụng
     */
    private void openRegisterView(Stage stage) {
        RegisterView registerView = new RegisterView(stage);
    }

    /**
     * Mở màn hình cho người dùng.
     *
     * @param stage   Stage của ứng dụng
     * @param username Tên người dùng
     */
    private void openUserView(Stage stage, String username) {
        UserView userView = new UserView(stage, username);
        System.out.println(username);
    }
}
