package org.example.libraryfxproject.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Lớp này chịu trách nhiệm thiết lập kết nối tới cơ sở dữ liệu MySQL.
 * Phương thức `getConnection()` sẽ trả về một kết nối hợp lệ để thao tác với cơ sở dữ liệu.
 */
public class DatabaseConnection {

    // Thông tin kết nối tới cơ sở dữ liệu
    private static final String URL = "jdbc:mysql://localhost:3307/bookdatabase";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    /**
     * Thiết lập kết nối tới cơ sở dữ liệu MySQL.
     *
     * @return Một đối tượng {@link Connection} nếu kết nối thành công, null nếu có lỗi xảy ra.
     */
    @SuppressWarnings("exports")
    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Đăng ký driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Kết nối tới cơ sở dữ liệu
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (ClassNotFoundException e) {
            System.out.println("Không tìm thấy driver MySQL.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Kết nối thất bại!");
            e.printStackTrace();
        }
        return connection;
    }
}
