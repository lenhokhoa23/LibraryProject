package org.example.javaapplicationproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserManagement {
    public static void updateBorrowedBooks(String username, int bookChange) {
        String sql = "UPDATE user SET borrowedBooks = borrowedBooks + ? WHERE username = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, bookChange);
            statement.setString(2, username);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Cập nhật số lượng sách mượn thành công cho người dùng có username: " + username);
            } else {
                System.out.println("Không tìm thấy người dùng với username: " + username);
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi cập nhật số lượng sách mượn!");
            e.printStackTrace();
        }
    }

}
