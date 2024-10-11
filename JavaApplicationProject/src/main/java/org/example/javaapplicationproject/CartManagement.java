package org.example.javaapplicationproject;


import java.sql.*;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class CartManagement {
    public void addCart(Cart cart) {
        String sql = "INSERT INTO cart (Cart_ID, startDate, endDate, title, ISBN) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cart.getCart_ID());
            statement.setString(2, cart.getStartDate());
            statement.setString(3, cart.getEndDate());
            statement.setString(4, cart.getTitle());
            statement.setString(5, cart.getISBN());
            statement.executeUpdate();
            System.out.println("Thêm sách vào giỏ hàng thành công!");

        } catch (SQLException e) {
            System.out.println("Lỗi khi thêm sách vào giỏ hàng!");
            e.printStackTrace();
        }
    }

    public String fetchISBNFromCart(String bookTitle, int cart_id) {
        String isbn = null;
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String query = "SELECT ISBN FROM cart WHERE cart_id = ? AND title = ?";

            statement = connection.prepareStatement(query);
            statement.setInt(1, cart_id);
            statement.setString(2, bookTitle);

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                isbn = resultSet.getString("ISBN");
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
        return isbn;
    }

    /** This function finds CartID from username. */
    public static int fetchCartIdByUsername(String username) {
        int cartId = -1;
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT Cart_ID FROM user WHERE username = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username); // Gán giá trị username vào câu truy vấn
            // Thực thi truy vấn
            rs = pstmt.executeQuery();
            if (rs.next()) {
                cartId = rs.getInt("Cart_ID"); // Gán giá trị CartID vào biến tương ứng
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return cartId;
    }

    /** This function cho biết trạng thái của quyển sách trong giỏ hàng. */
    public static String getBookStatus(int Cart_ID) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT endDate FROM cart WHERE Cart_ID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, Cart_ID); // Lấy endDate từ cart có ID tương ứng.
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Lấy ra giá trị kiểu java.sql.Date từ cột "endDate"
                Date eventDate = rs.getDate("endDate");

                // Chuyển đổi java.sql.Date thành java.time.LocalDate để dễ so sánh
                LocalDate eventLocalDate = eventDate.toLocalDate();

                // Lấy thời gian hiện tại
                LocalDate currentDate = LocalDate.now();

                // So sánh thời gian hiện tại với thời gian trong cơ sở dữ liệu
                if (currentDate.isBefore(eventLocalDate)) {
                    return "\033[32mTrạng thái: Còn hạn.\033[0m";
                } else {
                    return "\033[31mTrạng thái: Quá hạn mượn.\033[0m";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Không xác định.";
    }

    public void deleteCart(String isbn, int cartId) {
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = null;

        try {
            String query = "DELETE FROM cart WHERE ISBN = ? AND Cart_ID = ?";

            statement = connection.prepareStatement(query);
            statement.setString(1, isbn);
            statement.setInt(2, cartId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Xóa thành công sách có ISBN " + isbn + " khỏi giỏ hàng với Cart_ID: " + cartId);
            } else {
                System.out.println("Không tìm thấy sách với ISBN " + isbn + " và Cart_ID " + cartId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
