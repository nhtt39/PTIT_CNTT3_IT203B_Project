package presentation;

import dao.CategoryDAO;
import dao.ProductDAO;
import dao.CartDAO;
import model.Category;
import model.Product;
import service.OrderService;
import dao.OrderDAO;
import dao.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Scanner;

public class CustomerMenu {

    private Scanner sc = new Scanner(System.in);
    private ProductDAO productDAO = new ProductDAO();
    private OrderService orderService = new OrderService();

    public void menu(int userId) {
        while (true) {
            System.out.println("\n===== MENU =====");
            System.out.println("1. Hiển thị tất cả sản phẩm");
            System.out.println("2. Tìm kiếm sản phẩm theo tên");
            System.out.println("3. Lọc sản phẩm theo danh mục");
            System.out.println("4. Mua hàng");
            System.out.println("5. Quản lý giỏ hàng");
            System.out.println("6. Đặt hàng từ giỏ");
            System.out.println("7. Xem đơn hàng của tôi");
            System.out.println("0. Đăng xuất");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    showAllProducts();
                    break;
                case 2:
                    searchProduct();
                    break;
                case 3:
                    filterByCategory();
                    break;
                case 4:
                    buy(userId);
                    break;
                case 5:
                    manageCart(userId);
                    break;
                case 6:
                    checkout(userId);
                    break;
                case 7:
                    viewOrders(userId);
                    break;
                case 0:
                    return;
            }
        }
    }

    private void showAllProducts() {
        List<Product> list = productDAO.getAll();

        System.out.println("===== DANH SÁCH SẢN PHẨM =====");
        System.out.printf("%-5s %-20s %-10s %-10s\n", "ID", "Tên", "Giá", "Số lượng");

        for (Product p : list) {
            System.out.printf("%-5d %-20s %-10.2f %-10d\n",
                    p.getId(),
                    p.getName(),
                    p.getPrice(),
                    p.getStock());
        }
    }

    private void searchProduct() {
        System.out.print("Nhập tên sản phẩm: ");
        String keyword = sc.nextLine();
        List<Product> list = productDAO.searchByName(keyword);

        System.out.println("===== KẾT QUẢ TÌM KIẾM =====");
        for (Product p : list) {
            System.out.println(p.getId() + " | " + p.getName() + " | " + p.getPrice());
        }
    }

    private void filterByCategory() {
        try {
            CategoryDAO dao = new CategoryDAO();
            List<Category> categories = dao.getAll();

            System.out.println("===== DANH MỤC =====");
            for (Category c : categories) {
                System.out.println(c.getId() + ". " + c.getName());
            }

            System.out.print("Chọn danh mục: ");
            int cateId = sc.nextInt();
            sc.nextLine();

            List<Product> list = productDAO.getByCategory(cateId);

            System.out.println("===== SẢN PHẨM =====");
            for (Product p : list) {
                System.out.println(p.getId() + " | " + p.getName() + " | " + p.getPrice());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buy(int userId) {
        while (true) {
            System.out.println("=== MUA HÀNG ===");
            System.out.println("Nhập 0 để quay lại");

            System.out.print("Nhập ID sản phẩm: ");
            int id = sc.nextInt();
            if (id == 0) return;

            System.out.print("Số lượng: ");
            int qty = sc.nextInt();
            sc.nextLine();

            System.out.print("Địa chỉ: ");
            String address = sc.nextLine();

            orderService.placeOrder(userId, id, qty, address);

            System.out.println("Đặt hàng thành công!");
        }
    }

    private void manageCart(int userId) {
        CartDAO cartDAO = new CartDAO();

        while (true) {
            System.out.println("===== GIỎ HÀNG =====");

            List<Integer> productIds = cartDAO.getProductIds(userId);
            List<Integer> quantities = cartDAO.getQuantities(userId);

            if (productIds.isEmpty()) {
                System.out.println("Giỏ hàng trống.");
            } else {
                System.out.printf("%-5s %-20s %-10s %-10s\n", "ID", "Tên", "Số lượng", "Giá");
                for (int i = 0; i < productIds.size(); i++) {
                    Product p = productDAO.findById(productIds.get(i));
                    int qty = quantities.get(i);
                    System.out.printf("%-5d %-20s %-10d %-10.2f\n",
                            p.getId(), p.getName(), qty, p.getPrice() * qty);
                }
            }

            System.out.println("1. Thêm vào giỏ");
            System.out.println("2. Xóa sản phẩm trong giỏ");
            System.out.println("0. Quay lại");

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 0) return;

            if (choice == 1) {
                System.out.print("Nhập ID sản phẩm: ");
                int pid = sc.nextInt();

                System.out.print("Số lượng: ");
                int qty = sc.nextInt();

                cartDAO.addToCart(userId, pid, qty);
                System.out.println("Đã thêm vào giỏ!");
            }

            if (choice == 2) {
                System.out.print("Nhập ID sản phẩm muốn xóa: ");
                int pid = sc.nextInt();
                sc.nextLine();

                cartDAO.removeFromCart(userId, pid);
                System.out.println("Đã xóa sản phẩm khỏi giỏ hàng!");
            }
        }
    }

    private void checkout(int userId) {
        CartDAO cartDAO = new CartDAO();
        ProductDAO productDAO = new ProductDAO();
        OrderDAO orderDAO = new OrderDAO();

        List<Integer> productIds = cartDAO.getProductIds(userId);
        List<Integer> quantities = cartDAO.getQuantities(userId);

        if (productIds.isEmpty()) {
            System.out.println("Giỏ hàng trống!");
            return;
        }

        double total = 0;

        // Hiển thị chi tiết giỏ hàng trước khi xác nhận
        System.out.println("===== CHI TIẾT GIỎ HÀNG =====");
        System.out.printf("%-5s %-20s %-10s %-10s\n", "ID", "Tên", "Số lượng", "Giá");
        for (int i = 0; i < productIds.size(); i++) {
            Product p = productDAO.findById(productIds.get(i));
            int qty = quantities.get(i);
            double price = p.getPrice() * qty;
            total += price;
            System.out.printf("%-5d %-20s %-10d %-10.2f\n",
                    p.getId(), p.getName(), qty, price);
        }

        System.out.println("Tổng đơn hàng: " + total);
        System.out.print("Xác nhận đặt hàng? (Y/N): ");
        String confirm = sc.nextLine();

        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println("Hủy đặt hàng.");
            return;
        }

        System.out.print("Nhập địa chỉ nhận hàng: ");
        String address = sc.nextLine();

        // Tạo đơn hàng
        int orderId = orderDAO.createOrder(userId, total, address);

        // Thêm chi tiết đơn hàng & trừ stock
        for (int i = 0; i < productIds.size(); i++) {
            int pid = productIds.get(i);
            int qty = quantities.get(i);
            Product p = productDAO.findById(pid);

            orderDAO.addOrderDetail(orderId, pid, qty, p.getPrice());
            productDAO.updateStock(pid, qty); // trừ stock
        }

        // Xóa giỏ hàng sau khi đặt thành công
        cartDAO.clearCart(userId);

        System.out.println("Đặt hàng thành công! Tổng tiền: " + total);
    }

    private void viewOrders(int userId) {
        OrderDAO orderDAO = new OrderDAO();
        List<String> list = orderDAO.getOrdersByUser(userId);

        System.out.println("===== ĐƠN HÀNG CỦA BẠN =====");

        if (list.isEmpty()) {
            System.out.println("Chưa có đơn hàng!");
        } else {
            for (String s : list) {
                System.out.println(s);
            }
        }
    }
}