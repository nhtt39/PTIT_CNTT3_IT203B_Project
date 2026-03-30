package presentation;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("=================================");
        System.out.println("   PHONE STORE MANAGEMENT SYSTEM ");
        System.out.println("=================================");

        AuthMenu authMenu = new AuthMenu();
        authMenu.start();
    }
}