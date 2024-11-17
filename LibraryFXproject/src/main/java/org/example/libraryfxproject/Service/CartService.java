package org.example.libraryfxproject.Service;

import org.example.libraryfxproject.Dao.CartDAO;

public class CartService {
    private final CartDAO cartDAO = new CartDAO();
    private static CartService cartService;

    private CartService() {
        LoadService loadService = LoadService.getInstance();
        loadService.loadData(cartDAO);
    }
    public static synchronized CartService getInstance() {
        if (cartService == null) {
            cartService = new CartService();
        }
        return cartService;
    }

}
