package dao;

import java.sql.*;

public class OrderDAO {

    public int createOrder(int userId, double total, String address) {
        String sql = "INSERT INTO orders(user_id,total_price,shipping_address) VALUES(?,?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, userId);
            ps.setDouble(2, total);
            ps.setString(3, address);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void addOrderDetail(int orderId, int productId, int quantity, double price) {
        String sql = "INSERT INTO order_details(order_id,product_id,quantity,price) VALUES(?,?,?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.setDouble(4, price);

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}