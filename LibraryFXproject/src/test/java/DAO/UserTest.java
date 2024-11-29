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
    private UserDAO userDAO = new UserDAO();

    @BeforeAll
    public static void setupDatabase() throws SQLException {
        DatabaseInit.initializeDatabase();
        connection = DatabaseInit.getConnection();
    }

    @Test
    @Order(0)
    public void deleteUserForNextRun() {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM user WHERE username = 'joemama'")) {
            ResultSet rs = stmt.executeQuery();
            System.out.println(rs.getString(1) + "\n");
            System.out.println(rs.getString(2));
            if (rs.next()) {
                userDAO.deleteUserForNextRun("joemama");
            } else {
                fail("User not found in the database.");
            }
        } catch (SQLException e) {
            fail("Error querying the database: " + e.getMessage());
        }
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM user WHERE username = 'joemama'")) {
            ResultSet rs = stmt.executeQuery();
            assertFalse(rs.next(), "User should be deleted from the database");
        } catch (SQLException e) {
            fail("Error querying the database after deletion: " + e.getMessage());
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
        // Act
        userDAO.saveUserToDatabase(name, email, phoneNumber, username, password, membershipType);
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM user WHERE username = ?")) {
            stmt.setString(1, "joemama");
            ResultSet rs = stmt.executeQuery();
            // Assert
            User user = userDAO.findUserByUsername(username);
            System.out.println(user.getName());
            assertNotNull(user, "User should be found in the database");
            assertEquals(username, user.getUsername(), "Usernames should match");
            assertEquals(name, user.getName(), "Names should match");
            assertEquals(email, user.getEmail(), "Emails should match");
            assertEquals(phoneNumber, user.getPhoneNumber(), "Phone numbers should match");
            assertEquals(membershipType, user.getMembershipType(), "Membership types should match");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
        @Test
    @Order(2)
    public void testFindUserByComponentOfUserName() {
        // Arrange
        String component = "john"; // Part of the username inserted earlier

        // Act
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
        int cartId = 1; // Assuming this cart ID exists in the database
        String expectedUsername = "john_doe";

        // Act
        String username = userDAO.getUsernameByCartId(cartId);

        // Assert
        assertEquals(expectedUsername, username, "Username should match the expected value");
    }

    @AfterAll
    public static void tearDownDatabase() throws SQLException {
        DatabaseInit.closeConnection(); // Close the connection after tests are complete
    }
}
