package org.example.libraryfxproject.Service;

import org.example.libraryfxproject.Dao.BookDAO;
import org.example.libraryfxproject.Dao.CartDAO;
import org.example.libraryfxproject.Dao.UserDAO;
import org.example.libraryfxproject.Model.Cart;

import java.time.LocalDate;

public class CartService {
    private final CartDAO cartDAO = new CartDAO();
    private final BookDAO bookDAO = new BookDAO();
    private final UserDAO userDAO = new UserDAO();
    private static CartService cartService;

    private CartService() {
        LoadService.loadData(cartDAO);
    }

    public static synchronized CartService getInstance() {
        if (cartService == null) {
            cartService = new CartService();
        }
        return cartService;
    }

    public void addCart(int ID, String startDate, String dueDate, String isbn) {
        String title = bookDAO.fetchTitleFromBooks(isbn, 3);
        String username = userDAO.getUsernameByCartId(ID);
        bookDAO.updateQuantity(username, title,"BORROW");
        Cart cart = new Cart(ID, startDate, dueDate, isbn, title);
        cartDAO.addCart(cart);
    }

    public void deleteCart(String isbn, int ID) {
        String title = bookDAO.fetchTitleFromBooks(isbn, 3);
        String username = userDAO.getUsernameByCartId(ID);
        cartDAO.deleteCart(isbn, ID);
        bookDAO.updateQuantity(username, title, "RETURN");
    }

    public boolean hasBookInCart(String isbn, int cartId) {
        return cartDAO.hasBookInCart(isbn, cartId);
    }

    public boolean hasIDInUser(int ID) {
        String username = userDAO.getUsernameByCartId(ID);
        return username != null;
    }
    public static String getBookStatus(String endDateString) {
        LocalDate currentDate = LocalDate.now();  // Ngày hiện tại

        // Chuyển đổi từ String thành LocalDate (giả sử định dạng yyyy-MM-dd)
        LocalDate endDate = LocalDate.parse(endDateString);

        // So sánh ngày hết hạn và ngày hiện tại
        if (endDate.isBefore(currentDate)) {
            return "Quá hạn";  // Nếu hết hạn
        } else {
            return "Còn hạn";  // Nếu chưa hết hạn
        }
    }
}

