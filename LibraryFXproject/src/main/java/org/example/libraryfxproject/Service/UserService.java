package org.example.libraryfxproject.Service;

import org.example.libraryfxproject.Dao.AccountDAO;
import org.example.libraryfxproject.Dao.CartDAO;
import org.example.libraryfxproject.Dao.UserDAO;

import java.lang.reflect.AccessFlag;

public class UserService {
    private final UserDAO userDAO = new UserDAO();
    private final AccountDAO accountDAO = new AccountDAO();
    private static UserService userService;

    private UserService() {
        LoadService loadService = LoadService.getInstance();
        loadService.loadData(userDAO);
        loadService.loadData(accountDAO);
    }
    public static synchronized UserService getInstance() {
        if (userService == null) {
            userService = new UserService();
        }
        return userService;
    }

    public AccountDAO getAccountDAO() {
        return accountDAO;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }
}
