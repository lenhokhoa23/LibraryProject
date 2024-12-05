package org.example.libraryfxproject.Service;

import org.example.libraryfxproject.Dao.AccountDAO;
import org.example.libraryfxproject.Dao.CartDAO;
import org.example.libraryfxproject.Dao.UserDAO;
import org.example.libraryfxproject.Model.Librarian;
import org.example.libraryfxproject.Model.User;

import java.lang.reflect.AccessFlag;

/** Cung cấp các dịch vụ chung cho thao tác với user. */
public class UserService {
    private UserDAO userDAO;
    private AccountDAO accountDAO;
    private static UserService userService;

    private UserService() {
        userDAO = UserDAO.getInstance();
        accountDAO = AccountDAO.getInstance();
        LoadService.loadData(userDAO);
        LoadService.loadData(accountDAO);

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

    public void deleteUser(String usernameToDelete) {
        userDAO.deleteUserForNextRun(usernameToDelete);
    }

    public boolean hasIDInUser(int ID) {
        String username = userDAO.getUsernameByCartId(ID);
        return username != null;
    }

    public void modifyStudent(String ID, String attribute, String newValue) {
        UserDAO.modifyUserByAttribute(Integer.parseInt(ID), attribute, newValue);
    }
}
