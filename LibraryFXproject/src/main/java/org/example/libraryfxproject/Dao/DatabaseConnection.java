package org.example.libraryfxproject.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL;
    private static final String USER = "root";
    private static final String PASSWORD = "";

    static {
        // Choose database based on environment (test or production)
        if ("test".equals(System.getProperty("env"))) {
            // In-memory H2 database for testing
            URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
        } else {
            // MySQL database for production
            URL = "jdbc:mysql://localhost:3307/bookdatabase";
        }
    }
    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failed.");
            e.printStackTrace();
        }
        return connection;
    }
}

