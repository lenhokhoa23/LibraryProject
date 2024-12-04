package org.example.libraryfxproject.Service;

import org.example.libraryfxproject.Dao.AccountDAO;
import org.example.libraryfxproject.Dao.LibrarianDAO;
import org.example.libraryfxproject.Dao.UserDAO;
import org.example.libraryfxproject.Model.Account;
import org.example.libraryfxproject.Model.Librarian;
import org.example.libraryfxproject.Model.User;

import java.security.Key;

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

    public void setAccountDAO(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;

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
