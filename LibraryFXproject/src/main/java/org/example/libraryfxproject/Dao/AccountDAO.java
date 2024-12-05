package org.example.libraryfxproject.Dao;

import org.example.libraryfxproject.Model.Account;
import org.example.libraryfxproject.Service.LoadService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Lớp này chịu trách nhiệm quản lý các hoạt động với bảng tài khoản trong cơ sở dữ liệu.
 * Nó thực hiện các chức năng như tải dữ liệu tài khoản từ cơ sở dữ liệu và truy vấn tài khoản theo tên người dùng.
 */
public class AccountDAO extends GeneralDAO<String, Account> {
    private static AccountDAO accountDAO;

    private AccountDAO() {
    }

    public static synchronized AccountDAO getInstance() {
        if (accountDAO == null) {
            accountDAO = new AccountDAO();
        }
        return accountDAO;
    }

    /**
     * Tải tất cả các tài khoản từ cơ sở dữ liệu và lưu trữ vào bộ nhớ (dataMap).
     * Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu, lỗi sẽ được in ra màn hình.
     */
    @Override
    public void loadData() {
        String sql = "SELECT * FROM accounts";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Account account = new Account(resultSet.getString(1),
                        resultSet.getString(2), resultSet.getString(3));
                dataMap.put(account.getUsername(), account);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi load user memory");
            e.printStackTrace();
        }
    }

    /**
     * Lấy tài khoản theo tên người dùng.
     *
     * @param username tên người dùng của tài khoản cần tìm.
     * @return tài khoản tương ứng với tên người dùng hoặc null nếu không tìm thấy.
     */
    public Account getAccountByUsername(String username) {
        return dataMap.get(username);
    }
}
