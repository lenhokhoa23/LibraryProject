package DAO;

import org.example.libraryfxproject.Dao.BookDAO;
import org.junit.jupiter.api.BeforeAll;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInit {
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        }
        return connection;
    }
    
    public static void initializeDatabase() {
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement()) {
            // Tạo bảng books
            String createAccountsTable = "CREATE TABLE IF NOT EXISTS `accounts` ("
                    + "`username` varchar(30) NOT NULL, "
                    + "`password` varchar(30) DEFAULT NULL, "
                    + "`role` varchar(10) DEFAULT NULL, "
                    + "PRIMARY KEY (`username`));";
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(createAccountsTable);
            }

            // Tạo bảng books
            String createBooksTable = "CREATE TABLE IF NOT EXISTS `books` ("
                    + "`no` int(11) NOT NULL AUTO_INCREMENT, "
                    + "`title` varchar(255) NOT NULL, "
                    + "`author` varchar(103) DEFAULT NULL, "
                    + "`pubdate` varchar(19) DEFAULT NULL, "
                    + "`releaseDate` varchar(19) DEFAULT NULL, "
                    + "`ISBN` varchar(13) NOT NULL, "
                    + "`price` varchar(11) DEFAULT NULL, "
                    + "`subject` varchar(44) DEFAULT NULL, "
                    + "`category` varchar(42) DEFAULT NULL, "
                    + "`URL` varchar(70) DEFAULT NULL, "
                    + "`bookType` varchar(46) DEFAULT NULL, "
                    + "`quantity` varchar(15) DEFAULT NULL, "
                    + "PRIMARY KEY (`ISBN`));";
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(createBooksTable);
            }

            // Tạo bảng librarian
            String createLibrarianTable = "CREATE TABLE IF NOT EXISTS `librarian` ("
                    + "`Librarian_ID` int(11) NOT NULL AUTO_INCREMENT, "
                    + "`username` varchar(30) NOT NULL, "
                    + "`name` varchar(50) NOT NULL, "
                    + "`email` varchar(50) NOT NULL, "
                    + "`phoneNumber` varchar(13) DEFAULT NULL, "
                    + "`workShift` varchar(20) DEFAULT NULL, "
                    + "PRIMARY KEY (`Librarian_ID`));";
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(createLibrarianTable);
            }

            // Tạo bảng user
            String createUserTable = "CREATE TABLE IF NOT EXISTS `user` ("
                    + "`Cart_ID` int(11) NOT NULL AUTO_INCREMENT, "
                    + "`username` varchar(30) NOT NULL, "
                    + "`name` varchar(50) NOT NULL, "
                    + "`email` varchar(50) NOT NULL, "
                    + "`phoneNumber` varchar(13) DEFAULT NULL, "
                    + "`borrowedBooks` int(11) NOT NULL, "
                    + "`membershipType` varchar(20) DEFAULT NULL, "
                    + "PRIMARY KEY (`Cart_ID`), "
                    + "FOREIGN KEY (`username`) REFERENCES `accounts`(`username`));";
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(createUserTable);
            }

            // Tạo bảng cart
            String createCartTable = "CREATE TABLE IF NOT EXISTS `cart` ("
                    + "`Cart_ID` int(11) NOT NULL, "
                    + "`startDate` date NOT NULL, "
                    + "`endDate` varchar(19) NOT NULL, "
                    + "`title` varchar(255) NOT NULL, "
                    + "`ISBN` varchar(13) NOT NULL, "
                    + "FOREIGN KEY (`Cart_ID`) REFERENCES `user`(`Cart_ID`), "
                    + "FOREIGN KEY (`ISBN`) REFERENCES `books`(`ISBN`));";
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(createCartTable);
            }

            // Thêm dữ liệu vào bảng accounts
            String insertAccounts = "INSERT INTO `accounts` (`username`, `password`, `role`) "
                    + "VALUES "
                    + "('admin', 'admin123', 'admin'), "
                    + "('john_doe', 'password123', 'user'), "
                    + "('jane_doe', 'password456', 'user');";
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(insertAccounts);
            }

            // Thêm dữ liệu vào bảng books
            String insertBooks = "INSERT INTO `books` (`title`, `author`, `pubdate`, `releaseDate`, `ISBN`, `price`, `subject`, `category`, `URL`, `bookType`, `quantity`) "
                    + "VALUES "
                    + "('Effective Java', 'Joshua Bloch', '2001-01-01', '2001-02-01', '9780134685111', '45.99', 'Programming', 'Computing', 'http://example.com', 'Hardcover', '5'), "
                    + "('Clean Code', 'Robert C. Martin', '2008-08-01', '2008-09-01', '9780132350884', '39.99', 'Software Engineering', 'Computing', 'http://example.com', 'Paperback', '3'), "
                    + "('The Pragmatic Programmer', 'Andrew Hunt', '1999-03-01', '1999-04-01', '9780201616224', '42.50', 'Software Development', 'Computing', 'http://example.com', 'Hardcover', '7');";
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(insertBooks);
            }

            // Thêm dữ liệu vào bảng librarian
            String insertLibrarian = "INSERT INTO `librarian` (`username`, `name`, `email`, `phoneNumber`, `workShift`) "
                    + "VALUES "
                    + "('librarian1', 'Alice', 'alice@example.com', '1234567890', 'Morning'), "
                    + "('librarian2', 'Bob', 'bob@example.com', '0987654321', 'Afternoon');";
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(insertLibrarian);
            }

            // Thêm dữ liệu vào bảng user
            String insertUser = "INSERT INTO `user` (`username`, `name`, `email`, `phoneNumber`, `borrowedBooks`, `membershipType`) "
                    + "VALUES "
                    + "('john_doe', 'John Doe', 'john@example.com', '1112233445', 2, 'Premium'), "
                    + "('jane_doe', 'Jane Doe', 'jane@example.com', '2233445566', 1, 'Standard');";
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(insertUser);
            }

            // Thêm dữ liệu vào bảng cart
            String insertCart = "INSERT INTO `cart` (`Cart_ID`, `startDate`, `endDate`, `title`, `ISBN`) "
                    + "VALUES "
                    + "(1, '2024-11-01', '2024-11-15', 'Effective Java', '9780134685111'), "
                    + "(2, '2024-11-01', '2024-11-15', 'Clean Code', '9780132350884');";
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(insertCart);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Đóng kết nối sau khi test xong
    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

}
