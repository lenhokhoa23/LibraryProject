package org.example.javaapplicationproject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import static org.example.javaapplicationproject.Controller.br;

public class AccountManagement {
    private static HashMap<String, Account> accountMap = new HashMap<>();

    /** addAccount method return. */
    public static void addAccount(Account account) throws IOException {
        String sql1 = "INSERT INTO accounts (username, password, role)" + "VALUES(?, ?, ?)";
        String sql2 ="INSERT INTO `user` (username, name, email, phoneNumber, borrowedBooks, membershipType)"
                + "VALUES(?, ?, ?, ?, ?, ?)";
        System.out.println("Chúc mừng bạn đã đăng kí thành công, hãy hoàn thiện việc" +
                " đăng kí bằng cách điền thêm thông tin của bạn!!!");
        System.out.println("Nhập tên của bạn: ");
        String name = br.readLine();
        System.out.println("Nhập email của bạn: ");
        String email = br.readLine();
        System.out.println("Nhập số điện thoại của bạn: ");
        String phoneNumber = br.readLine();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement1 = connection.prepareStatement(sql1);
        PreparedStatement statement2 = connection.prepareStatement(sql2)){
            statement1.setString(1, account.getUsername());
            statement1.setString(2, account.getPassword());
            statement1.setString(3, account.getRole());
            statement2.setString(1, account.getUsername());
            statement2.setString(2, name);
            statement2.setString(3, email);
            statement2.setString(4, phoneNumber);
            statement2.setInt(5, 0);
            statement2.setString(6, "Standard");
            statement1.executeUpdate();
            statement2.executeUpdate();
            System.out.println("Tạo tài khoản thành công!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Tạo tài khoản không thành công!, Vui lòng nhập đúng định dạng thông tin!");
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
                        User user = new User(resultSet.getString(2),
                                resultSet.getString(3), resultSet.getString(4),
                                resultSet.getString(5), resultSet.getInt(1),
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

    public static void register() throws IOException {
        System.out.println("Enter username: ");
        String username = br.readLine();
        System.out.println("Enter password: ");
        String password = br.readLine();

        Account account = new Account(username, password, "user");
        AccountManagement.addAccount(account);
    }

}


