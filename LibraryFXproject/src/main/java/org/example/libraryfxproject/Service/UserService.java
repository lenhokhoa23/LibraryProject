package org.example.libraryfxproject.Service;

import org.example.libraryfxproject.Dao.CartDAO;
import org.example.libraryfxproject.Dao.UserDAO;

public class UserService {
    private final UserDAO userDAO = new UserDAO();
    private static UserService userService;

    public UserDAO getUserDAO() {
        return userDAO;
    }

    private UserService() {
        LoadService loadService = LoadService.getInstance();
        loadService.loadData(userDAO);
    }
    public static synchronized UserService getInstance() {
        if (userService == null) {
            userService = new UserService();
        }
        return userService;
    }

}
