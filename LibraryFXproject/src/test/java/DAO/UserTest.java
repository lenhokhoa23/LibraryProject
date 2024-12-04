package DAO;

import org.example.libraryfxproject.Dao.UserDAO;
import org.example.libraryfxproject.Model.User;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest {
    private static Connection connection;
    private UserDAO userDAO = UserDAO.getInstance();

    @BeforeAll
    public static void setupDatabase() throws SQLException {
        connection = DatabaseInit.getConnection();
    }

    @Test
    @Order(0)
    public void deleteUserForNextRun() {
        boolean userExists = false;

        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM user WHERE username = 'joemama'")) {
            ResultSet rs = stmt.executeQuery();
            userExists = rs.next();
        } catch (SQLException e) {
            fail("Error querying the database: " + e.getMessage());
        }

        try {
            userDAO.deleteUserForNextRun("joemama");
        } catch (Exception e) {
            fail("Error deleting the user: " + e.getMessage());
        }

        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM user WHERE username = 'joemama'")) {
            ResultSet rs = stmt.executeQuery();
            assertFalse(rs.next(), "User should be deleted from the database or not exist initially");
        } catch (SQLException e) {
            fail("Error querying the database after deletion: " + e.getMessage());
        }

        if (!userExists) {
            System.out.println("The user did not exist in the database. Deletion operation handled gracefully.");
        }
    }


    @Test
    @Order(1)
    public void testSaveUserToDatabase() {
        String username = "joemama";
        String name = "Joe Mama";
        String email = "joemama@example.com";
        String phoneNumber = "01234567890";
        String password = "123456";
        String membershipType = "Basic";
        userDAO.saveUserToDatabase(name, email, phoneNumber, username, password, membershipType);

        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM user WHERE username = ?")) {
            stmt.setString(1, username); // Sử dụng tham số "username" (ở đây là "joemama").
            ResultSet rs = stmt.executeQuery();

            // Assert
            if (rs.next()) {
                assertEquals(username, rs.getString("username"), "Username does not match.");
                assertEquals(name, rs.getString("name"), "Name does not match.");
                assertEquals(email, rs.getString("email"), "Email does not match.");
                assertEquals(phoneNumber, rs.getString("phoneNumber"), "Phone number does not match.");
                assertEquals(membershipType, rs.getString("membershipType"), "Membership type does not match.");
                assertEquals(0, rs.getInt("borrowedBooks"), "BorrowedBooks should be 0 for a new user.");
            } else {
                fail("User was not inserted into the database.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database query failed: " + e.getMessage());
        }
    }

    @Test
    @Order(2)
    public void testFindUserByComponentOfUserName() {
        // Arrange
        String component = "viet"; // Part of the username inserted earlier
        List<User> users = userDAO.findUserByComponentOfUserName(component);

        // Assert
        assertFalse(users.isEmpty(), "User list should not be empty");
        assertTrue(users.stream().anyMatch(user -> user.getUsername().contains(component)),
                "There should be a user whose username contains the component");
    }

    @Test
    @Order(3)
    public void testGetUsernameByCartId() {
        // Arrange
        int cartId = 1;
        String expectedUsername = "viettran97";
        String username = userDAO.getUsernameByCartId(cartId);
        assertEquals(expectedUsername, username, "Username should match the expected value");
    }

    @AfterAll
    public static void tearDownDatabase() throws SQLException {
        DatabaseInit.closeConnection(); // Close the connection after tests are complete
    }
}
