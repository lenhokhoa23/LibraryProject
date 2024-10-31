package org.example.libraryfxproject.Service;

import org.example.libraryfxproject.Dao.CartDAO;

public class CartService {
    private final CartDAO cartDAO = new CartDAO();

    public CartService() {
        LoadService loadService = new LoadService();
        loadService.loadData(cartDAO);
    }
}
