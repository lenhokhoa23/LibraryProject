package org.example.libraryfxproject.Dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.libraryfxproject.Model.Account;
import org.example.libraryfxproject.Model.Cart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CartDAO extends GeneralDAO<Integer, Cart> {

    @Override
    public void loadData() {
        String sql = "SELECT * FROM cart";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                // Lấy dữ liệu từ ResultSet
                int cartId = resultSet.getInt("Cart_ID");
                String startDate = resultSet.getString("startDate");
                String endDate = resultSet.getString("endDate");
                String isbn = resultSet.getString("ISBN");
                String title = resultSet.getString("title");

                // Tạo đối tượng Cart
                Cart cart = new Cart(cartId, startDate, endDate, isbn, title);

                // Thêm cart vào HashMap (giả sử dataMap là một HashMap<Integer, Cart>)
                dataMap.put(cartId, cart);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Cart fetchCartByUsername(String username) {
        Cart cart = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // Query to get Cart details for the given username using JOIN
            String query = "SELECT c.Cart_ID, c.startDate, c.endDate, c.ISBN, c.title " +
                    "FROM user u JOIN cart c ON u.Cart_ID = c.Cart_ID " +
                    "WHERE u.username = ?";
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, username); // Set the username parameter
            rs = pstmt.executeQuery();
            if (rs.next()) {
                // Create the Cart object with details from the result set
                int cartID = rs.getInt("Cart_ID");
                String startDate = rs.getString("startDate");
                String endDate = rs.getString("endDate");
                String isbn = rs.getString("ISBN");
                String title = rs.getString("title");
                cart = new Cart(cartID, startDate, endDate, title, isbn); // Create Cart object
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return cart; // Return the Cart object or null if not found
    }

    public String fetchFromCart(String username, String attribute) {
        // Check which attribute to fetch from the Cart object
        Cart cart = fetchCartByUsername(username);
        switch (attribute) {
            case "ISBN":
                return cart.getISBN();
            case "title":
                return cart.getTitle();
            case "startDate":
                return cart.getStartDate();
            case "endDate":
                return cart.getEndDate();
            case "Cart_ID":
                return String.valueOf(cart.getCart_ID());
            default:
                return "Invalid attribute"; // Handle case for an invalid attribute
        }
    }

    public String getBookStatus(String username) {
        Cart cart = fetchCartByUsername(username);
        int Cart_ID = cart.getCart_ID();
        try {
            String query = "SELECT endDate FROM cart WHERE Cart_ID = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
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

    public String checkCartUser(Account account, String user) {
        int cartId = 0;
        // Determine cartId based on user role
        String role = account.getRole();
        String username = account.getUsername();
        if (role.equals("user")) {
            Cart userCart = fetchCartByUsername(username);
            if (userCart != null) {
                cartId = userCart.getCart_ID();
            }
        } else if (role.equals("admin")) {
            // Check if the username exists in accounts
            String query = "SELECT username FROM accounts WHERE username = " + user;
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, user);
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) {
                    return "Người dùng \"" + user + "\" không tồn tại.";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            cartId = fetchCartByUsername(user).getCart_ID();
        }

        // Lấy tổng số sách đã mượn và thông tin từng sách
        String countQuery = "SELECT COUNT(*) AS totalBooks FROM cart WHERE Cart_ID = ?";
        String detailQuery = "SELECT * FROM cart WHERE Cart_ID = ?";

        StringBuilder result = new StringBuilder(); // result string
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement countStmt = conn.prepareStatement(countQuery);
             PreparedStatement detailStmt = conn.prepareStatement(detailQuery)) {

            // Lấy tổng số sách đã mượn
            countStmt.setInt(1, cartId);
            ResultSet countRs = countStmt.executeQuery();
            if (countRs.next()) {
                int totalBooks = countRs.getInt("totalBooks");
                result.append("Số sách đã mượn: ").append(totalBooks).append("\n");
            }
            // Lấy thông tin chi tiết từng sách
            detailStmt.setInt(1, cartId);
            ResultSet detailRs = detailStmt.executeQuery();

            // Kiểm tra nếu giỏ hàng trống
            if (!detailRs.isBeforeFirst()) {
                return "Giá sách cá nhân của người dùng \"" + username + "\" trống.";
            }
            // Duyệt qua tất cả các sách trong giỏ
            while (detailRs.next()) {
                Cart cart = new Cart(
                        detailRs.getInt("Cart_ID"),
                        detailRs.getString("startDate"),
                        detailRs.getString("endDate"),
                        detailRs.getString("ISBN"),
                        detailRs.getString("title")
                );
                result.append("Sách: ").append(cart.getTitle())
                        .append(", ISBN: ").append(cart.getISBN())
                        .append(", Ngày bắt đầu: ").append(cart.getStartDate())
                        .append(", Ngày kết thúc: ").append(cart.getEndDate()).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public int getBooksBorrowedCount() {
        String sql = "SELECT COUNT(*) AS borrowedCount FROM Cart";
        int borrowedCount = 0;

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                borrowedCount = resultSet.getInt("borrowedCount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowedCount;
    }

    public int getOverdueBooksCount() {
        String sql = "SELECT COUNT(*) AS overdueCount FROM Cart WHERE endDate < ?";
        int overdueCount = 0;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                overdueCount = resultSet.getInt("overdueCount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return overdueCount;
    }

    public ObservableList<ObservableList<String>> getActivities(int limit) {
        ObservableList<ObservableList<String>> activities = FXCollections.observableArrayList();

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                String query = "SELECT c.startDate AS time, c.Cart_ID AS userID, u.username, " +
                        "c.title AS bookTitle, c.ISBN, c.endDate AS due " +
                        "FROM cart c " +
                        "JOIN user u ON c.Cart_ID = u.Cart_ID ORDER BY c.startDate DESC " +
                        (limit > 0 ? "LIMIT ?" : "");

                PreparedStatement statement = conn.prepareStatement(query);
                if (limit > 0) {
                    statement.setInt(1, limit);
                }
                ResultSet result = statement.executeQuery();

                while (result.next()) {
                    ObservableList<String> row = FXCollections.observableArrayList();
                    row.add(result.getString("time"));
                    row.add(String.valueOf(result.getInt("userID")));
                    row.add(result.getString("username"));
                    row.add(result.getString("bookTitle"));
                    row.add(result.getString("ISBN"));
                    row.add(result.getString("due"));
                    activities.add(row);
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi truy vấn dữ liệu.");
            e.printStackTrace();
        }

        return activities;
    }

    public List<Cart> getAllBorrowHistory() {
        List<Cart> borrowHistory = new ArrayList<>();
        String query = """
                SELECT c.Cart_ID, c.startDate, c.endDate, c.title, c.ISBN, u.username
                            FROM Cart c
                            JOIN user u ON c.Cart_ID = u.Cart_ID""";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Cart cart = new Cart(
                        resultSet.getInt("Cart_ID"),
                        resultSet.getString("startDate"),
                        resultSet.getString("endDate"),
                        resultSet.getString("ISBN"),
                        resultSet.getString("title")
                );
                borrowHistory.add(cart);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowHistory;
    }

    public List<Cart> searchBorrowHistory(String column, String value) {
        List<Cart> searchResults = new ArrayList<>();
        String query;

        // Phân biệt giữa cột kiểu số và chuỗi
        if (column.equals("Cart_ID")) {
            query = "SELECT Cart_ID, startDate, endDate, title, ISBN FROM Cart WHERE " + column + " = ?";
        } else {
            query = "SELECT Cart_ID, startDate, endDate, title, ISBN FROM Cart WHERE " + column + " LIKE ?";
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Gán giá trị cho tham số
            if (column.equals("Cart_ID")) {
                statement.setInt(1, Integer.parseInt(value)); // Với cột kiểu số
            } else {
                statement.setString(1, "%" + value + "%"); // Với cột kiểu chuỗi
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Cart cart = new Cart(
                            resultSet.getInt("Cart_ID"),
                            resultSet.getString("startDate"),
                            resultSet.getString("endDate"),
                            resultSet.getString("ISBN"),
                            resultSet.getString("title")
                    );
                    searchResults.add(cart);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Invalid input for numeric column: " + value);
        }

        return searchResults;
    }


}
