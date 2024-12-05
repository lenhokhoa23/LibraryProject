package org.example.libraryfxproject.Dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.libraryfxproject.Model.Cart;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp này chịu trách nhiệm thao tác với cơ sở dữ liệu Cart, bao gồm các phương thức
 * để tải dữ liệu từ cơ sở dữ liệu, tìm kiếm các giỏ hàng theo username, kiểm tra
 * tình trạng sách, số lượng sách đã mượn, số lượng sách quá hạn, và lấy các hoạt động.
 */
public class CartDAO extends GeneralDAO<Integer, Cart> {

    private static CartDAO cartDAO;

    public static synchronized CartDAO getInstance() {
        if (cartDAO == null) {
            cartDAO = new CartDAO();
        }
        return cartDAO;
    }

    /**
     * Tải tất cả dữ liệu từ bảng Cart trong cơ sở dữ liệu .
     */
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

                // Tạo đối tượng Cart và thêm vào HashMap
                Cart cart = new Cart(cartId, startDate, endDate, isbn, title);
                dataMap.put(cartId, cart);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lấy thông tin giỏ hàng của người dùng theo tên đăng nhập.
     *
     * @param username tên đăng nhập của người dùng.
     * @return đối tượng {@link Cart} của người dùng hoặc null nếu không tìm thấy.
     */
    public Cart fetchCartByUsername(String username) {
        Cart cart = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // Truy vấn giỏ hàng theo username sử dụng JOIN
            String query = "SELECT c.Cart_ID, c.startDate, c.endDate, c.ISBN, c.title " +
                    "FROM user u JOIN cart c ON u.Cart_ID = c.Cart_ID " +
                    "WHERE u.username = ?";
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, username); // Đặt tham số username
            rs = pstmt.executeQuery();
            if (rs.next()) {
                // Tạo đối tượng Cart với thông tin từ resultSet
                int cartID = rs.getInt("Cart_ID");
                String startDate = rs.getString("startDate");
                String endDate = rs.getString("endDate");
                String isbn = rs.getString("ISBN");
                String title = rs.getString("title");
                cart = new Cart(cartID, startDate, endDate, title, isbn); // Tạo đối tượng Cart
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return cart; // Trả về đối tượng Cart hoặc null nếu không tìm thấy
    }

    /**
     * Lấy danh sách các giỏ hàng có sách với tiêu đề nhất định.
     *
     * @param bookTitle tiêu đề sách cần tìm.
     * @return danh sách {@link Cart} chứa sách với tiêu đề tìm được.
     */
    public static List<Cart> getBooksStatus(String bookTitle) {
        List<Cart> cartList = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM cart WHERE title = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, bookTitle);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cart cart = new Cart(
                        rs.getInt("Cart_ID"),
                        rs.getString("startDate"),
                        rs.getString("endDate"),
                        rs.getString("title"),
                        rs.getString("ISBN")
                );
                cartList.add(cart);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartList;
    }

    /**
     * Lấy số lượng sách đã mượn từ bảng Cart.
     *
     * @return số lượng sách đã mượn.
     */
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

    /**
     * Lấy số lượng sách quá hạn từ bảng Cart.
     *
     * @return số lượng sách quá hạn.
     */
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

    /**
     * Lấy danh sách các hoạt động (giỏ hàng) gần đây trong hệ thống, giới hạn bởi tham số.
     *
     * @param limit giới hạn số lượng hoạt động cần lấy (nếu có).
     * @return danh sách {@link ObservableList} chứa các hoạt động.
     */
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

    public ObservableList<ObservableList<String>> getActivities(int studentID, String startDate, String endDate) {
        ObservableList<ObservableList<String>> activities = FXCollections.observableArrayList();
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                StringBuilder queryBuilder = new StringBuilder(
                        "SELECT c.startDate AS time, c.Cart_ID AS userID, u.username, " +
                                "c.title AS bookTitle, c.ISBN, c.endDate AS due " +
                                "FROM cart c JOIN user u ON c.Cart_ID = u.Cart_ID WHERE 1=1 "
                );

                if (studentID > 0) {
                    queryBuilder.append("AND u.Cart_ID = ? ");
                }

                if (startDate != null && !startDate.isEmpty()) {
                    queryBuilder.append("AND c.startDate >= ? ");
                }

                if (endDate != null && !endDate.isEmpty()) {
                    queryBuilder.append("AND c.endDate <= ? ");
                }

                PreparedStatement statement = conn.prepareStatement(queryBuilder.toString());

                int paramIndex = 1;
                if (studentID > 0) {
                    statement.setInt(paramIndex++, studentID);
                }

                if (startDate != null && !startDate.isEmpty()) {
                    statement.setString(paramIndex++, startDate);
                }
                if (endDate != null && !endDate.isEmpty()) {
                    statement.setString(paramIndex, endDate);
                }

                ResultSet result = statement.executeQuery();
                System.out.println(statement.toString());
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

    /**
     * Lấy các hoạt động giỏ hàng theo Cart_ID.
     *
     * @param cartId ID của giỏ hàng.
     * @return Danh sách các hoạt động giỏ hàng theo Cart_ID.
     */
    public ObservableList<ObservableList<String>> getActivitiesByCartId(int cartId) {
        ObservableList<ObservableList<String>> activities = FXCollections.observableArrayList();

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                String query = "SELECT c.startDate AS time, c.Cart_ID AS userID, u.username, " +
                        "c.title AS bookTitle, c.ISBN, c.endDate AS due " +
                        "FROM cart c " +
                        "JOIN user u ON c.Cart_ID = u.Cart_ID " +
                        "WHERE c.Cart_ID = ? ORDER BY c.startDate DESC";

                PreparedStatement statement = conn.prepareStatement(query);
                statement.setInt(1, cartId);
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

    /**
     * Thêm một giỏ hàng mới vào cơ sở dữ liệu.
     *
     * @param cart Đối tượng Cart cần thêm.
     */
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

    /**
     * Xóa sách khỏi giỏ hàng theo ISBN và Cart_ID.
     *
     * @param isbn ISBN của sách cần xóa.
     * @param cartId ID của giỏ hàng.
     */
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

    /**
     * Kiểm tra xem sách có tồn tại trong giỏ hàng hay không.
     *
     * @param isbn ISBN của sách.
     * @param cartId ID của giỏ hàng.
     * @return true nếu sách có trong giỏ hàng, false nếu không.
     */
    public boolean hasBookInCart(String isbn, int cartId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "SELECT 1 FROM cart WHERE ISBN = ? AND Cart_ID = ? LIMIT 1";
        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, isbn);
            preparedStatement.setInt(2, cartId);
            resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
