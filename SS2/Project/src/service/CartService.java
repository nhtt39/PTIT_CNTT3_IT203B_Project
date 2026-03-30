package service;

import dao.CartDAO;

public class CartService {

    CartDAO dao = new CartDAO();

    public void addToCart(int userId, int productId, int quantity) {
        dao.addToCart(userId, productId, quantity);
    }
}