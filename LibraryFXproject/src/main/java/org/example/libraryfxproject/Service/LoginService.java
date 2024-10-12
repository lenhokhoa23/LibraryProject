package org.example.libraryfxproject.Service;

import org.example.libraryfxproject.Dao.UserDAO;
import org.example.libraryfxproject.Model.User;

public class LoginService {
    private final UserDAO userDAO;

    public LoginService() {
        this.userDAO = new UserDAO();
    }

    public boolean authenticate(String username, String password) {

        return false;
    }
}
