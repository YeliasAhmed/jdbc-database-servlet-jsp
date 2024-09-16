package jdbc;

import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class JdbcConnection {

    public void loadDriverClass() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MYSQL JDBC Driver Loaded");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found");
            e.printStackTrace();
        }
    }
    public Properties loadProperties() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")){
            props.load(fis);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return props;
    }

    public Connection createConnection(Properties props) {
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password", "");
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database successfully.");
        } catch (SQLException e) {
            System.out.println("Failed to connect to database.");
            e.printStackTrace();
        }
        return conn;
    }
    public void insertEmployee(Connection conn, String name, String designation, double salary) {

        String insertQuery = "insert into employee_info (name, designation, salary) values(?, ?, ?)";
        try(PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.setString(1, name);
            pstmt.setString(2, designation);
            pstmt.setDouble(3, salary);
            int rowAffected = pstmt.executeUpdate();
            if (rowAffected > 0) {
                System.out.println("Employee inserted successfully.");
            } else {
                System.out.println("Employee insertion failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error occurred while inserting into employee info");
        }
    }
    public void fetchEmployee(Connection conn, int id) {
        String query = "select * from employee_info where id=?";
        try(PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("Employee ID: "+rs.getInt("id"));
                System.out.println("Employee Name: "+ rs.getString("name"));
                System.out.println("Employee Designation: "+ rs.getString("designation"));
                System.out.println("Employee Salary: "+ rs.getDouble("salary"));
            }
        } catch(SQLException e) {
            System.out.println("Employee not found");
        }
    }
    public void updateEmployee(Connection conn, int id, String name, String designation, double salary) {
        String query = "update employee_info set name=?, designation=?, salary=? where id=?";
        try(PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, designation);
            pstmt.setDouble(3, salary);
            pstmt.setInt(4, id);
            int rowAffected = pstmt.executeUpdate();
            if (rowAffected > 0) {
                System.out.println("Employee updated successfully.");
            } else {
                System.out.println("Failed to update employee.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteEmployee(Connection conn, int id) {
        String query = "delete from employee_info where id=?";
        try(PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            int rowAffected = preparedStatement.executeUpdate();
            if (rowAffected > 0) {
                System.out.println("Employee deleted successfully");
            } else {
                System.out.println("Employee not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JdbcConnection jdbc = new JdbcConnection();
        jdbc.loadDriverClass();
        Properties prop = jdbc.loadProperties();
        Connection conn = jdbc.createConnection(prop);
        if (conn !=null) {
            jdbc.deleteEmployee(conn, 1);
        }


    }
}
