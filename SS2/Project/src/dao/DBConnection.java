package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/phone_store_db",
                    "root",
                    "Hoangnhat"
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}