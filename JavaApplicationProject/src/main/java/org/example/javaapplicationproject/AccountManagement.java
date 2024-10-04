package org.example.javaapplicationproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccountManagement {
    public static void addAccount(Account account) {
        String sql = "INSERT INTO accounts (username, password)" + "VALUES(?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());
            statement.executeUpdate();
            System.out.println("Tạo tài khoản thành công!");
        } catch (SQLException e) {
            System.out.println("Lỗi khi thêm tài khoản!");
            e.printStackTrace();
        }
    }
}
