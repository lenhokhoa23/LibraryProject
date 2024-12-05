package Service;

import DAO.DatabaseInit;
import org.example.libraryfxproject.Dao.BookDAO;
import org.example.libraryfxproject.Service.BookService;
import org.example.libraryfxproject.Model.Book;
import org.junit.jupiter.api.*;

import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceTest {
    private static Connection connection;
    private BookDAO bookDAO = BookDAO.getInstance();
    private BookService bookService = BookService.getInstance();
    // Set up an in-memory database before running tests
    @BeforeAll
    public static void setupDatabase() throws SQLException {
        connection = DatabaseInit.getConnection(); // Get the connection to the in-memory database
    }

    @Test
    public void testInvalidISBN() {
        // Thông tin sách với ISBN không hợp lệ (dài hơn 13 chữ số)
        String title = "Effective Java";
        String author = "Joshua Bloch";
        String pubdateStr = "01-Jan-01";
        String releaseDateStr = "01-Feb-01";
        String ISBN = "978013468511122";  // ISBN sai: dài hơn 13 ký tự
        String price = "45.99";
        String subject = "Programming";
        String category = "Computing";
        String URL = "http://example.com";
        String bookType = "Hardcover";
        String quantity = "5";
        Book book = new Book(9999, title, author, pubdateStr, releaseDateStr,
                ISBN, price, subject, category, URL, bookType, quantity);
        // Kiểm tra phương thức validateAddBookInput
        int result = bookService.validateAddBookInput(book);

        // Kiểm tra kết quả trả về (1 có nghĩa là ISBN không hợp lệ)
        assertEquals(1, result, "ISBN should be invalid");
    }

    // Test cho trường hợp title không hợp lệ
    @Test
    public void testInvalidTitle() {
        String title = "Effective@Java"; // Title có ký tự đặc biệt
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

        Book book = new Book(9999, title, author, pubdateStr, releaseDateStr,
                ISBN, price, subject, category, URL, bookType, quantity);
        // Kiểm tra phương thức validateAddBookInput
        int result = bookService.validateAddBookInput(book);

        // Kiểm tra kết quả trả về (2 có nghĩa là title không hợp lệ)
        assertEquals(2, result, "Title should be invalid due to special characters");
    }

    // Test cho trường hợp price không hợp lệ
    @Test
    public void testInvalidPrice() {
        String title = "Effective Java";
        String author = "Joshua Bloch";
        String pubdateStr = "01-Jan-01";
        String releaseDateStr = "01-Feb-01";
        String ISBN = "9780134685111";
        String price = "abc"; // Giá không hợp lệ (chuỗi)
        String subject = "Programming";
        String category = "Computing";
        String URL = "http://example.com";
        String bookType = "Hardcover";
        String quantity = "5";

        Book book = new Book(9999, title, author, pubdateStr, releaseDateStr,
                ISBN, price, subject, category, URL, bookType, quantity);
        // Kiểm tra phương thức validateAddBookInput
        int result = bookService.validateAddBookInput(book);

        // Kiểm tra kết quả trả về (3 có nghĩa là price không hợp lệ)
        assertEquals(3, result, "Price should be invalid due to non-numeric value");
    }

    // Test cho trường hợp subject không hợp lệ
    @Test
    public void testInvalidSubject() {
        String title = "Effective Java";
        String author = "Joshua Bloch";
        String pubdateStr = "01-Jan-01";
        String releaseDateStr = "01-Feb-01";
        String ISBN = "9780134685111";
        String price = "45.99";
        String subject = "Programming123"; // Subject không hợp lệ (chứa số)
        String category = "Computing";
        String URL = "http://example.com";
        String bookType = "Hardcover";
        String quantity = "5";

        Book book = new Book(9999, title, author, pubdateStr, releaseDateStr,
                ISBN, price, subject, category, URL, bookType, quantity);
        // Kiểm tra phương thức validateAddBookInput
        int result = bookService.validateAddBookInput(book);

        // Kiểm tra kết quả trả về (4 có nghĩa là subject không hợp lệ)
        assertEquals(4, result, "Subject should be invalid due to numbers");
    }

    // Test cho trường hợp quantity không hợp lệ
    @Test
    public void testInvalidQuantity() {
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
        String quantity = "five"; // Quantity invalid

        Book book = new Book(9999, title, author, pubdateStr, releaseDateStr,
                ISBN, price, subject, category, URL, bookType, quantity);
        // Kiểm tra phương thức validateAddBookInput
        int result = bookService.validateAddBookInput(book);

        // Kiểm tra kết quả
        assertEquals(5, result, "Quantity should be invalid due to non-numeric value");
    }

    @AfterAll
    public static void tearDownDatabase() throws SQLException {
        DatabaseInit.closeConnection();  // Close the in-memory database connection
    }
}

