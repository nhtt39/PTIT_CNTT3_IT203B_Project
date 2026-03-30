package presentation;

import dao.ProductDAO;
import model.Category;
import model.Product;
import dao.CategoryDAO;

import java.util.List;
import java.util.Scanner;

public class AdminMenu {

    private Scanner sc = new Scanner(System.in);
    private ProductDAO productDAO = new ProductDAO();

    private void showProductsByCategory() {
        try {
            CategoryDAO categoryDAO = new CategoryDAO();

            while (true) {
                List<Category> categories = categoryDAO.getAll();

                System.out.println("===== DANH MỤC =====");
                for (Category c : categories) {
                    System.out.println(c.getId() + ". " + c.getName());
                }
                System.out.println("0. Quay lại");

                System.out.print("Chọn danh mục: ");
                int cateId = sc.nextInt();

                if (cateId == 0) return;

                List<Product> products = productDAO.getByCategory(cateId);

                System.out.println("===== SẢN PHẨM =====");
                for (Product p : products) {
                    System.out.println(p.getId() + " - " + p.getName() + " - " + p.getPrice());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateProduct() {
        System.out.print("Nhập ID sản phẩm: ");
        int id = sc.nextInt();
        sc.nextLine();

        Product p = productDAO.findById(id);

        if (p == null) {
            System.out.println("Không tìm thấy!");
            return;
        }

        System.out.print("Tên mới (" + p.getName() + "): ");
        String name = sc.nextLine();
        if (!name.isEmpty()) p.setName(name);

        System.out.print("Brand mới (" + p.getBrand() + "): ");
        String brand = sc.nextLine();
        if (!brand.isEmpty()) p.setBrand(brand);

        System.out.print("Giá mới (" + p.getPrice() + "): ");
        String price = sc.nextLine();
        if (!price.isEmpty()) p.setPrice(Double.parseDouble(price));

        System.out.print("Stock mới (" + p.getStock() + "): ");
        String stock = sc.nextLine();
        if (!stock.isEmpty()) p.setStock(Integer.parseInt(stock));

        System.out.print("Category mới (" + p.getCategoryId() + "): ");
        String cate = sc.nextLine();
        if (!cate.isEmpty()) p.setCategoryId(Integer.parseInt(cate));

        productDAO.update(p);
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
            System.out.println("Thêm danh mục thành công!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCategory() {
        try {
            CategoryDAO dao = new CategoryDAO();

            System.out.print("Nhập ID danh mục: ");
            int id = sc.nextInt();
            sc.nextLine();

            Category c = new Category();
            c.setId(id);

            System.out.print("Tên mới: ");
            c.setName(sc.nextLine());

            System.out.print("Mô tả mới: ");
            c.setDescription(sc.nextLine());

            dao.update(c);
            System.out.println("Cập nhật thành công!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteCategory() {
        try {
            CategoryDAO dao = new CategoryDAO();

            System.out.print("Nhập ID danh mục cần xóa: ");
            int id = sc.nextInt();

            dao.delete(id);
            System.out.println("Xóa thành công!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addProduct() {
        Product p = new Product();
        CategoryDAO categoryDAO = new CategoryDAO();

        try {
            List<Category> categories = categoryDAO.getAll();

            System.out.println("===== DANH MỤC =====");
            for (Category c : categories) {
                System.out.println(c.getId() + ". " + c.getName());
            }

            System.out.print("Chọn danh mục: ");
            int cateId = sc.nextInt();
            sc.nextLine();

            p.setCategoryId(cateId);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.print("Tên: ");
        p.setName(sc.nextLine());

        System.out.print("Brand: ");
        p.setBrand(sc.nextLine());

        System.out.print("Giá: ");
        p.setPrice(sc.nextDouble());

        System.out.print("Stock: ");
        p.setStock(sc.nextInt());

        productDAO.add(p);
    }

    private void deleteProduct() {
        System.out.print("Nhập ID sản phẩm cần xóa: ");
        int id = sc.nextInt();

        productDAO.delete(id);
        System.out.println("Xóa thành công!");
    }

    public void menu() {
        while (true) {
            System.out.println("===== ADMIN MENU =====");
            System.out.println("1. Xem danh mục");
            System.out.println("2. Thêm danh mục");
            System.out.println("3. Sửa danh mục");
            System.out.println("4. Xóa danh mục");
            System.out.println("5. Thêm sản phẩm");
            System.out.println("6. Sửa sản phẩm");
            System.out.println("7. Xóa sản phẩm");
            System.out.println("0. Đăng xuất");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: showProductsByCategory(); break;
                case 2: addCategory(); break;
                case 3: updateCategory(); break;
                case 4: deleteCategory(); break;
                case 5: addProduct(); break;
                case 6: updateProduct(); break;
                case 7: deleteProduct(); break;
                case 0: return;
            }
        }
    }
}