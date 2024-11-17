package org.example.libraryfxproject.Dao;

import javafx.scene.control.Alert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import org.example.libraryfxproject.Model.Book;
import org.example.libraryfxproject.Model.Trie;
import org.example.libraryfxproject.Model.TrieNode;
import org.example.libraryfxproject.Service.LoadService;
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
                    System.out.println("Không thể chuyển đổi quantity thành số cho sách: " + book.getTitle());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
            } else {
                System.out.println("Không tìm thấy sách có tựa đề " + type);
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
        List<Book> books = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        if (attribute.equals("id")) {
            attribute = "no";
        }
        String query = "SELECT * FROM books WHERE " + attribute + " = ?";
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, value);
            resultSet = statement.executeQuery();
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
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public int fetchQuantityFromBooks(String type, int searchType) {
        Book book = findBookByDistinctAttribute(type, searchType);
        return Integer.parseInt(book.getQuantity());
    }


    public String fetchISBNFromBooks(String type, int searchType) {
        return findBookByDistinctAttribute(type, searchType).getISBN();
    }

    public void insertBookToDatabase(String title, String author, String pubdateStr, String releaseDateStr,
                                     String ISBN, String price, String subject, String category, String URL,
                                     String bookType, String quantity) {
        String sql = "INSERT INTO books (title, author, pubdate, releaseDate, ISBN, price, subject, category, URL, bookType, quantity) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yy"); // e.g., 12-May-00

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, title);
            statement.setString(2, author);
            LocalDate pubdate = parseDate(pubdateStr, formatter);
            LocalDate releaseDate = parseDate(releaseDateStr, formatter);
            statement.setDate(3, pubdate != null ? Date.valueOf(pubdate) : null);
            statement.setDate(4, releaseDate != null ? Date.valueOf(releaseDate) : null);
            statement.setString(5, ISBN);
            statement.setString(6, price);
            statement.setString(7, subject);
            statement.setString(8, category);
            statement.setString(9, URL);
            statement.setString(10, bookType);
            statement.setString(11, quantity);
            statement.executeUpdate();
            System.out.println("Thêm sách thành công!");
        } catch (SQLException e) {
            e.printStackTrace();
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

    public void deleteBookFromDatebase (String title) {
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

    /*public String getBooksStatus(String bookTitle) {
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
                String status = CartDAO.getBookStatus("viettran97"); // Gọi hàm lấy trạng thái
                statusBuilder.append("Cart ID: ").append(cartId)
                        .append(" - ").append(status).append("\n");
            }

            // Thêm thông tin số lượng sách vào kết quả
            statusBuilder.insert(0, "Số lượng sách \"" + bookTitle + "\": " + count + "\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statusBuilder.toString();
    }*/
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
}

