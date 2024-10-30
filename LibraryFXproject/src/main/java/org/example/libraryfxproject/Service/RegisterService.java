package org.example.libraryfxproject.Service;

import org.example.libraryfxproject.Dao.AccountDAO;
import org.example.libraryfxproject.Model.Account;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterService {
    AccountDAO accountDAO = new AccountDAO();

    public RegisterService() {
        LoadService loadService = new LoadService();
        loadService.loadData(accountDAO);
    }

    public int validateInput(String username, String phoneNumber, String email) {
        String usernamePattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*_\\S]{8,20}$";
        Pattern patternUsername = Pattern.compile(usernamePattern);
        Matcher matcherUsername = patternUsername.matcher(username);

        if (!matcherUsername.matches()) {
            return 1;
        }

        String phonePattern = "^0\\d{9,}$";
        Pattern patternPhone = Pattern.compile(phonePattern);
        Matcher matcherPhone = patternPhone.matcher(phoneNumber);

        if (!matcherPhone.matches()) {
            return 2;
        }

        String emailPattern = "^[A-Za-z0-9._%+-]+@gmail\\.com$";
        Pattern patternEmail = Pattern.compile(emailPattern);
        Matcher matcherEmail = patternEmail.matcher(email);

        if (!matcherEmail.matches()) {
            return 3;
        }

        Account account = accountDAO.getAccountByUsername(username);
        if (account != null) {
            return 4;
        }
        return 0;
    }
}
