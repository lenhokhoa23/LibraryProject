package org.example.libraryfxproject.Dao;

import org.example.libraryfxproject.Model.Account;
import org.example.libraryfxproject.Service.LoadService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class AccountDAO extends GeneralDao<String, Account> {

    @Override
    public void loadData() {
        String sql = "SELECT * FROM accounts";
        try (PreparedStatement statement = connection.prepareStatement(sql);) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Account account = new Account(resultSet.getString(1),
                        resultSet.getString(2), resultSet.getString(3));
                dataMap.put(account.getUsername(), account);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi load user memory");
            e.printStackTrace();
        }
    }

    public void saveUserToDatabase(String name, String email, String phoneNumber, String username, String password) {
        String insertUserSQL = "INSERT INTO user (username, name, email, phoneNumber, borrowedBooks, membershipType) VALUES (?, ?, ?, ?, 0, 'Basic')";
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
            preparedStatementUser.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Account getAccountByUsername(String username) {
        return dataMap.get(username);
    }

}
