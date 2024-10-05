package org.example.javaapplicationproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.HashMap;



public class BookManagement {
    private static HashMap<String, Book> bookMapTitle = new HashMap<>();

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
    public static void loadBooksIntoMemory() {
        String sql = "SELECT * FROM books";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Book book = new Book(
                        resultSet.getInt("no"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("pubdate"),
                        resultSet.getString("releaseDate"),
                        resultSet.getString("ISBN"),
                        resultSet.getString("price"),
                        resultSet.getString("subject"),
                        resultSet.getString("category"),
                        resultSet.getString("URL"),
                        resultSet.getString("bookType"),
                        resultSet.getString("quantity")
                );
                // Thêm sách vào HashMap
                bookMapTitle.put(book.getTitle(), book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void findBookByTitleInMemory(String title) {
        Book book = bookMapTitle.get(title);
        if (book == null) {
            System.out.println("Không tìm thấy sách có tựa đề " + title);
        } else {
            book.printInfoBook();
        }
    }
}
