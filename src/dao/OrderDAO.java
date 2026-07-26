package dao;

import db.DBConnection;
import model.OrderItem;

import java.sql.*;
import java.util.List;

public class OrderDAO {

    public int createOrder(int customerId, List<OrderItem> items) {
        Connection conn = null;
        PreparedStatement orderStmt = null;
        PreparedStatement itemStmt = null;
        ResultSet rs = null;
        int orderId = -1;

        String orderSql = "INSERT INTO Orders (customer_id) VALUES (?)";
        String itemSql = "INSERT INTO Order_Items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";

        ProductDAO productDAO = new ProductDAO();

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Enable Transaction Management

            // 1. Insert into Orders
            orderStmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);
            orderStmt.setInt(1, customerId);
            orderStmt.executeUpdate();

            rs = orderStmt.getGeneratedKeys();
            if (rs.next()) {
                orderId = rs.getInt(1);
            }

            // 2. Insert Order Items & Update Stock
            itemStmt = conn.prepareStatement(itemSql);
            for (OrderItem item : items) {
                // Check and update stock using existing transaction
                boolean stockUpdated = productDAO.updateStock(conn, item.getProductId(), item.getQuantity());
                if (!stockUpdated) {
                    throw new SQLException("Insufficient stock or invalid product ID: " + item.getProductId());
                }

                itemStmt.setInt(1, orderId);
                itemStmt.setInt(2, item.getProductId());
                itemStmt.setInt(3, item.getQuantity());
                itemStmt.setDouble(4, item.getPrice());
                itemStmt.addBatch();
            }

            itemStmt.executeBatch();
            conn.commit(); // Commit transaction
            System.out.println("Order created successfully with ID: " + orderId);

        } catch (SQLException e) {
            System.err.println("Transaction failed. Rolling back changes... Error: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return -1;
        } finally {
            try {
                if (rs != null) rs.close();
                if (orderStmt != null) orderStmt.close();
                if (itemStmt != null) itemStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return orderId;
    }
}