import org.example.libraryfxproject.Dao.BookDAO;
import org.example.libraryfxproject.Model.Book;
import org.example.libraryfxproject.Service.BookService;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookTest {
    private static Connection connection;
    private BookDAO bookDAO = new BookDAO();
    private BookService bookService = BookService.getInstance();
    @BeforeAll
    public static void setupDatabase() throws SQLException {
        DatabaseInit.initializeDatabase(); // Initialize the database with tables and test data
    }

    @AfterAll
    public static void tearDownDatabase() throws SQLException {
        DatabaseInit.closeConnection(); // Close the connection after tests are complete
    }

    @Test
    public void testInsertBookToDatabase() {
        String title = "Effective Java";
        String author = "Joshua Bloch";
        String pubdateStr = "01-Jan-01";
        String releaseDateStr = "01-Feb-01";
        String ISBN = "9780134685111";
        String price = "45.99";
        String subject = "Programming";
        String category = "Computing";
        String URL = "http://example.com";
        String bookType = "Hardcover";
        String quantity = "5";

        bookDAO.insertBookToDatabase(title, author, pubdateStr, releaseDateStr, ISBN, price, subject, category, URL, bookType, quantity);

        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM books WHERE ISBN = ?")) {
            stmt.setString(1, ISBN);
            ResultSet rs = stmt.executeQuery();

            assertTrue(rs.next(), "Book should be added to database");
            assertEquals(title, rs.getString("title"));
            assertEquals(author, rs.getString("author"));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yy");
            LocalDate expectedPubDate = LocalDate.parse(pubdateStr, formatter);
            LocalDate expectedReleaseDate = LocalDate.parse(releaseDateStr, formatter);
            assertEquals(Date.valueOf(expectedPubDate), rs.getDate("pubdate"));
            assertEquals(Date.valueOf(expectedReleaseDate), rs.getDate("releaseDate"));
            assertEquals(price, rs.getString("price"));
            assertEquals(subject, rs.getString("subject"));
            assertEquals(category, rs.getString("category"));
            assertEquals(URL, rs.getString("URL"));
            assertEquals(bookType, rs.getString("bookType"));
            assertEquals(quantity, rs.getString("quantity"));
        } catch (SQLException e) {
            fail("Error querying the database: " + e.getMessage());
        }
    }

    @Test
    public void testFindBookByDistinctAttribute1() {
        String ISBN = "9780134685111";
        int searchType = 3; // Search by ISBN

        // Call the method to find the book
        Book book = bookDAO.findBookByDistinctAttribute(ISBN, searchType);

        assertNotNull(book, "Book should be found by ISBN");
        assertEquals(ISBN, book.getISBN(), "ISBN should match");
        assertEquals("Effective Java", book.getTitle(), "Title should match");
        assertEquals("Joshua Bloch", book.getAuthor(), "Author should match");
    }

    @Test
    public void testFindBooksByAttribute() {
        String title = "Effective Java";
        String attribute = "title";

        // Call the method to find books by attribute
        List<Book> books = bookDAO.findBooksByAttribute(attribute, title);

        assertNotNull(books, "Books list should not be null");
        assertEquals(1, books.size(), "Only one book should be found");
        assertEquals(title, books.get(0).getTitle(), "Title should match");
    }

    @Test
    public void testFetchQuantityFromBooks() {
        String title = "Effective Java";
        int searchType = 2; // Search by title

        // Call the method to fetch quantity
        int quantity = bookDAO.fetchQuantityFromBooks(title, searchType);

        assertEquals(5, quantity, "Quantity should match");
    }

    @Test
    public void testFetchISBNFromBooks() {
        String title = "Effective Java";
        int searchType = 2; // Search by title

        // Call the method to fetch ISBN
        String ISBN = bookDAO.fetchISBNFromBooks(title, searchType);

        assertEquals("9780134685111", ISBN, "ISBN should match");
    }

    @Test
    public void testFetchTitleFromBooks() {
        String ISBN = "9780134685111";
        int searchType = 3; // Search by ISBN

        // Call the method to fetch title
        String title = bookDAO.fetchTitleFromBooks(ISBN, searchType);

        assertEquals("Effective Java", title, "Title should match");
    }

    @Test
    public void testFindBookByDistinctAttribute2() {
        String type = "InvalidTitle";
        int searchType = 999; // Invalid search type

        // Check if IllegalArgumentException is thrown
        assertThrows(IllegalArgumentException.class, () -> bookDAO.findBookByDistinctAttribute(type, searchType),
                "Invalid search type should throw IllegalArgumentException");
    }

    @Test
    public void testModifyBookAttribute() {
        String ISBN = "9780134685111";
        String attribute = "price"; // The attribute to modify
        String newValue = "50.99"; // New price

        // Check the current price
        String oldPrice = bookDAO.fetchPriceFromBooks(ISBN, 3); // Assuming this method fetches price by ISBN
        assertEquals("45.99", oldPrice, "Price should be the initial value");

        // Call the method to modify the book attribute
        bookDAO.modifyBookAttribute(ISBN, attribute, newValue);

        // Check if the price was updated
        String updatedPrice = bookDAO.fetchPriceFromBooks(ISBN, 3);
        assertEquals(newValue, updatedPrice, "Price should be updated to the new value");
    }

    @Test
    public void testModifyBookAttribute2() {
        String ISBN = "9999999999"; // Non-existent ISBN
        String attribute = "price";
        String newValue = "50.99";

        // Call the method to modify book attribute with a non-existent ISBN
        bookDAO.modifyBookAttribute(ISBN, attribute, newValue);

        // Check that no book with this ISBN exists
        String nonExistentBookPrice = bookDAO.fetchPriceFromBooks(ISBN, 3);
        assertNull(nonExistentBookPrice, "Price should not be updated for a non-existent book");
    }

    @Test
    public void testUpdateQuantity_Borrow() throws SQLException {
        String username = "viettran97";
        String bookName = "Effective Java";
        String operation = "BORROW";

        bookDAO.updateQuantity(username, bookName, operation);

        try (Connection connection = DatabaseInit.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT quantity FROM books WHERE title = ?")) {
            stmt.setString(1, bookName);
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next(), "Book should be found in the database");
            assertEquals(4, rs.getInt("quantity"), "Quantity should be decreased by 1");
        }

        try (Connection connection = DatabaseInit.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT borrowedBooks FROM user WHERE username = ?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next(), "User should be found in the database");
            assertEquals(1, rs.getInt("borrowedBooks"), "User's borrowed books should be increased by 1");
        }
    }
}
