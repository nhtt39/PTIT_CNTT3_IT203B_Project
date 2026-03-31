package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // Tạo đơn hàng (khách hàng đặt)
    public int createOrder(int userId, double total, String address) {
        String sql = "INSERT INTO orders(user_id,total_price,shipping_address,status) VALUES(?,?,?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, userId);
            ps.setDouble(2, total);
            ps.setString(3, address);
            ps.setString(4, "PENDING"); // mặc định đơn mới là PENDING

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Thêm chi tiết đơn hàng
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

    // Lấy tất cả đơn hàng để admin xem (có chi tiết sản phẩm)
    public List<String> getAllOrdersWithDetails() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT o.id AS order_id, o.user_id, o.total_price, o.shipping_address, o.status " +
                "FROM orders o ORDER BY o.id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                int userId = rs.getInt("user_id");
                double total = rs.getDouble("total_price");
                String address = rs.getString("shipping_address");
                String status = rs.getString("status");

                // Lấy chi tiết sản phẩm
                String detailSql = "SELECT od.product_id, od.quantity, od.price, p.name " +
                        "FROM order_details od JOIN products p ON od.product_id = p.id " +
                        "WHERE od.order_id=?";
                PreparedStatement ps2 = conn.prepareStatement(detailSql);
                ps2.setInt(1, orderId);
                ResultSet rs2 = ps2.executeQuery();

                StringBuilder products = new StringBuilder();
                while (rs2.next()) {
                    products.append(rs2.getString("name"))
                            .append(" x")
                            .append(rs2.getInt("quantity"))
                            .append(" (")
                            .append(rs2.getDouble("price"))
                            .append("), ");
                }
                if (products.length() > 0) products.setLength(products.length() - 2);

                list.add("OrderID: " + orderId +
                        " | UserID: " + userId +
                        " | Products: " + products.toString() +
                        " | Total: " + total +
                        " | Address: " + address +
                        " | Status: " + status);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Cập nhật trạng thái đơn hàng (admin)
    public void updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE orders SET status=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, orderId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Lấy đơn hàng của 1 khách hàng (customer)
    public List<String> getOrdersByUser(int userId) {
        List<String> list = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(
                        "OrderID: " + rs.getInt("id") +
                                " | Total: " + rs.getDouble("total_price") +
                                " | Address: " + rs.getString("shipping_address") +
                                " | Status: " + rs.getString("status")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}