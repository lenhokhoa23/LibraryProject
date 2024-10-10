package org.example.javaapplicationproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CartManagement {
    public void addCart(Cart cart) {
        String sql = "INSERT INTO cart (Cart_ID, startDate, endDate, title, ISBN) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cart.getCart_ID());
            statement.setString(2, cart.getStartDate());
            statement.setString(3, cart.getEndDate());
            statement.setString(4, cart.getTitle());
            statement.setString(5, cart.getISBN());
            statement.executeUpdate();
            System.out.println("Thêm sách vào giỏ hàng thành công!");

        } catch (SQLException e) {
            System.out.println("Lỗi khi thêm sách vào giỏ hàng!");
            e.printStackTrace();
        }
    }

    public String fetchISBNFromCart(String bookTitle, String username) {
        String isbn = null;
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String query = "SELECT ISBN FROM cart WHERE title = ? AND username = ?";

            statement = connection.prepareStatement(query);
            statement.setString(1, bookTitle);
            statement.setString(2, username);

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                isbn = resultSet.getString("ISBN");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isbn;
    }

}
