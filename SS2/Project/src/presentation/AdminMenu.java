package presentation;

import dao.ProductDAO;
import model.Product;

import java.util.Scanner;

public class AdminMenu {

    private Scanner sc = new Scanner(System.in);
    private ProductDAO productDAO = new ProductDAO();

    public void menu() {
        while (true) {
            System.out.println("1. Xem sản phẩm");
            System.out.println("2. Thêm sản phẩm");

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                for (Product p : productDAO.getAll()) {
                    System.out.println(p.getId() + " - " + p.getName() + " - " + p.getPrice());
                }
            }

            if (choice == 2) {
                Product p = new Product();

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
        }
    }
}