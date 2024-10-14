package org.example.libraryfxproject.Dao;

import org.example.libraryfxproject.Model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class BookDAO extends GeneralDao<String, Book> {

    @Override
    public void loadData() {
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

                // Thêm sách vào HashMap
                dataMap.put(book.getTitle(), book);
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


}
