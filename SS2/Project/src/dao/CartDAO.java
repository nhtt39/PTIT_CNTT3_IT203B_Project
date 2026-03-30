package dao;

import java.sql.*;

public class CartDAO {

    public void addToCart(int userId, int productId, int quantity) {
        String sql = "INSERT INTO cart(user_id, product_id, quantity) VALUES(?,?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}