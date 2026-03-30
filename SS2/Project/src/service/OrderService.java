package service;

import dao.OrderDAO;
import dao.ProductDAO;
import model.Product;

public class OrderService {

    private OrderDAO orderDAO = new OrderDAO();
    private ProductDAO productDAO = new ProductDAO();

    public void placeOrder(int userId, int productId, int quantity, String address) {

        Product p = productDAO.findById(productId);

        if (p == null || p.getStock() < quantity) {
            System.out.println("Không đủ hàng!");
            return;
        }

        double total = p.getPrice() * quantity;

        int orderId = orderDAO.createOrder(userId, total, address);

        orderDAO.addOrderDetail(orderId, productId, quantity, p.getPrice());

        productDAO.updateStock(productId, quantity);

        System.out.println("Đặt hàng thành công!");
    }
}