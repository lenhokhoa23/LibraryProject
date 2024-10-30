package org.example.libraryfxproject.Dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import org.example.libraryfxproject.Model.Book;
import org.example.libraryfxproject.Model.Trie;
import org.example.libraryfxproject.Model.TrieNode;
import org.example.libraryfxproject.Service.LoadService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class BookDAO extends GeneralDao<String, Book> {
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

    public Book findBookByType(String type, int searchType) {
        Book book = null;
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = null;

        if (searchType == 1) {
            query = "SELECT * FROM books WHERE title = ?";
        } else if (searchType == 2) {
            query = "SELECT * FROM books WHERE author = ?";
        } else if (searchType == 3) {
            query = "SELECT * FROM books WHERE category = ?";
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

    public int fetchQuantityFromBooks(String type, int searchType) {
        return Integer.parseInt(findBookByType(type, searchType).getQuantity());
    }

    public String fetchISBNFromBooks(String type, int searchType) {
        return findBookByType(type, searchType).getISBN();
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
}

