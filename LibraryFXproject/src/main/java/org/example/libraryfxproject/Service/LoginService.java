package org.example.libraryfxproject.Service;

import org.example.libraryfxproject.Dao.AccountDAO;
import org.example.libraryfxproject.Dao.LibrarianDAO;
import org.example.libraryfxproject.Dao.UserDAO;
import org.example.libraryfxproject.Model.Account;
import org.example.libraryfxproject.Model.Librarian;
import org.example.libraryfxproject.Model.User;

public class LoginService {
    private AccountDAO accountDAO;
    private LibrarianDAO librarianDAO;
    private UserDAO userDAO;
    private static LoginService loginService;

    public static synchronized LoginService getInstance() {
        if (loginService == null) {
            loginService = new LoginService();
        }
        return loginService;
    }

    private LoginService() {
        accountDAO = AccountDAO.getInstance();
        librarianDAO = LibrarianDAO.getInstance();
        userDAO = UserDAO.getInstance();
        LoadService.loadData(accountDAO);
        for (String key : accountDAO.getDataMap().keySet()) {
            System.out.println(key);
        }
        LoadService.loadData(librarianDAO);
        LoadService.loadData(userDAO);
    }

    /**
     * Xác thực người dùng dựa trên tên đăng nhập và mật khẩu.
     * @param username Tên đăng nhập
     * @param password Mật khẩu
     * @return 0 nếu là admin, 1 nếu là người dùng, -1 nếu không tìm thấy tài khoản hoặc mật khẩu không đúng
     */
    public int authenticate(String username, String password) {
        Account account = accountDAO.getAccountByUsername(username);
        if (account == null) {
            return -1; // Không tìm thấy tài khoản
        } else if (!account.getPassword().equals(password)) {
            return -1; // Mật khẩu sai
        } else if (account.getRole().equals("admin")) {
            return 0; // Tài khoản admin
        } else {
            return 1; // Tài khoản người dùng
        }
    }

    /**
     * Tìm kiếm thủ thư theo tên đăng nhập.
     * @param username Tên đăng nhập của thủ thư
     * @return Thủ thư nếu tìm thấy, null nếu không
     */
    public Librarian findLibrarianByUsername(String username) {
        return librarianDAO.findLibrarian(username, 2);
    }

    /**
     * Tìm kiếm người dùng theo tên đăng nhập.
     * @param username Tên đăng nhập của người dùng
     * @return Người dùng nếu tìm thấy, null nếu không
     */
    public User findUserByUsername(String username) {
        return userDAO.findUserByUsername(username);
    }
}
