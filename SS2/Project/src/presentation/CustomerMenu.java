package presentation;

import dao.ProductDAO;
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
            System.out.println("1. Xem sản phẩm");
            System.out.println("2. Mua hàng");
            System.out.println("0. Thoát");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    showProducts();
                    break;
                case 2:
                    buy(userId);
                    break;
                case 0:
                    System.exit(0);
            }
        }
    }

    private void showProducts() {
        List<Product> list = productDAO.getAll();
        for (Product p : list) {
            System.out.println(p.getId() + " | " + p.getName() + " | " + p.getPrice() + " | Stock: " + p.getStock());
        }
    }

    private void buy(int userId) {
        System.out.print("Nhập ID sản phẩm: ");
        int id = sc.nextInt();

        System.out.print("Số lượng: ");
        int qty = sc.nextInt();
        sc.nextLine();

        System.out.print("Địa chỉ: ");
        String address = sc.nextLine();

        orderService.placeOrder(userId, id, qty, address);
    }
}