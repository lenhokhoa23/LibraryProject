package org.example.libraryfxproject.Service;

import org.example.libraryfxproject.Dao.AccountDAO;
import org.example.libraryfxproject.Dao.LibrarianDAO;
import org.example.libraryfxproject.Dao.UserDAO;
import org.example.libraryfxproject.Model.Account;
import org.example.libraryfxproject.Model.Librarian;
import org.example.libraryfxproject.Model.User;

public class LoginService {
    private final AccountDAO accountDAO = new AccountDAO();
    private final LibrarianDAO librarianDAO = new LibrarianDAO();
    private final UserDAO userDAO = new UserDAO();
    private static LoginService loginService;
    public static synchronized LoginService getInstance() {
        if (loginService == null) {
            loginService = new LoginService();
        }
        return loginService;
    }

    private LoginService() {
        LoadService.loadData(accountDAO);
    }

    public int authenticate(String username, String password) {
        Account account = accountDAO.getAccountByUsername(username);
        if (account == null) {
            return - 1;
        } else if (!account.getPassword().equals(password)){
            return - 1;
        } else if (account.getRole().equals("admin")) {
            return 0;
        } else {
            return 1;
        }
    }

    public Librarian findLibrarianByUsername(String username) {
        return librarianDAO.findLibrarian(username, 2);
    }

    public User findUserByUsername(String username) {
        return userDAO.findUserByUsername(username);
    }
}
