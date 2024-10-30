package org.example.libraryfxproject.Dao;

import org.example.libraryfxproject.Model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO extends GeneralDao<String, User> {

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

}
