package org.example.libraryfxproject.Dao;

import org.example.libraryfxproject.Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends GeneralDAO<String, User> {
    /** Load userdata into memory. */
    @Override
    public void loadData() {
        String sql = "SELECT * FROM user";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User(resultSet.getString(2), resultSet.getString(3),
                        resultSet.getString(4), resultSet.getString(5),
                        resultSet.getInt(1), resultSet.getInt(6),
                        resultSet.getString(7));
                dataMap.put(user.getUsername(), user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User findUserByUsername(String username) {
        return dataMap.get(username);
    }

    public int fetchCartIdByUsername(String username) {
        if (dataMap.get(username) == null) {
            return -1;
        } else {
            return dataMap.get(username).getCart_ID();
        }
    }

    public int getTotalUserCount() {
        String sql = "SELECT COUNT(*) AS userCount FROM user";
        int userCount = 0;

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                userCount = resultSet.getInt("userCount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userCount;
    }

    public void saveUserToDatabase(String name, String email, String phoneNumber, String username, String password, String membershipType) {
        String insertUserSQL = "INSERT INTO user (username, name, email, phoneNumber, borrowedBooks, membershipType) VALUES (?, ?, ?, ?, 0, ?)";
        String insertAccountSQL = "INSERT INTO accounts (username, password, role) VALUES (?, ?, 'user')";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatementUser = connection.prepareStatement(insertUserSQL);
             PreparedStatement preparedStatementAccount = connection.prepareStatement(insertAccountSQL)) {

            preparedStatementAccount.setString(1, username);
            preparedStatementAccount.setString(2, password);
            preparedStatementAccount.executeUpdate();

            preparedStatementUser.setString(1, username);
            preparedStatementUser.setString(2, name);
            preparedStatementUser.setString(3, email);
            preparedStatementUser.setString(4, phoneNumber);
            preparedStatementUser.setString(5, membershipType);
            preparedStatementUser.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<User> findUserByComponentOfUserName(String component) {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM user WHERE username LIKE '%" + component + "%'";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                User user = new User(resultSet.getString(2),
                        resultSet.getString(3), resultSet.getString(4),
                        resultSet.getString(5), resultSet.getInt(1),
                        resultSet.getInt(6), resultSet.getString(7));
                userList.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi truy cập bảng user");
            e.printStackTrace();
        }
        return userList;
    }

    public String getUsernameByCartId(int cartId) {
        String selectUsernameSQL = "SELECT username FROM user WHERE Cart_ID = ?";
        String username = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectUsernameSQL)) {
            preparedStatement.setInt(1, cartId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    username = resultSet.getString("username");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return username;
    }
    public void deleteUserForNextRun(String usernameToDelete) {
        String deleteSQLUser = "DELETE FROM user WHERE username = ?";
        String deleteSQLAccount = "DELETE FROM accounts WHERE username = ?";
        try (PreparedStatement stmtUser = connection.prepareStatement(deleteSQLUser)) {
            stmtUser.setString(1, usernameToDelete);
            int rowsAffectedUser = stmtUser.executeUpdate();
            if (rowsAffectedUser == 0) {
                throw new SQLException("User not found in the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Delete from 'accounts' table
        try (PreparedStatement stmtAccount = connection.prepareStatement(deleteSQLAccount)) {
            stmtAccount.setString(1, usernameToDelete);
            int rowsAffectedAccount = stmtAccount.executeUpdate();
            if (rowsAffectedAccount == 0) {
                throw new SQLException("Account associated with user not found.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        System.out.println("User and associated account deleted successfully.");
    }
}
