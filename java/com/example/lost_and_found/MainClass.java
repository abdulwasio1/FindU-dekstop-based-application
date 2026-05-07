package com.example.lost_and_found;

import java.sql.*;
import java.util.Scanner;

 interface DbSetup {
    void doSetup() throws ClassNotFoundException, SQLException;
    void doRelease();
}

// Interface 2: TblDepartments
interface TblDepartments {
    void insertDepartment();
    void updateDepartment();
    void removeDepartment();
    void displayDepartments();
}


class Departments implements DbSetup, TblDepartments {

    private Connection connection;
    private Statement statement;
    private Scanner scanner = new Scanner(System.in);


    @Override
    public void doSetup() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/depinfo" ,"root" , "abdulwasi098@@" );
        statement = connection.createStatement();

        System.out.println("Database connection established successfully.");

    }

    @Override
    public void doRelease() {
        try {
            if (statement != null && !statement.isClosed()) {
                statement.close();
                System.out.println("Statement closed.");
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error releasing resources: " + e.getMessage());
        }
    }


    @Override
    public void insertDepartment() {
        try {
            System.out.print("Enter Department ID   : ");
            int deptId = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter Department Name : ");
            String deptName = scanner.nextLine();
            System.out.print("Enter Location        : ");
            String depLocation = scanner.nextLine();

            String sql = "INSERT INTO depinfo.info (dep_id, dep_name, dep_location) VALUES (" + deptId + ", '" + deptName + "', '" + depLocation + "')";

            int rows = statement.executeUpdate(sql);
            if (rows > 0) {
                System.out.println("Department inserted successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
        }
    }

    @Override
    public void updateDepartment() {
        try {
            System.out.print("Enter Department ID to update : ");
            int deptId = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter new Department Name     : ");
            String deptName = scanner.nextLine();
            System.out.print("Enter new Location            : ");
            String depLocation = scanner.nextLine();

            String sql = "UPDATE depinfo.info SET dep_name='" + deptName + "', dep_location='" + depLocation + "' WHERE dep_id=" + deptId;

            int rows = statement.executeUpdate(sql);
            if (rows > 0) {
                System.out.println("Department updated successfully.");
            } else {
                System.out.println("No department found with ID: " + deptId);
            }
        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
        }
    }

    @Override
    public void removeDepartment() {
        try {
            System.out.print("Enter Department ID to remove : ");
            int deptId = scanner.nextInt();

            String sql = "DELETE FROM depinfo.info WHERE dep_id=" + deptId;

            int rows = statement.executeUpdate(sql);
            if (rows > 0) {
                System.out.println("Department removed successfully.");
            } else {
                System.out.println("No department found with ID: " + deptId);
            }
        } catch (SQLException e) {
            System.out.println("Delete failed: " + e.getMessage());
        }
    }

    @Override
    public void displayDepartments() {
        try {
            String sql = "SELECT * FROM depinfo.info";
            ResultSet rs = statement.executeQuery(sql);

            boolean found = false;
            while (rs.next()) {
                found = true;
                int id       = rs.getInt("dep_id");
                String name  = rs.getString("dep_name");
                String loc   = rs.getString("dep_location");
                System.out.print( id + "   " + name +  "   " + loc);
                System.out.println("\n--------------------------------------------------");
            }

            if (!found) {
                System.out.println("No departments found.");
            }
            System.out.println("--------------------------------------------------\n");

        } catch (SQLException e) {
            System.out.println("Display failed: " + e.getMessage());
        }
    }
}

// Main class
public class MainClass {
    public static void main(String args[]) throws SQLException, ClassNotFoundException {

        Departments dept = new Departments();
        dept.doSetup();

        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            // Display menu
            System.out.println("\n==========================================");
            System.out.println("       DEPARTMENT MANAGEMENT SYSTEM");
            System.out.println("==========================================");
            System.out.println("Enter:");
            System.out.println("  1. to display departments");
            System.out.println("  2. to insert department");
            System.out.println("  3. to remove department");
            System.out.println("  4. to update department");
            System.out.println("  5. to exit");
            System.out.println("==========================================");
            System.out.print("Your choice: ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    dept.displayDepartments();
                    break;
                case 2:
                    dept.insertDepartment();
                    break;
                case 3:
                    dept.removeDepartment();
                    break;
                case 4:
                    dept.updateDepartment();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1-5.");
            }

        } while (choice != 5);

        dept.doRelease();
        scanner.close();
    }
}