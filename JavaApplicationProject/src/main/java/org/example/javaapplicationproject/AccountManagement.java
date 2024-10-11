package org.example.javaapplicationproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class AccountManagement {
    private static HashMap<String, Account> accountMap = new HashMap<>();

    /** addAccount method return. */
    public static void addAccount(Account account) {
        String sql = "INSERT INTO accounts (username, password, role)" + "VALUES(?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());
            statement.setString(3, account.getRole());
            statement.executeUpdate();
            System.out.println("Tạo tài khoản thành công!");
        } catch (SQLException e) {
            System.out.println("Username này đã tồn tại, vui lòng chọn username khác!");
        }
    }
    public static void deleteAccount(String username) {
        String sql = "DELETE FROM accounts WHERE username = ?";
        try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            int rowAffected = statement.executeUpdate();
            if (rowAffected > 0) {
                System.out.println("Xoá tài khoản thành công!");
            } else {
                System.out.println("Không tồn tại tài khoản này!");
            }
        } catch (SQLException e) {
            System.out.println("Đã xảy ra lỗi khi xoá tài khoản!");
            e.printStackTrace();
        }
    }

    public static void loadUserIntoMemory() {
        String sql = "SELECT * FROM accounts";
        try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Account account = new Account(resultSet.getString(1),
                        resultSet.getString(2), resultSet.getString(3));
                accountMap.put(account.getUsername(), account);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi load user memory");
            e.printStackTrace();
        }
    }

    public static void findUser(String username) {
        Account account = accountMap.get(username);
        if (account == null) {
            System.out.println("Không tìm thấy tài khoản này!");
        } else {
            if (account.getRole().equals("admin")) {
                String sql =  "SELECT * FROM librarian where username = ?";
                try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);) {
                    statement.setString(1, account.getUsername());
                    ResultSet resultSet = statement.executeQuery();
                    resultSet.next();
                    Librarian librarian = new Librarian(resultSet.getString(1),
                            resultSet.getString(2), resultSet.getString(3),
                            resultSet.getString(4), resultSet.getInt(5),
                            resultSet.getString(6));
                    librarian.printInfo();
                } catch (SQLException e) {
                    System.out.println("Lỗi khi truy cập bảng librarian");
                    e.printStackTrace();
                }
            } else {
                if (account.getRole().equals("user")) {
                    String sql = "SELECT * FROM user where username = ?";
                    try (Connection connection = DatabaseConnection.getConnection();
                         PreparedStatement statement = connection.prepareStatement(sql);) {
                        statement.setString(1, account.getUsername());
                        ResultSet resultSet = statement.executeQuery();
                        resultSet.next();
                        User user = new User(resultSet.getString(1),
                                resultSet.getString(2), resultSet.getString(3),
                                resultSet.getString(4), resultSet.getInt(5),
                                resultSet.getInt(6), resultSet.getString(7));
                        user.printInfo();
                    } catch (SQLException e) {
                        System.out.println("Lỗi khi truy cập bảng user");
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public static int fetchCartIdByUsername(String username) {
        int cartId = -1;
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String query = "SELECT Cart_ID FROM user WHERE username = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, username); // Gán giá trị username vào câu truy vấn

            // Thực thi truy vấn
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                cartId = resultSet.getInt("Cart_ID"); // Gán giá trị nếu tìm thấy
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
        return cartId;
    }


}


