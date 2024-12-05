package org.example.libraryfxproject.Service;

import org.example.libraryfxproject.Dao.AccountDAO;
import org.example.libraryfxproject.Model.Account;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.example.libraryfxproject.Util.ValidationUtils.*;

public class RegisterService {
    private AccountDAO accountDAO;
    private static RegisterService registerService;

    private RegisterService() {
        accountDAO = AccountDAO.getInstance();
        LoadService.loadData(accountDAO);
    }

    public static synchronized RegisterService getInstance() {
        if (registerService == null) {
            registerService = new RegisterService();
        }
        return registerService;
    }

    public AccountDAO getAccountDAO() {
        return accountDAO;
    }

    public void refreshAccountDAO() {
        LoadService.loadData(accountDAO);
    }

    public int validateInput(String username, String phoneNumber, String email) {
        if (!isValidUsername(username)) {
            return 1;
        }

        if (!isValidPhoneNumber(phoneNumber)) {
            return 2;
        }

        if (!isValidEmail(email)) {
            return 3;
        }

        Account account = accountDAO.getAccountByUsername(username);
        if (account != null) {
            return 4;
        }
        return 0;
    }
}
