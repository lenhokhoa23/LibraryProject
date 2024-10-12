package org.example.javaapplicationproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.HashMap;

public class BookManagement {
    private static HashMap<String, Book> bookMapTitle = new HashMap<>();

    public String fetchISBNFromBooks(String bookTitle) {
        String isbn = null;
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT ISBN FROM books WHERE title = ?";

            // Tạo PreparedStatement
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, bookTitle); // Gán giá trị bookTitle vào câu truy vấn

            // Thực thi truy vấn
            rs = pstmt.executeQuery();
            if (rs.next()) {
                isbn = rs.getString("ISBN");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isbn;
    }

    public static void addBook(Book book) {
        String sql = "INSERT INTO books (title, author, pubdate, releaseDate, ISBN, price, subject, category, URL, bookType, quantity) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getPubdate());
            statement.setString(4, book.getReleaseDate());
            statement.setString(5, book.getISBN());
            statement.setString(6, book.getPrice());
            statement.setString(7, book.getSubject());
            statement.setString(8, book.getCategory());
            statement.setString(9, book.getURL());
            statement.setString(10, book.getBookType());
            statement.setString(11, book.getQuantity());

            statement.executeUpdate();
            System.out.println("Thêm sách thành công!");

        } catch (SQLException e) {
            System.out.println("Lỗi khi thêm sách!");
            e.printStackTrace();
        }
    }

    /** This function xoá sách ở database. */
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

    /** This function cải thiện tìm sách. */
    public static void findBookByTitleInMemory(String title) {
        Book book = bookMapTitle.get(title);
        if (book == null) {
            System.out.println("Không tìm thấy sách có tựa đề " + title);
        } else {
            book.printInfoBook();
        }
    }

    /** This function tìm sách dựa trên thể loại. */
    public static void findBookByCategory(String category) {
        String sql = "SELECT * FROM books WHERE category = ?";
        try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category);
            ResultSet resultSet = statement.executeQuery();
            boolean flag = true;
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
                flag = false;
                book.printInfoBook();
            }
            if (flag) {
                System.out.println("Không tìm được cuốn sách nào như vậy cả!");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** This function tìm sách dựa trên tác giả. */
    public static void findBookByAuthor(String author) {
        String sql = "SELECT * FROM books WHERE author = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, author);
            ResultSet resultSet = statement.executeQuery();
            boolean flag = true;
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
                flag = false;
                book.printInfoBook();
            }
            if (flag) {
                System.out.println("Không tìm được cuốn sách nào như vậy cả!");
                return;
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
            boolean flag = true;
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
                flag = false;
                book.printInfoBook();
            }
            if (flag) {
                System.out.println("Không tìm được cuốn sách nào như vậy cả!");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** This function return a string that contains infomations about the status
     * books that have the same name. */
    public static String getBooksStatus(String bookTitle) {
        StringBuilder statusBuilder = new StringBuilder();
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Truy vấn để lấy số lượng sách cùng tên và trạng thái của từng quyển sách
            String query = "SELECT Cart_ID, endDate FROM cart WHERE title = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, bookTitle);
            ResultSet rs = stmt.executeQuery();

            int count = 0;
            while (rs.next()) {
                count++;
                int cartId = rs.getInt("Cart_ID");
                String status = CartManagement.getBookStatus(cartId); // Gọi hàm lấy trạng thái
                statusBuilder.append("Cart ID: ").append(cartId)
                        .append(" - ").append(status).append("\n");
            }

            // Thêm thông tin số lượng sách vào kết quả
            statusBuilder.insert(0, "Số lượng sách \"" + bookTitle + "\": " + count + "\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statusBuilder.toString();
    }

    public int fetchQuantityFromBooks(String bookTitle) {
        int quantity = -1;
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String query = "SELECT quantity FROM books WHERE title = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, bookTitle);

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

    public static void updateQuantity(String bookName, String operation) {
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
}
