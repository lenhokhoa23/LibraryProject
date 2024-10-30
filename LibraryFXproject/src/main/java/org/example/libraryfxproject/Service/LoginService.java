package org.example.libraryfxproject.Service;

import org.example.libraryfxproject.Dao.AccountDAO;
import org.example.libraryfxproject.Dao.UserDAO;
import org.example.libraryfxproject.Model.Account;
import org.example.libraryfxproject.Model.User;

public class LoginService {
    private final AccountDAO accountDAO = new AccountDAO();

    public LoginService() {
        LoadService loadService = new LoadService();
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


}
