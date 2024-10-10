package org.example.javaapplicationproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Thông tin kết nối
    private static final String URL = "jdbc:mysql://localhost:3307/bookdatabase";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Phương thức kết nối với cơ sở dữ liệu
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

