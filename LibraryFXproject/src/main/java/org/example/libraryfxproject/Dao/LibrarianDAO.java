package org.example.libraryfxproject.Dao;

import org.example.libraryfxproject.Model.Book;
import org.example.libraryfxproject.Model.Librarian;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LibrarianDAO extends GeneralDao<String, Librarian>{
    @Override
    public void loadData() {
        String sql = "SELECT * FROM Librarian";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                // Lấy dữ liệu từ ResultSet
                String username = resultSet.getString("username");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String phoneNumber = resultSet.getString("phoneNumber");
                int id = resultSet.getInt("Librarian_ID");
                String workShift = resultSet.getString("workShift");

                // Tạo đối tượng Librarian
                Librarian librarian = new Librarian(username, name, email, phoneNumber, id, workShift);

                // Thêm librarian vào HashMap (giả sử dataMap là một HashMap<String, Librarian>)
                dataMap.put(username, librarian);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Librarian findLibrarian(String type, int searchType) {
        Librarian librarian = null;
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = null;

        if (searchType == 1) {
            query = "SELECT * FROM librarian WHERE Librarian_ID = ?";
        } else if (searchType == 2) {
            query = "SELECT * FROM librarian WHERE username = ?";
        }

        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, type);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                librarian = new Librarian(resultSet.getString("username"),
                                          resultSet.getString("name"),
                                          resultSet.getString("email"),
                                          resultSet.getString("phoneNumber"),
                                          resultSet.getInt("id"),
                                          resultSet.getString("workShift"));
            } else {
                System.out.println("Không tìm thấy thủ thư" + type);
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
        return librarian;
    }


