package org.example.javaapplicationproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookManagement {
    public static void addBook(Book book) {
        String sql = "INSERT INTO books (no, title, author, pubdate, releaseDate, ISBN, price, subject, category, URL, bookType, quantity) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, book.getNo());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setString(4, book.getPubdate());
            statement.setString(5, book.getReleaseDate());
            statement.setString(6, book.getISBN());
            statement.setString(7, book.getPrice());
            statement.setString(8, book.getSubject());
            statement.setString(9, book.getCategory());
            statement.setString(10, book.getURL());
            statement.setString(11, book.getBookType());
            statement.setString(12, book.getQuantity());

            statement.executeUpdate();
            System.out.println("Thêm sách thành công!");

        } catch (SQLException e) {
            System.out.println("Lỗi khi thêm sách!");
            e.printStackTrace();
        }
    }
    public static void deleteBook (String title) {
        String sql = "DELETE FROM books where title = ?";
        try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, title);
            int rowAffected = statement.executeUpdate();
            if (rowAffected > 0) {
                System.out.println("Đã xoá thành công cuốn sách này!");
            } else {
                System.out.println("Không tồn tại cuốn sách này!");
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi xoá sách");
            e.printStackTrace();
        }
    }
}
