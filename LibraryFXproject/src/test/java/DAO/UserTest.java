import org.example.libraryfxproject.Dao.UserDAO;
import org.example.libraryfxproject.Model.User;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private static Connection connection;
    private UserDAO userDAO = new UserDAO();

    public static void setupDatabase() throws SQLException {
        DatabaseInit.initializeDatabase(); // Initialize the database with tables and test data
    }

    @AfterAll
    public static void tearDownDatabase() throws SQLException {
        DatabaseInit.closeConnection(); // Close the connection after tests are complete
    }
}
