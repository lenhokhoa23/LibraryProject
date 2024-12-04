package org.example.libraryfxproject.Dao;

import org.example.libraryfxproject.Model.Account;
import org.example.libraryfxproject.Service.LoadService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class AccountDAO extends GeneralDAO<String, Account> {
    private static AccountDAO accountDAO;
    private AccountDAO() {

    }

    public static synchronized AccountDAO getInstance() {
        if (accountDAO == null) {
            accountDAO = new AccountDAO();
        }
        return accountDAO;
    }

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


    public Account getAccountByUsername(String username) {
        return dataMap.get(username);
    }

}
