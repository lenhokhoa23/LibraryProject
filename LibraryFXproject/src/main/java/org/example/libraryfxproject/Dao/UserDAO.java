package org.example.libraryfxproject.Dao;

import org.example.libraryfxproject.Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp UserDAO quản lý các thao tác với dữ liệu người dùng trong cơ sở dữ liệu.
 * Cung cấp các phương thức để tìm kiếm, lưu trữ, xóa và sửa đổi thông tin người dùng.
 */
public class UserDAO extends GeneralDAO<String, User> {
    private static UserDAO userDAO;

    private UserDAO() {

    }

    public static synchronized UserDAO getInstance() {
        if (userDAO == null) {
            userDAO = new UserDAO();
        }
        return userDAO;
    }

    /**
     * Tải tất cả dữ liệu người dùng vào bộ nhớ.
     */
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

    /**
     * Tìm người dùng theo tên đăng nhập.
     * @param username tên đăng nhập.
     * @return đối tượng User tương ứng với tên đăng nhập.
     */
    public User findUserByUsername(String username) {
        return dataMap.get(username);
    }

    /**
     * Lấy cart_id của người dùng theo tên đăng nhập.
     * @param username tên đăng nhập.
     * @return cart_id của người dùng hoặc -1 nếu không tìm thấy.
     */
    public int fetchCartIdByUsername(String username) {
        if (dataMap.get(username) == null) {
            return -1;
        } else {
            return dataMap.get(username).getCart_ID();
        }
    }

    /**
     * Lấy tổng số lượng người dùng.
     * @return tổng số lượng người dùng.
     */
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

    /**
     * Lưu người dùng mới vào cơ sở dữ liệu.
     * @param name tên người dùng.
     * @param email email người dùng.
     * @param phoneNumber số điện thoại người dùng.
     * @param username tên đăng nhập người dùng.
     * @param password mật khẩu người dùng.
     * @param membershipType loại thẻ hội viên của người dùng.
     */
    public void saveUserToDatabase(String name, String email, String phoneNumber, String username, String password, String membershipType) {
        // Sql để tìm id tiếp theo để thêm vào database.
        String findNextUserIDQuery = "SELECT t1.cart_id + 1 AS next_id FROM user t1 "
                + "LEFT JOIN user t2 ON t1.cart_id + 1 = t2.cart_id WHERE t2.cart_id IS NULL LIMIT 1";
        String insertUserSQL = "INSERT INTO user (cart_id, username, name, email, phoneNumber, borrowedBooks, membershipType) "
                + "VALUES (?, ?, ?, ?, ?, 0, ?)";
        String insertAccountSQL = "INSERT INTO accounts (username, password, role) VALUES (?, ?, 'user')";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement findNextUserIDStmt = connection.prepareStatement(findNextUserIDQuery);
             PreparedStatement insertUserStmt = connection.prepareStatement(insertUserSQL);
             PreparedStatement insertAccountStmt = connection.prepareStatement(insertAccountSQL)) {

            // Tìm cart_id tiếp theo cho bảng user
            int nextCartID = 0;
            ResultSet rsUserID = findNextUserIDStmt.executeQuery();
            if (rsUserID.next()) {
                nextCartID = rsUserID.getInt("next_id");
            } else {
                throw new SQLException("Failed to find the next cart_id.");
            }
            insertAccountStmt.setString(1, username);
            insertAccountStmt.setString(2, password);
            insertAccountStmt.executeUpdate();

            insertUserStmt.setInt(1, nextCartID); // cart_id
            insertUserStmt.setString(2, username); // username
            insertUserStmt.setString(3, name); // name
            insertUserStmt.setString(4, email); // email
            insertUserStmt.setString(5, phoneNumber); // phoneNumber
            insertUserStmt.setString(6, membershipType); // membershipType
            insertUserStmt.executeUpdate();
            System.out.println("User and account added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error adding user and account: " + e.getMessage());
        }
    }

    /**
     * Tìm người dùng theo một phần của tên đăng nhập.
     * @param component phần của tên đăng nhập.
     * @return danh sách người dùng tìm được.
     */
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

    /**
     * Lấy tên đăng nhập của người dùng theo cart_id.
     * @param cartId cart_id của người dùng.
     * @return tên đăng nhập người dùng.
     */
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

    /**
     * Xóa người dùng và tài khoản của người dùng theo tên đăng nhập.
     * @param usernameToDelete tên đăng nhập của người dùng cần xóa.
     */
    public void deleteUserForNextRun(String usernameToDelete) {
        String deleteSQLUser = "DELETE FROM user WHERE username = ?";
        String deleteSQLAccount = "DELETE FROM accounts WHERE username = ?";

        try (PreparedStatement stmtAccount = connection.prepareStatement(deleteSQLAccount);
             PreparedStatement stmtUser = connection.prepareStatement(deleteSQLUser)) {

            stmtAccount.setString(1, usernameToDelete);
            stmtAccount.executeUpdate();

            stmtUser.setString(1, usernameToDelete);
            stmtUser.executeUpdate();
            System.out.println("User and associated account deleted successfully.");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete user and account: " + e.getMessage(), e);
        }
    }

    /**
     * Sửa đổi thuộc tính của người dùng theo cart_id.
     * @param userId cart_id của người dùng.
     * @param attribute tên thuộc tính cần thay đổi.
     * @param newValue giá trị mới của thuộc tính.
     */
    public static void modifyUserByAttribute(int userId, String attribute, String newValue) {
        String sql = "UPDATE user SET " + attribute + " = ? WHERE Cart_ID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, newValue);
            statement.setInt(2, userId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Cập nhật thành công thuộc tính " + attribute + " cho người dùng có Cart_ID: " + userId);
            } else {
                System.out.println("Không tìm thấy người dùng với Cart_ID: " + userId);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi cập nhật thuộc tính của người dùng!");
            e.printStackTrace();
        }
    }

}
