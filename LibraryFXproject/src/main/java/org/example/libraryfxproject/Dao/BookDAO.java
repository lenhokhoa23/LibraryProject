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
}
