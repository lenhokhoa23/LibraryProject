package org.example.libraryfxproject.Service;

import org.example.libraryfxproject.Dao.BookDAO;
import org.example.libraryfxproject.Dao.CartDAO;
import org.example.libraryfxproject.Dao.UserDAO;
import org.example.libraryfxproject.Model.Cart;

import java.time.LocalDate;

public class CartService {
    private final CartDAO cartDAO;
    private final BookDAO bookDAO;
    private final UserDAO userDAO;
    private static CartService cartService;

    /**
     * Constructor nạp dữ liệu từ các DAO.
     */
    private CartService() {
        userDAO = UserDAO.getInstance();
        bookDAO = BookDAO.getInstance();
        cartDAO = CartDAO.getInstance();
        LoadService.loadData(cartDAO); // Nạp dữ liệu cho CartDAO
    }

    public static synchronized CartService getInstance() {
        if (cartService == null) {
            cartService = new CartService();
        }
        return cartService;
    }

    /**
     * Thêm sách vào giỏ hàng.
     * Cập nhật số lượng sách trong kho và tạo một đối tượng Cart mới để thêm vào CartDAO.
     * @param ID        ID của người dùng (cart ID)
     * @param startDate Ngày bắt đầu mượn
     * @param dueDate   Ngày hết hạn mượn
     * @param isbn      ISBN của cuốn sách
     */
    public void addCart(int ID, String startDate, String dueDate, String isbn) {
        String title = bookDAO.fetchTitleFromBooks(isbn, 3); // Lấy tên sách từ ISBN
        String username = userDAO.getUsernameByCartId(ID); // Lấy tên người dùng từ cart ID
        bookDAO.updateQuantity(username, title, "BORROW"); // Cập nhật số lượng sách khi mượn
        Cart cart = new Cart(ID, startDate, dueDate, title, isbn); // Tạo đối tượng Cart mới
        cartDAO.addCart(cart); // Thêm vào CartDAO
    }

    /**
     * Xóa sách khỏi giỏ hàng.
     * Cập nhật lại số lượng sách trong kho và xóa sách khỏi CartDAO.
     * @param isbn ISBN của cuốn sách cần xóa
     * @param ID   ID của người dùng (cart ID)
     */
    public void deleteCart(String isbn, int ID) {
        String title = bookDAO.fetchTitleFromBooks(isbn, 3); // Lấy tên sách từ ISBN
        String username = userDAO.getUsernameByCartId(ID); // Lấy tên người dùng từ cart ID
        cartDAO.deleteCart(isbn, ID); // Xóa sách khỏi giỏ hàng
        bookDAO.updateQuantity(username, title, "RETURN"); // Cập nhật lại số lượng sách khi trả
    }

    /**
     * Kiểm tra xem cuốn sách có trong giỏ hàng của người dùng hay không.
     * @param isbn   ISBN của cuốn sách cần kiểm tra
     * @param cartId ID của giỏ hàng
     * @return true nếu sách có trong giỏ hàng, false nếu không
     */
    public boolean hasBookInCart(String isbn, int cartId) {
        return cartDAO.hasBookInCart(isbn, cartId); // Kiểm tra trong CartDAO
    }

    /**
     * Trả về mã người dùng khi nhận vào username.
     * @param username username của người dùng
     * @return mã người dùng
     */
    public int getCartIDByUsername(String username) {
        return userDAO.fetchCartIdByUsername(username);
    }

}
