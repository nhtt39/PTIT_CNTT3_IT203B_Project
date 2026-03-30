package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public List<String> getCart(int userId) {
        List<String> list = new ArrayList<>();
        String sql = "SELECT p.name, c.quantity FROM cart c JOIN products p ON c.product_id = p.id WHERE c.user_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(rs.getString("name") + " - SL: " + rs.getInt("quantity"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}