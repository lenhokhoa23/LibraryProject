package API;

import org.example.libraryfxproject.Model.Book;
import org.example.libraryfxproject.Service.GoogleBooksService;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class APIGoogleTest {

    private static GoogleBooksService googleBooksService;

    @BeforeAll
    public static void setUp() {
        try {
            googleBooksService = new GoogleBooksService();
        } catch (Exception e) {
            fail("Failed to initialize GoogleBooksService: " + e.getMessage());
        }
    }

    @Test
    @Order(1)
    public void testGetBookByValidISBN() {
        String validISBN = "9780134685991";

        try {
            Book book = googleBooksService.getBookByISBN(validISBN);
            assertNotNull(book, "Book should be returned for a valid ISBN");
            assertEquals("Effective Java", book.getTitle(), "Book title should match");
            assertEquals("Joshua Bloch", book.getAuthor(), "Book author should match");
        } catch (Exception e) {
            fail("Exception occurred while fetching book by ISBN: " + e.getMessage());
        }
    }

    @Test
    @Order(2)
    public void testGetBookByInvalidISBN() {
        String invalidISBN = "123456";

        assertThrows(IllegalArgumentException.class, () -> {
            googleBooksService.getBookByISBN(invalidISBN);
        }, "Expected IllegalArgumentException for invalid ISBN");
    }


    @Test
    @Order(3)
    public void testGetBookByNullISBN() {
        String nullISBN = null;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            googleBooksService.getBookByISBN(nullISBN);
        });
        assertEquals("ISBN không hợp lệ", exception.getMessage(), "Exception message should match");
    }

}
