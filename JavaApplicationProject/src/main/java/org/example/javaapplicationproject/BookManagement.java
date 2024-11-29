package org.example.javaapplicationproject;

import java.sql.*;

import java.time.LocalDate;
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

    public static void modifyBookAttribute(String ISBN, String attribute, String newValue) {
        String sql = "UPDATE books SET " + attribute + " = ? WHERE ISBN = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, newValue);
            statement.setString(2, ISBN);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Cập nhật thành công thuộc tính " + attribute + " cho sách có ISBN: " + ISBN);
            } else {
                System.out.println("Không tìm thấy sách với ISBN: " + ISBN);
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi cập nhật thuộc tính của sách!");
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
            System.out.println("Lỗi khi tải database");
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
            // Kiểm tra sự tồn tại của sách trong kho
            String query1 = "SELECT COUNT(*) AS count FROM books WHERE title = ?";
            PreparedStatement stmt1 = conn.prepareStatement(query1);
            stmt1.setString(1, bookTitle);
            ResultSet rs1 = stmt1.executeQuery();

            if (rs1.next() && rs1.getInt("count") == 0) {
                return "Không tìm thấy sách \"" + bookTitle + "\" trong kho.";
            }

            // Lấy tổng số sách còn lại trong kho từ bảng books
            String query2 = "SELECT SUM(quantity) AS remain FROM books WHERE title = ?";
            PreparedStatement stmt2 = conn.prepareStatement(query2);
            stmt2.setString(1, bookTitle);
            ResultSet rs2 = stmt2.executeQuery();

            int remainBooks = 0;
            if (rs2.next()) {
                remainBooks = rs2.getInt("remain");
            }

            // Lấy thông tin sách đã được mượn từ bảng cart
            String query3 = "SELECT isbn, title, Cart_ID, endDate FROM cart WHERE title = ?";
            PreparedStatement stmt3 = conn.prepareStatement(query3);
            stmt3.setString(1, bookTitle);
            ResultSet rs3 = stmt3.executeQuery();

            int borrowedBooks = 0;
            while (rs3.next()) {
                borrowedBooks++;
                String isbn = rs3.getString("isbn");
                String title = rs3.getString("title");
                int cartId = rs3.getInt("Cart_ID");
                Date endDate = rs3.getDate("endDate");
                LocalDate eventDate = endDate.toLocalDate();
                LocalDate currentDate = LocalDate.now();
                // Kiểm tra trạng thái sách
                String status = currentDate.isBefore(eventDate)
                        ? "\033[32mCòn hạn\033[0m"
                        : "\033[31mQuá hạn\033[0m";
                statusBuilder.append("Cart ID: ").append(cartId)
                        .append("\n ISBN: ").append(isbn)
                        .append("\n Sách \"").append(title)
                        .append("\"\nHạn: ").append(String.valueOf(endDate))
                        .append("Trạng thái: " + status)
                        .append("\n*------------------------------------*\n");
            }

            // Tính tổng số lượng sách
            int totalBooks = remainBooks + borrowedBooks;

            // Xây dựng kết quả
            statusBuilder.insert(0, "Tổng số sách \"" + bookTitle + "\": " + totalBooks + "\n");
            statusBuilder.append("Tổng số sách đã mượn: ").append(borrowedBooks).append("\n")
                    .append("Tổng số sách còn trống: ").append(remainBooks).append("\n");

            // Nếu không có sách nào được mượn
            if (borrowedBooks == 0) {
                statusBuilder.append("Hiện không có sách nào được mượn.\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Đã xảy ra lỗi khi lấy thông tin sách.";
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

    public static void updateQuantity(String username, String bookName, String operation) {
        Connection connection = null;
        PreparedStatement statementBook = null;
        PreparedStatement statementUser = null;

        try {
            connection = DatabaseConnection.getConnection();
            String updateBookQuery;
            String updateUserQuery;

            if (operation.equals("BORROW")) {
                updateBookQuery = "UPDATE books SET quantity = quantity - 1 WHERE title = ?";
                updateUserQuery = "UPDATE user SET borrowedBooks = borrowedBooks + 1 WHERE username = ?";
            } else {
                updateBookQuery = "UPDATE books SET quantity = quantity + 1 WHERE title = ?";
                updateUserQuery = "UPDATE user SET borrowedBooks = borrowedBooks - 1 WHERE username = ?";
            }

            statementBook = connection.prepareStatement(updateBookQuery);
            statementBook.setString(1, bookName);
            int rowsUpdatedBook = statementBook.executeUpdate();

            statementUser = connection.prepareStatement(updateUserQuery);
            statementUser.setString(1, username);
            int rowsUpdatedUser = statementUser.executeUpdate();

            if (rowsUpdatedBook > 0 && rowsUpdatedUser > 0) {
                System.out.println("Đã cập nhật số lượng sách và số sách mượn thành công.");
            } else {
                System.out.println("Không tìm thấy sách với tiêu đề: " + bookName + " hoặc không tìm thấy người dùng: " + username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statementBook != null) statementBook.close();
                if (statementUser != null) statementUser.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
