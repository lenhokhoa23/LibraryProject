package org.example.javaapplicationproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class AccountManagement {


    public static void addAccount(Account account) {
        String sql = "INSERT INTO accounts (username, password, role)" + "VALUES(?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());
            statement.setString(3, account.getRole());
            statement.executeUpdate();
            System.out.println("Tạo tài khoản thành công!");
        } catch (SQLException e) {
            System.out.println("Lỗi khi thêm tài khoản!");
            e.printStackTrace();
        }
    }
    public static void deleteAccount(String username) {
        String sql = "DELETE FROM accounts WHERE username = ?";
        try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            int rowAffected = statement.executeUpdate();
            if (rowAffected > 0) {
                System.out.println("Xoá tài khoản thành công!");
            } else {
                System.out.println("Không tồn tại tài khoản này!");
            }
        } catch (SQLException e) {
            System.out.println("Đã xảy ra lỗi khi xoá tài khoản!");
            e.printStackTrace();
        }
    }

}
