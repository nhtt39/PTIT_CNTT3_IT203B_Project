package service;

import dao.UserDAO;
import model.User;

public class AuthService {

    private UserDAO userDAO = new UserDAO();

    public void register(User user) {
        try {
            user.setRole("CUSTOMER"); // mặc định
            userDAO.register(user);
            System.out.println("Đăng ký thành công");
        } catch (Exception e) {
            System.out.println("Lỗi đăng ký");
            e.printStackTrace();
        }
    }

    public User login(String email, String password) {
        try {
            User user = userDAO.findByEmail(email);

            if (user != null && user.getPassword().equals(password)) {
                System.out.println("Đăng nhập thành công!");
                return user;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}