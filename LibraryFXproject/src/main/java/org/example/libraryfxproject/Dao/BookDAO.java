package org.example.libraryfxproject.Dao;

import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import org.example.libraryfxproject.Model.Book;
import org.example.libraryfxproject.Model.Comment;
import org.example.libraryfxproject.Model.Trie;
import org.example.libraryfxproject.Model.TrieNode;
import org.example.libraryfxproject.Service.LoadService;
import org.example.libraryfxproject.Util.JavaFXAlertDisplayer;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.Date;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.concurrent.*;

public class BookDAO extends GeneralDAO<String, Book> {
    private Trie trie = new Trie();
    private TrieNode trieNode = new TrieNode();
    public static int totalQuantity = 0;

    public Trie getTrie() {
        return trie;
    }

    public void setTrie(Trie trie) {
        this.trie = trie;
    }

    public TrieNode getTrieNode() {
        return trieNode;
    }

    public void setTrieNode(TrieNode trieNode) {
        this.trieNode = trieNode;
    }

    public BookDAO() {
        LoadService.loadData(this);
    }

    public void insert(String word) {
        TrieNode current = trie.getRoot();
        for (char ch : word.toCharArray()) {
            current = current.getChildren().computeIfAbsent(ch, c -> new TrieNode());
        }
        current.setEndOfWord(true);
    }

