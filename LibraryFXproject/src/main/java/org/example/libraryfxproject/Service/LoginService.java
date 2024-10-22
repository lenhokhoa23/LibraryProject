package org.example.libraryfxproject.Service;

import org.example.libraryfxproject.Dao.AccountDAO;
import org.example.libraryfxproject.Dao.UserDAO;
import org.example.libraryfxproject.Model.Account;
import org.example.libraryfxproject.Model.User;

public class LoginService {
    private final AccountDAO accountDAO = new AccountDAO();

    public LoginService() {
        LoadService loadService = new LoadService();
        loadService.loadData(accountDAO);
    }

    public boolean authenticate(String username, String password) {
        Account account = accountDAO.getAccountByUsername(username);
        if (account == null) {
            return false;
        } else {
            return password.equals(account.getPassword());
        }
    }
}
