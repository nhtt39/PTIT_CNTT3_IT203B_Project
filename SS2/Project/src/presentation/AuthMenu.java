package presentation;

import model.User;
import service.AuthService;
import dao.UserDAO;
import util.ValidationUtil;

import java.util.Scanner;

public class AuthMenu {

    private AuthService authService = new AuthService();
    private Scanner sc = new Scanner(System.in);

    public void start() throws Exception {
        while (true) {
            System.out.println("1. Đăng nhập");
            System.out.println("2. Đăng ký");
            System.out.println("0. Thoát");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 0:
                    System.exit(0);
            }
        }
    }

    private void login() {
        try {
            System.out.print("Email: ");
            String email = sc.nextLine();

            System.out.print("Password: ");
            String pass = sc.nextLine();

            User user = authService.login(email, pass);

            if (!email.contains("@")) {
                System.out.println("Email không hợp lệ!");
                return;
            }

            if (user != null) {
                System.out.println("Đăng nhập thành công!");

                if (user.getRole().equals("ADMIN")) {
                    new AdminMenu().menu();
                } else {
                    new CustomerMenu().menu(user.getId());
                }
            } else {
                System.out.println("Sai tài khoản hoặc mật khẩu!");
            }

        } catch (Exception e) {

            if (e.getMessage().equals("BLOCKED")) {
                System.out.println("Tài khoản đã bị khóa");
            } else {
                e.printStackTrace();
            }
        }
    }

    private void register() {
        User user = new User();

        System.out.print("Name: ");
        user.setName(sc.nextLine());

        System.out.print("Email: ");
        String email = sc.nextLine();

        if (!ValidationUtil.isValidEmail(email)) {
            System.out.println(" Email không hợp lệ!");
            return;
        }
        user.setEmail(email);

        System.out.print("Password: ");
        user.setPassword(sc.nextLine());

        user.setRole("CUSTOMER");

        authService.register(user);
    }
}