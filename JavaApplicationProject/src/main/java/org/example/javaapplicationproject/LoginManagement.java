package org.example.javaapplicationproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginManagement {
    public static void login(String username, String password) {
        String sql = "SELECT * FROM accounts WHERE username = ? AND password = ?";
        try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String role = resultSet.getString("role");
                System.out.println("Đăng nhập thành công với vai trò " + role);
                if (role.equals("admin")) {
                    Menu.showAdminMenu();
                } else {
                    Menu.showUserMenu();
                }
            } else {
                System.out.println("Sai tên đăng nhập hoặc mật khẩu!");
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi đăng nhập!");
            e.printStackTrace();
        }
    }
}
