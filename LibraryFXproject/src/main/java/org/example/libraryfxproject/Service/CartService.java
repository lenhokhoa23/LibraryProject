package org.example.libraryfxproject.Service;

import org.example.libraryfxproject.Dao.BookDAO;
import org.example.libraryfxproject.Dao.CartDAO;
import org.example.libraryfxproject.Dao.UserDAO;
import org.example.libraryfxproject.Model.Cart;

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
}
