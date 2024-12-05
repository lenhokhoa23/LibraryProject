package org.example.libraryfxproject.Service;

import org.example.libraryfxproject.Dao.AccountDAO;
import org.example.libraryfxproject.Model.Account;

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

    /**
     * Kiểm tra tính hợp lệ của thông tin đầu vào.
     * @param username Tên người dùng
     * @param phoneNumber Số điện thoại
     * @param email Địa chỉ email
     * @return Mã lỗi (1: Tên người dùng không hợp lệ, 2: Số điện thoại không hợp lệ,
     *         3: Email không hợp lệ, 4: Tên người dùng đã tồn tại, 0: Thành công)
     */
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
