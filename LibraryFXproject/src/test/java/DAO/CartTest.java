package DAO;
import org.example.libraryfxproject.Dao.CartDAO;
import org.example.libraryfxproject.Model.Cart;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CartTest {
    private static Connection connection;
    private CartDAO cartDAO = new CartDAO();

    // Set up an in-memory database before running tests
    @BeforeAll
    public static void setupDatabase() throws SQLException {
        connection = DatabaseInit.getConnection(); // Get the connection to the in-memory database
    }

    @Test
    @Order(1)
    public void testDeleteCart() {
        // Đảm bảo không có sách ban đầu để xóa
        cartDAO.deleteCart("9780134685111", 50);
        boolean exists = cartDAO.hasBookInCart("9780134685111", 50);
        assertFalse(exists, "Book should be removed from cart if exists.");
    }

    @Test
    @Order(2)
    public void testAddCart() {
        // Thêm sách vào giỏ hàng
        Cart newCart = new Cart(50, LocalDate.now().toString(), LocalDate.now().plusDays(7).toString(),
                "Effective Java", "9780134685111");
        cartDAO.addCart(newCart);

        // Kiểm tra sách đã được thêm
        boolean exists = cartDAO.hasBookInCart("9780134685111", 50);
        assertTrue(exists, "Book should be added to cart.");
    }

    @Test
    @Order(3)
    public void testFetchCartByUsername() {
        // Lấy thông tin giỏ hàng của username
        Cart cart = cartDAO.fetchCartByUsername("joemama");
        assertNotNull(cart, "Cart should exist for user 'joemama'.");
        assertEquals(50, cart.getCart_ID(), "Cart_ID does not match.");
        assertEquals("Effective Java", cart.getTitle(), "Title does not match.");
        assertEquals("9780134685111", cart.getISBN(), "ISBN does not match.");
    }

    @Test
    @Order(4)
    public void testFetchFromCart() {
        // Lấy từng thuộc tính từ giỏ hàng
        String isbn = cartDAO.fetchFromCart("joemama", "ISBN");
        assertEquals("9780134685111", isbn, "ISBN does not match.");

        String title = cartDAO.fetchFromCart("joemama", "title");
        assertEquals("Effective Java", title, "Title does not match.");
    }

    @Test
    @Order(5)
    public void testGetBooksStatus() {
        // Lấy danh sách sách theo tiêu đề
        List<Cart> cartList = CartDAO.getBooksStatus("Effective Java");
        assertNotNull(cartList, "Cart list should not be null.");
        assertEquals(1, cartList.size(), "Cart list size does not match.");
        assertEquals(50, cartList.get(0).getCart_ID(), "Cart_ID does not match.");
    }

    @Test
    @Order(6)
    public void testGetBooksBorrowedCount() {
        // Đếm số sách đã mượn
        int count = cartDAO.getBooksBorrowedCount();
        assertEquals(163, count, "Books borrowed count does not match.");
    }

    @Test
    @Order(7)
    public void testGetOverdueBooksCount() {
        // Đếm số sách quá hạn
        int overdueCount = cartDAO.getOverdueBooksCount();
        assertEquals(159, overdueCount, "Overdue books count should be 0.");
    }

    @AfterAll
    public static void tearDownDatabase() throws SQLException {
        DatabaseInit.closeConnection();  // Close the in-memory database connection
    }
}
