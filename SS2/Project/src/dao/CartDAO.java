package dao;

import dao.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    // Hiển thị giỏ hàng theo user
    public List<String> getCart(int userId) {
        List<String> list = new ArrayList<>();
        String sql = "SELECT c.product_id, p.name, c.quantity, p.price " +
                "FROM cart c JOIN products p ON c.product_id = p.id " +
                "WHERE c.user_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int pid = rs.getInt("product_id");
                String name = rs.getString("name");
                int qty = rs.getInt("quantity");
                double price = rs.getDouble("price");

                list.add("ProductID: " + pid + " | " + name +
                        " | Quantity: " + qty + " | Price: " + price);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Integer> getProductIds(int userId) {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT product_id FROM cart WHERE user_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ids.add(rs.getInt("product_id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ids;
    }

    public List<Integer> getQuantities(int userId) {
        List<Integer> qtys = new ArrayList<>();
        String sql = "SELECT quantity FROM cart WHERE user_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                qtys.add(rs.getInt("quantity"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return qtys;
    }

    public void addToCart(int userId, int productId, int quantity) {
        String checkSql = "SELECT quantity FROM cart WHERE user_id=? AND product_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement psCheck = conn.prepareStatement(checkSql)) {

            psCheck.setInt(1, userId);
            psCheck.setInt(2, productId);

            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                int existingQty = rs.getInt("quantity");
                String updateSql = "UPDATE cart SET quantity=? WHERE user_id=? AND product_id=?";
                try (PreparedStatement psUpdate = conn.prepareStatement(updateSql)) {
                    psUpdate.setInt(1, existingQty + quantity);
                    psUpdate.setInt(2, userId);
                    psUpdate.setInt(3, productId);
                    psUpdate.executeUpdate();
                }
            } else {
                String insertSql = "INSERT INTO cart(user_id, product_id, quantity) VALUES(?,?,?)";
                try (PreparedStatement psInsert = conn.prepareStatement(insertSql)) {
                    psInsert.setInt(1, userId);
                    psInsert.setInt(2, productId);
                    psInsert.setInt(3, quantity);
                    psInsert.executeUpdate();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearCart(int userId) {
        String sql = "DELETE FROM cart WHERE user_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeFromCart(int userId, int productId) {
        String sql = "DELETE FROM cart WHERE user_id=? AND product_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}