package org.example.javaapplicationproject;

import java.sql.*;
import java.time.LocalDate;

public class CartManagement {
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

    /** This function finds Cart from CartID. */
    public static Cart fetchCartByCartID(int cartId) {
        String sql = "SELECT * FROM cart WHERE Cart_ID = ?";
        Cart cart = null;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, cartId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                cart = new Cart(
                        resultSet.getInt("Cart_ID"),
                        resultSet.getString("startDate"),
                        resultSet.getString("endDate"),
                        resultSet.getString("ISBN"),
                        resultSet.getString("title")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cart;
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
}
