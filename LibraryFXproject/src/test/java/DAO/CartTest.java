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

    @BeforeAll
    public static void setupDatabase() throws SQLException {
        connection = DatabaseInit.getConnection();
    }

    @Test
    @Order(1)
    public void testDeleteCart() {
        // Đảm bảo không có sách ban đầu để xóa
        cartDAO.deleteCart("9789813208988", 1);
        boolean exists = cartDAO.hasBookInCart("9789813208988", 1);
        assertFalse(exists, "Book should be removed from cart if exists.");
    }

    @Test
    @Order(2)
    public void testAddCart() {
        // Thêm sách vào giỏ hàng
        Cart newCart = new Cart(1, LocalDate.now().toString(), LocalDate.now().plusDays(7).toString(),
                "SINGAPORE SCHOOL PRINCIPALS: LEADERSHIP STORIES", "9789813208988");
        cartDAO.addCart(newCart);

        // Kiểm tra sách đã được thêm
        boolean exists = cartDAO.hasBookInCart("9789813208988", 1);
        assertTrue(exists, "Book should be added to cart.");
    }

    @Test
    @Order(3)
    public void testFetchCartByUsername() {
        // Lấy thông tin giỏ hàng của username
        Cart cart = cartDAO.fetchCartByUsername("viettran97");
        assertNotNull(cart, "Cart should exist for user 'viettran97'.");
        assertEquals(1, cart.getCart_ID(), "Cart_ID does not match.");
    }

    @Test
    @Order(5)
    public void testGetBooksStatus() {
        // Lấy danh sách sách theo tiêu đề
        List<Cart> cartList = CartDAO.getBooksStatus("SINGAPORE SCHOOL PRINCIPALS: LEADERSHIP STORIES");
        assertNotNull(cartList, "Cart list should not be null.");
        assertEquals(1, cartList.get(0).getCart_ID(), "Cart_ID does not match.");
    }

    @Test
    @Order(6)
    public void testGetBooksBorrowedCount() {

        int count = cartDAO.getBooksBorrowedCount();

        String sql = "SELECT COUNT(*) FROM cart";
        int bookCount = 0;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                bookCount = rs.getInt(1);
            }
        } catch (SQLException e) {
            fail("Error querying the database: " + e.getMessage());
        }
        assertEquals(bookCount, count, "Books borrowed count does not match.");
    }

    @AfterAll
    public static void tearDownDatabase() throws SQLException {
        DatabaseInit.closeConnection();
    }
}
