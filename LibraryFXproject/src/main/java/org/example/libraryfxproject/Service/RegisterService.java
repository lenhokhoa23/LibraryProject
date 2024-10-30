package org.example.libraryfxproject.Service;

import javafx.scene.control.Alert;
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

    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi nhập liệu");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public boolean validateInput(String username, String phoneNumber, String email) {
        String usernamePattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        Pattern patternUsername = Pattern.compile(usernamePattern);
        Matcher matcherUsername = patternUsername.matcher(username);

        if (!matcherUsername.matches()) {
            showAlert("Username phải chứa ít nhất 8 ký tự, bao gồm cả chữ cái và số.");
            return false;
        }

        String phonePattern = "^0\\d{9,}$";
        Pattern patternPhone = Pattern.compile(phonePattern);
        Matcher matcherPhone = patternPhone.matcher(phoneNumber);

        if (!matcherPhone.matches()) {
            showAlert("Số điện thoại phải là dãy số, bắt đầu với số 0 và có ít nhất 10 ký tự.");
            return false;
        }

        String emailPattern = "^[A-Za-z0-9._%+-]+@gmail\\.com$";
        Pattern patternEmail = Pattern.compile(emailPattern);
        Matcher matcherEmail = patternEmail.matcher(email);

        if (!matcherEmail.matches()) {
            showAlert("Email phải có định dạng hợp lệ và đuôi là @gmail.com.");
            return false;
        }

        return true;
    }
}
