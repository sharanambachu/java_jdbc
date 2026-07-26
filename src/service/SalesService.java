package service;

import db.DBConnection;

import java.sql.*;

public class SalesService {

    // Report 1: Total Sales
    public void getReportTotalSales() {
        String sql = "SELECT SUM(quantity * price) AS total_revenue, COUNT(DISTINCT order_id) AS total_orders FROM Order_Items";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n========== TOTAL SALES REPORT ==========");
            if (rs.next()) {
                System.out.printf("Total Orders Placed : %d\n", rs.getInt("total_orders"));
                System.out.printf("Total Revenue      : ₹%.2f\n", rs.getDouble("total_revenue"));
            }
            System.out.println("=========================================");
        } catch (SQLException e) {
            System.err.println("Error generating total sales report: " + e.getMessage());
        }
    }

    // Report 2: Best Selling Product
    public void getBestSellingProduct() {
        String sql = "SELECT p.product_name, SUM(oi.quantity) AS total_qty, SUM(oi.quantity * oi.price) AS revenue " +
                     "FROM Order_Items oi " +
                     "JOIN Products p ON oi.product_id = p.product_id " +
                     "GROUP BY p.product_id, p.product_name " +
                     "ORDER BY total_qty DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n========== BEST SELLING PRODUCT ==========");
            if (rs.next()) {
                System.out.println("Product Name : " + rs.getString("product_name"));
                System.out.println("Units Sold   : " + rs.getInt("total_qty"));
                System.out.printf("Total Revenue: ₹%.2f\n", rs.getDouble("revenue"));
            } else {
                System.out.println("No sales data available.");
            }
            System.out.println("=========================================");
        } catch (SQLException e) {
            System.err.println("Error generating best seller report: " + e.getMessage());
        }
    }

    // Report 3: Customer Purchase Report (INNER JOIN)
    public void getCustomerPurchaseReport() {
        String sql = "SELECT c.customer_name, o.order_id, o.order_date, SUM(oi.quantity * oi.price) AS order_total " +
                     "FROM Customers c " +
                     "INNER JOIN Orders o ON c.customer_id = o.customer_id " +
                     "INNER JOIN Order_Items oi ON o.order_id = oi.order_id " +
                     "GROUP BY c.customer_name, o.order_id, o.order_date " +
                     "ORDER BY o.order_date DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n====================== CUSTOMER PURCHASE REPORT (INNER JOIN) ======================");
            System.out.printf("%-20s | %-10s | %-20s | %-12s\n", "Customer Name", "Order ID", "Date", "Total Amount");
            System.out.println("----------------------------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-20s | %-10d | %-20s | ₹%-12.2f\n",
                        rs.getString("customer_name"),
                        rs.getInt("order_id"),
                        rs.getTimestamp("order_date").toString(),
                        rs.getDouble("order_total"));
            }
            System.out.println("==================================================================================");
        } catch (SQLException e) {
            System.err.println("Error generating purchase report: " + e.getMessage());
        }
    }

    // Report 4: Customers Without Orders (LEFT JOIN)
    public void getCustomersWithoutOrders() {
        String sql = "SELECT c.customer_id, c.customer_name, c.email, c.phone " +
                     "FROM Customers c " +
                     "LEFT JOIN Orders o ON c.customer_id = o.customer_id " +
                     "WHERE o.order_id IS NULL";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n================= CUSTOMERS WITHOUT ORDERS (LEFT JOIN) =================");
            System.out.printf("%-5s | %-20s | %-25s | %-15s\n", "ID", "Name", "Email", "Phone");
            System.out.println("-----------------------------------------------------------------------");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-5d | %-20s | %-25s | %-15s\n",
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("email"),
                        rs.getString("phone"));
            }
            if (!found) System.out.println("All registered customers have placed at least one order.");
            System.out.println("=======================================================================");
        } catch (SQLException e) {
            System.err.println("Error generating report: " + e.getMessage());
        }
    }

    // Report 5: Top Customers (GROUP BY, SUM)
    public void getTopCustomers() {
        String sql = "SELECT c.customer_name, COUNT(DISTINCT o.order_id) AS total_orders, SUM(oi.quantity * oi.price) AS total_spent " +
                     "FROM Customers c " +
                     "JOIN Orders o ON c.customer_id = o.customer_id " +
                     "JOIN Order_Items oi ON o.order_id = oi.order_id " +
                     "GROUP BY c.customer_id, c.customer_name " +
                     "ORDER BY total_spent DESC LIMIT 5";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n================ TOP CUSTOMERS REPORT (GROUP BY, SUM) ================");
            System.out.printf("%-20s | %-15s | %-15s\n", "Customer Name", "Total Orders", "Total Spent");
            System.out.println("---------------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-20s | %-15d | ₹%-15.2f\n",
                        rs.getString("customer_name"),
                        rs.getInt("total_orders"),
                        rs.getDouble("total_spent"));
            }
            System.out.println("=====================================================================");
        } catch (SQLException e) {
            System.err.println("Error generating top customers report: " + e.getMessage());
        }
    }
}