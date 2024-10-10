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

    public static void findBookByCategory(String category) {
        String sql = "SELECT * FROM books WHERE category = ?";
        try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                System.out.println("Không tìm được cuốn sách nào như vậy cả!");
                return;
            }
            while(resultSet.next()) {
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
                book.printInfoBook();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void findBookByAuthor(String author) {
        String sql = "SELECT * FROM books WHERE author = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, author);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                System.out.println("Không tìm được cuốn sách nào như vậy cả!");
                return;
            }
            while(resultSet.next()) {
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
                book.printInfoBook();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void findBookByComponentOfName(String component) {
        String sql = "SELECT * FROM books WHERE title LIKE ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + component + "%");

            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                System.out.println("Không tìm được cuốn sách nào như vậy cả!");
                return;
            }
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
                book.printInfoBook();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String fetchISBNFromBooks(String bookTitle) {
        String isbn = null;
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT ISBN FROM books WHERE title = ?";

            statement = connection.prepareStatement(query);
            statement.setString(1, bookTitle);

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

    public int fetchQuantityFromBooks(String bookName) {
        int quantity = -1;
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String query = "SELECT quantity FROM books WHERE title = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, bookName);

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                quantity = resultSet.getInt("quantity");
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
        return quantity;
    }

    public void updateQuantity(String bookName, String operation) {
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        String updateQuery = "";

        try {
            connection = DatabaseConnection.getConnection();
            if (operation.equals("BORROW")) {
                updateQuery = "UPDATE books SET quantity = quantity - 1 WHERE title = ?";
            } else {
                updateQuery = "UPDATE books SET quantity = quantity + 1 WHERE title = ?";
            }

            statement = connection.prepareStatement(updateQuery);
            statement.setString(1, bookName);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Đã cập nhật số lượng sách thành công.");
            } else {
                System.out.println("Không tìm thấy sách với tiêu đề: " + bookName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteFromCart(String isbn, int cartId) {
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;

        try {
            String query = "DELETE FROM cart WHERE ISBN = ? AND Cart_ID = ?";

            statement = connection.prepareStatement(query);
            statement.setString(1, isbn);
            statement.setInt(2, cartId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Xóa thành công sách có ISBN " + isbn + " khỏi giỏ hàng với Cart_ID: " + cartId);
            } else {
                System.out.println("Không tìm thấy sách với ISBN " + isbn + " và Cart_ID " + cartId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
