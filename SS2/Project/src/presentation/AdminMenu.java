package presentation;

import dao.ProductDAO;
import dao.UserDAO;
import dao.CategoryDAO;
import model.Category;
import model.Product;
import model.User;
import dao.OrderDAO;

import java.util.List;
import java.util.Scanner;

public class AdminMenu {

    private Scanner sc = new Scanner(System.in);
    private ProductDAO productDAO = new ProductDAO();

    private void categoryMenu() {
        while (true) {
            System.out.println("\n===== QUẢN LÝ DANH MỤC =====");
            System.out.println("1. Xem danh mục");
            System.out.println("2. Thêm danh mục");
            System.out.println("3. Sửa danh mục");
            System.out.println("4. Xóa danh mục");
            System.out.println("0. Quay lại");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: showProductsByCategory(); break;
                case 2: addCategory(); break;
                case 3: updateCategory(); break;
                case 4: deleteCategory(); break;
                case 0: return;
            }
        }
    }

    private void productMenu() {
        while (true) {
            System.out.println("\n===== QUẢN LÝ SẢN PHẨM =====");
            System.out.println("1. Thêm sản phẩm");
            System.out.println("2. Sửa sản phẩm");
            System.out.println("3. Xóa sản phẩm");
            System.out.println("0. Quay lại");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: addProduct(); break;
                case 2: updateProduct(); break;
                case 3: deleteProduct(); break;
                case 0: return;
            }
        }
    }

    private void userMenu() {
        while (true) {
            System.out.println("\n===== QUẢN LÝ NGƯỜI DÙNG =====");
            System.out.println("1. Xem danh sách");
            System.out.println("2. Khóa/Mở tài khoản");
            System.out.println("0. Quay lại");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: showUsers(); break;
                case 2: manageUsers(); break;
                case 0: return;
            }
        }
    }

    private void showUsers() {
        UserDAO userDAO = new UserDAO();

        try {
            List<User> users = userDAO.getAllUsers();

            System.out.println("===== DANH SÁCH USER =====");
            System.out.printf("%-5s %-25s %-10s %-10s\n", "ID", "Email", "Role", "Status");

            for (User u : users) {
                System.out.printf("%-5d %-25s %-10s %-10s\n",
                        u.getId(),
                        u.getEmail(),
                        u.getRole(),
                        u.getStatus());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void manageUsers() {
        UserDAO userDAO = new UserDAO();

        try {
            showUsers();

            System.out.print("Nhập ID user: ");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.println("1. Khóa");
            System.out.println("2. Mở khóa");

            int choice = sc.nextInt();

            if (choice == 1) {
                userDAO.updateStatus(id, "BLOCKED");
            } else {
                userDAO.updateStatus(id, "ACTIVE");
            }

            System.out.println("Cập nhật thành công!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showProductsByCategory() {
        try {
            CategoryDAO categoryDAO = new CategoryDAO();

            List<Category> categories = categoryDAO.getAll();

            System.out.println("===== DANH MỤC =====");
            for (Category c : categories) {
                System.out.println(c.getId() + ". " + c.getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addCategory() {
        try {
            CategoryDAO dao = new CategoryDAO();
            Category c = new Category();

            System.out.print("Tên danh mục: ");
            c.setName(sc.nextLine());

            System.out.print("Mô tả: ");
            c.setDescription(sc.nextLine());

            dao.add(c);
            System.out.println("Thêm thành công!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCategory() {
        try {
            CategoryDAO dao = new CategoryDAO();

            System.out.print("ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            Category c = new Category();
            c.setId(id);

            System.out.print("Tên mới: ");
            c.setName(sc.nextLine());

            System.out.print("Mô tả mới: ");
            c.setDescription(sc.nextLine());

            dao.update(c);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteCategory() {
        try {
            CategoryDAO dao = new CategoryDAO();

            System.out.print("ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.print("Bạn có chắc muốn xóa? (Y/N): ");
            String confirm = sc.nextLine();

            if (confirm.equalsIgnoreCase("Y")) {
                dao.delete(id);
                System.out.println(" Đã xóa!");
            } else {
                System.out.println(" Đã hủy!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addProduct() {
        Product p = new Product();

        System.out.print("Tên: ");
        String name = sc.nextLine();
        if (productDAO.isNameExists(name)) {
            System.out.println(" Tên sản phẩm đã tồn tại!");
            return;
        }

        p.setName(name);

        System.out.print("Brand: ");
        p.setBrand(sc.nextLine());

        System.out.print("Giá: ");
        p.setPrice(sc.nextDouble());

        System.out.print("Stock: ");
        p.setStock(sc.nextInt());

        System.out.print("Category ID: ");
        p.setCategoryId(sc.nextInt());
        sc.nextLine();

        productDAO.add(p);
    }

    private void updateProduct() {
        System.out.print("ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        Product p = productDAO.findById(id);

        if (p == null) {
            System.out.println("Không tìm thấy!");
            return;
        }

        System.out.print("Tên mới: ");
        String newName = sc.nextLine();

        if (!newName.isEmpty() && productDAO.isNameExists(newName)) {
            System.out.println(" Tên đã tồn tại!");
            return;
        }

        if (!newName.isEmpty()) p.setName(newName);

        System.out.print("Brand: ");
        p.setBrand(sc.nextLine());

        System.out.print("Giá: ");
        p.setPrice(sc.nextDouble());

        System.out.print("Stock: ");
        p.setStock(sc.nextInt());

        productDAO.update(p);
    }

    private void deleteProduct() {
        System.out.print("Nhập ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Bạn có chắc muốn xóa? (Y/N): ");
        String confirm = sc.nextLine();

        if (confirm.equalsIgnoreCase("Y")) {
            productDAO.delete(id);
            System.out.println(" Đã xóa!");
        } else {
            System.out.println(" Đã hủy!");
        }
    }

    private void orderMenu() {
        OrderDAO orderDAO = new OrderDAO();

        while (true) {
            System.out.println("\n===== QUẢN LÝ ĐƠN HÀNG =====");
            System.out.println("1. Xem danh sách đơn hàng của khách");
            System.out.println("2. Cập nhật trạng thái đơn hàng");
            System.out.println("0. Quay lại");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    // Lấy danh sách các đơn hàng, hiển thị theo khách
                    List<String> orders = orderDAO.getAllOrdersWithDetails();
                    if (orders.isEmpty()) {
                        System.out.println("Chưa có đơn hàng nào!");
                    } else {
                        for (String s : orders) {
                            System.out.println(s); // ví dụ: "OrderID: 1 | UserID: 2 | Products: iPhone 14 x1 | Total: 3000 | Status: PENDING"
                        }
                    }
                    break;

                case 2:
                    System.out.print("Nhập Order ID cần cập nhật trạng thái: ");
                    int orderId = sc.nextInt();
                    sc.nextLine();

                    System.out.println("Chọn trạng thái mới:");
                    System.out.println("1. PENDING");
                    System.out.println("2. SHIPPING");
                    System.out.println("3. DELIVERED");
                    int st = sc.nextInt();
                    sc.nextLine();

                    String status = "PENDING";
                    if (st == 2) status = "SHIPPING";
                    if (st == 3) status = "DELIVERED";

                    orderDAO.updateOrderStatus(orderId, status);
                    System.out.println("Cập nhật trạng thái thành công!");
                    break;

                case 0:
                    return;
            }
        }
    }

    public void menu() {
        while (true) {
            System.out.println("\n===== ADMIN MENU =====");
            System.out.println("1. Quản lý danh mục");
            System.out.println("2. Quản lý sản phẩm");
            System.out.println("3. Quản lý người dùng");
            System.out.println("4. Quản lý đơn hàng");
            System.out.println("5. Đăng xuất");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: categoryMenu(); break;
                case 2: productMenu(); break;
                case 3: userMenu(); break;
                case 4: orderMenu(); break;
                case 5: return;
            }
        }
    }
}