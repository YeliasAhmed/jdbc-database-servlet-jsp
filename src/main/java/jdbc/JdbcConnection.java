package jdbc;

import java.sql.*;

public class JdbcConnection {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found");
            e.printStackTrace();
            return;
        }
        String url = "jdbc:mysql://localhost:3306/employees";
        String user = "root";
        String password = "";

        try ( Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to database successfully.");
        } catch (SQLException e) {
            System.out.println("Failed to connect to database.");
            e.printStackTrace();
        }
    }
}