    @Override
    public void loadData() {
        totalQuantity = 0;  // Reset tổng số lượng sách khi load lại
        String sql = "SELECT * FROM books";
        try (PreparedStatement statement = connection.prepareStatement(sql);
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

                dataMap.put(book.getTitle(), book);
                insert(book.getTitle());

                try {
                    int quantity = Integer.parseInt(book.getQuantity());
                    totalQuantity += quantity;
                } catch (NumberFormatException e) {
                    JavaFXAlertDisplayer.getInstance().showErrorAlert("Error","Không thể chuyển đổi quantity thành số cho sách: " + book.getTitle());
                }
            }
        } catch (SQLException e) {
            JavaFXAlertDisplayer.getInstance().showErrorAlert("Error", "Load book Error");
        }
    }

    @Override
    public HashMap<String, Book> getDataMap() {
        return super.getDataMap();
    }

    public Book findBookByDistinctAttribute(String type, int searchType) {
        Book book = null;
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "";
        switch (searchType) {
            case 1:
                query = "SELECT * FROM books WHERE no = ?";
                break;
            case 2:
                query = "SELECT * FROM books WHERE title = ?";
                break;
            case 3:
                query = "SELECT * FROM books WHERE ISBN = ?";
                break;
            case 4:
                query = "SELECT * FROM books WHERE URL = ?";
                break;
            default:
                throw new IllegalArgumentException("Invalid search type");
        }
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, type);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                book = new Book();
                book.setNo(resultSet.getInt("no"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setPubdate(resultSet.getString("pubdate"));
                book.setReleaseDate(resultSet.getString("releaseDate"));
                book.setISBN(resultSet.getString("ISBN"));
                book.setPrice(resultSet.getString("price"));
                book.setSubject(resultSet.getString("subject"));
                book.setCategory(resultSet.getString("category"));
                book.setURL(resultSet.getString("URL"));
                book.setBookType(resultSet.getString("bookType"));
                book.setQuantity(resultSet.getString("quantity"));
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
        return book;
    }

    public List<Book> findBooksByAttribute(String attribute, String value) {
        // List to hold the result
        List<Book> books = new ArrayList<>();

        // Callable to perform the database operation
        Callable<List<Book>> task = () -> {
            List<Book> result = new ArrayList<>();
            PreparedStatement statement = null;
            ResultSet resultSet = null;
            String query;
            // Adjust attribute if necessary
            if (attribute.equals("id")) {
                query = "SELECT * FROM books WHERE " + "no" + " = ?";
            }
            else query = "SELECT * FROM books WHERE " + attribute + " = ?";
            if (!attribute.equals("id")) {
                query = "SELECT * FROM books WHERE " + attribute + " LIKE ?";
            }
            System.out.println(query);

            try {
                statement = connection.prepareStatement(query);
                if (!attribute.equals("id")) {
                    statement.setString(1, "%" + value + "%");
                } else {
                    statement.setString(1, value);
                }

                // Execute the query
                resultSet = statement.executeQuery();

                // Populate the result list
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
                    result.add(book);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (resultSet != null) resultSet.close();
                    if (statement != null) statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return result;
        };

        // Execute the task in a separate thread
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<List<Book>> future = executor.submit(task);

        try {
            // Wait for the task to complete and get the result
            books = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        return books;
    }

    public int fetchQuantityFromBooks(String type, int searchType) {
        return Integer.parseInt(findBookByDistinctAttribute(type, searchType).getQuantity());
    }

    public String fetchISBNFromBooks(String type, int searchType) {
        return findBookByDistinctAttribute(type, searchType).getISBN();
    }

    public String fetchTitleFromBooks(String type, int searchType) {
        return findBookByDistinctAttribute(type, searchType).getTitle();
    }

    public String fetchPriceFromBooks(String ISBN, int searchType) {
        String price = null;
        String query = "SELECT price FROM books WHERE ISBN = ?";  // Truy vấn trực tiếp cột price theo ISBN
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, ISBN);  // Set ISBN vào câu truy vấn

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    price = resultSet.getString("price");  // Lấy giá trực tiếp từ kết quả
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return price;  // Trả về giá hoặc null nếu không tìm thấy
    }

    public void insertBookToDatabase(String title, String author, String pubdate, String releaseDate,
                                     String ISBN, String price, String subject, String category,
                                     String URL, String bookType, String quantity) {
        Callable<Void> task = () -> {
            String findNextAvailableIDQuery = "SELECT t1.no + 1 AS next_id FROM books t1 "
                    + "LEFT JOIN books t2 ON t1.no + 1 = t2.no WHERE t2.no IS NULL LIMIT 1";
            String sql = "INSERT INTO books (no, title, author, pubdate, releaseDate, ISBN, price, subject, category, URL, bookType, quantity) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement findIDStmt = conn.prepareStatement(findNextAvailableIDQuery);
                 ResultSet rs = findIDStmt.executeQuery()) {

                int nextAvailableID = -1;
                if (rs.next()) {
                    nextAvailableID = rs.getInt("next_id");
                }

                // Nếu không tìm thấy ID trống, thì sử dụng ID tự động tăng.
                if (nextAvailableID == -1) {
                    nextAvailableID = getNextAutoIncrementID(conn); // Lấy ID tiếp theo tự động tăng
                }

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, nextAvailableID);
                    pstmt.setString(2, title);
                    pstmt.setString(3, author);
                    pstmt.setString(4, pubdate);
                    pstmt.setString(5, releaseDate);
                    pstmt.setString(6, ISBN);
                    pstmt.setString(7, price);
                    pstmt.setString(8, subject);
                    pstmt.setString(9, category);
                    pstmt.setString(10, URL);
                    pstmt.setString(11, bookType);
                    pstmt.setString(12, quantity);
                    pstmt.executeUpdate();
                }

            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error while inserting book data.");
            }
            return null;
        };
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Void> future = executorService.submit(task);
        try {
            future.get();
        } catch (Exception e) {
            JavaFXAlertDisplayer.getInstance().showErrorAlert("Error", "An error occured when inserting book!");
        } finally {
            executorService.shutdown();
        }

    }

    private int getNextAutoIncrementID(Connection conn) throws SQLException {
        String autoIncrementQuery = "SELECT AUTO_INCREMENT FROM information_schema.tables "
                + "WHERE table_name = 'books' AND table_schema = DATABASE()";
        try (PreparedStatement stmt = conn.prepareStatement(autoIncrementQuery);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("AUTO_INCREMENT");
            }
            return -1;
        }
    }

    private LocalDate parseDate(String dateStr, DateTimeFormatter formatter) {
        try {
            return LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date format: " + dateStr);
            return null;
        }
    }
    public Book getBookByBookname(String title) {
        return dataMap.get(title);
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

    public void deleteBookFromDatabase (String title) {
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
            e.printStackTrace();
        }
    }

    public void loadGenreCirculationData(PieChart genreCirculationChart) {
        String sql = "SELECT category, SUM(quantity) AS total_quantity " +
                "FROM books " +
                "WHERE category IN ('Business and Economics', 'Chemistry/Materials Science/Nanotechnology', 'Computer Science', " +
                "'Engineering', 'Environmental Science', 'General and Popular Science', 'Life Sciences', " +
                "'Mathematics', 'Medicine and Healthcare', 'Physics/Nonlinear Science', 'Social Sciences and Asian Studies') " +
                "GROUP BY category";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            while (resultSet.next()) {
                String category = resultSet.getString("category");
                int totalQuantity = resultSet.getInt("total_quantity");
                pieChartData.add(new PieChart.Data(category, totalQuantity));
            }
            genreCirculationChart.setData(pieChartData);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<XYChart.Series<String, Number>> loadGenreBorrowedData() {
        String sql = "SELECT b.category, COUNT(c.ISBN) AS borrow_count " +
                "FROM books b " +
                "JOIN cart c ON b.ISBN = c.ISBN " +
                "GROUP BY b.category " +
                "ORDER BY b.category, borrow_count DESC";

        ObservableList<XYChart.Series<String, Number>> seriesList = FXCollections.observableArrayList();
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String category = resultSet.getString("category");
                int borrowCount = resultSet.getInt("borrow_count");

                // Thêm dữ liệu vào Series
                series.getData().add(new XYChart.Data<>(category, borrowCount));
            }

            // Đặt tên cho Series
            series.setName("Top Books Borrowed by Category");
            seriesList.add(series);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return seriesList;
    }

    public void updateQuantity(String username, String bookName, String operation) {
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

//    author	content	timemark	ISBN

    public void addNewComment(Book book, Comment comment) throws RuntimeException {
        String author = comment.getAuthor();
        String content = comment.getContent();
        String ISBN = book.getISBN();
        LocalDateTime timemark = comment.getTimestamp();

        String sql = "INSERT INTO comment (author, content, timemark, ISBN) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, author);
            preparedStatement.setString(2, content);
            preparedStatement.setTimestamp(3, Timestamp.valueOf(timemark));
            preparedStatement.setString(4, ISBN);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Comment> getBookComment(String ISBN) {
        List<Comment> commentList = new ArrayList<>();
        String sql = "SELECT * FROM comment WHERE ISBN = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, ISBN);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Comment comment = new Comment(resultSet.getString("author"), resultSet.getString("content"),
                        resultSet.getTimestamp("timemark").toLocalDateTime());
                commentList.add(comment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commentList;
    }
}

