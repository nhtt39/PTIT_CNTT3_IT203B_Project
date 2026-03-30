package presentation;

import dao.CategoryDAO;
import dao.ProductDAO;
import dao.CartDAO;
import model.Category;
import model.Product;
import service.OrderService;

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
                case 0:
                    return;
            }
        }
    }

    private void showAllProducts() {
        List<Product> allProducts = productDAO.getAll();
        System.out.println("===== TẤT CẢ SẢN PHẨM =====");
        for (Product p : allProducts) {
            System.out.println(p.getId() + " | " + p.getName() + " | " + p.getPrice());
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

            List<String> cart = cartDAO.getCart(userId);
            if (cart.isEmpty()) {
                System.out.println("Giỏ hàng trống.");
            } else {
                for (String item : cart) {
                    System.out.println(item);
                }
            }

            System.out.println("1. Thêm vào giỏ");
            System.out.println("0. Quay lại");

            int choice = sc.nextInt();
            if (choice == 0) return;

            if (choice == 1) {
                System.out.print("Nhập ID sản phẩm: ");
                int pid = sc.nextInt();

                System.out.print("Số lượng: ");
                int qty = sc.nextInt();

                cartDAO.addToCart(userId, pid, qty);
                System.out.println("Đã thêm vào giỏ!");
            }
        }
    }
}