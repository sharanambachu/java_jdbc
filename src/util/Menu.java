package util;

import dao.CustomerDAO;
import dao.ProductDAO;
import dao.OrderDAO;
import model.Customer;
import model.Product;
import model.OrderItem;
import service.SalesService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private final Scanner scanner = new Scanner(System.in);
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final SalesService salesService = new SalesService();

    public void start() {
        while (true) {
            System.out.println("--SMARTMART--");
            System.out.println("1. Customer Management");
            System.out.println("2. Product Management");
            System.out.println("3. Order Management");
            System.out.println("4. Reports Management");
            System.out.println("5. Exit");
            System.out.print("Enter your choice between (1-5) : ");

            int choice = readInt();
            switch (choice) {
                case 1 -> customerMenu();
                case 2 -> productMenu();
                case 3 -> orderMenu();
                case 4 -> reportMenu();
                case 5 -> {
                    System.out.println("Exiting SmartMart System. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice! Try again.");
            }
        }
    }

    // --- Customer Menu ---
    private void customerMenu() {
        System.out.println("\n--- CUSTOMER MANAGEMENT ---");
        System.out.println("1. Add Customer");
        System.out.println("2. Update Customer");
        System.out.println("3. Delete Customer");
        System.out.println("4. Search Customer by ID");
        System.out.println("5. View All Customers");
        System.out.print("Choice: ");

        int choice = readInt();
        switch (choice) {
            case 1 -> {
                System.out.print("Name: ");
                String name = scanner.nextLine();
                System.out.print("Phone: ");
                String phone = scanner.nextLine();
                System.out.print("Email: ");
                String email = scanner.nextLine();
                System.out.print("City: ");
                String city = scanner.nextLine();
                if (customerDAO.addCustomer(new Customer(name, phone, email, city))) {
                    System.out.println("Customer added successfully!");
                }
            }
            case 2 -> {
                System.out.print("Customer ID to update: ");
                int id = readInt();
                Customer existing = customerDAO.getCustomerById(id);
                if (existing != null) {
                    System.out.print("New Name (" + existing.getCustomerName() + "): ");
                    String name = scanner.nextLine();
                    System.out.print("New Phone (" + existing.getPhone() + "): ");
                    String phone = scanner.nextLine();
                    System.out.print("New Email (" + existing.getEmail() + "): ");
                    String email = scanner.nextLine();
                    System.out.print("New City (" + existing.getCity() + "): ");
                    String city = scanner.nextLine();
                    Customer updated = new Customer(id, name, phone, email, city);
                    if (customerDAO.updateCustomer(updated)) {
                        System.out.println("Customer updated!");
                    }
                } else {
                    System.out.println("Customer not found.");
                }
            }
            case 3 -> {
                System.out.print("Customer ID to delete: ");
                int id = readInt();
                if (customerDAO.deleteCustomer(id)) {
                    System.out.println("Customer deleted.");
                }
            }
            case 4 -> {
                System.out.print("Customer ID: ");
                int id = readInt();
                Customer c = customerDAO.getCustomerById(id);
                System.out.println(c != null ? c : "Customer not found.");
            }
            case 5 -> {
                List<Customer> list = customerDAO.getAllCustomers();
for (Customer c : list) {
    System.out.println(c);
}
            }
            default -> System.out.println("Invalid option.");
        }
    }

    // --- Product Menu ---
    private void productMenu() {
        System.out.println("\n--- PRODUCT MANAGEMENT ---");
        System.out.println("1. Add Product");
        System.out.println("2. Update Product");
        System.out.println("3. Delete Product");
        System.out.println("4. Search Product by ID");
        System.out.println("5. View All Products");
        System.out.print("Choice: ");

        int choice = readInt();
        switch (choice) {
            case 1 -> {
                System.out.print("Name: ");
                String name = scanner.nextLine();
                System.out.print("Category: ");
                String cat = scanner.nextLine();
                System.out.print("Price: ");
                double price = readDouble();
                System.out.print("Stock: ");
                int stock = readInt();
                if (productDAO.addProduct(new Product(name, cat, price, stock))) {
                    System.out.println("Product added successfully!");
                }
            }
            case 2 -> {
                System.out.print("Product ID to update: ");
                int id = readInt();
                Product existing = productDAO.getProductById(id);
                if (existing != null) {
                    System.out.print("New Name (" + existing.getProductName() + "): ");
                    String name = scanner.nextLine();
                    System.out.print("New Category (" + existing.getCategory() + "): ");
                    String cat = scanner.nextLine();
                    System.out.print("New Price (" + existing.getPrice() + "): ");
                    double price = readDouble();
                    System.out.print("New Stock (" + existing.getStock() + "): ");
                    int stock = readInt();
                    if (productDAO.updateProduct(new Product(id, name, cat, price, stock))) {
                        System.out.println("Product updated!");
                    }
                } else {
                    System.out.println("Product not found.");
                }
            }
            case 3 -> {
                System.out.print("Product ID to delete: ");
                int id = readInt();
                if (productDAO.deleteProduct(id)) {
                    System.out.println("Product deleted.");
                }
            }
            case 4 -> {
                System.out.print("Product ID: ");
                int id = readInt();
                Product p = productDAO.getProductById(id);
                System.out.println(p != null ? p : "Product not found.");
            }
            case 5 -> {
               List<Product> list = productDAO.getAllProducts();
for (Product p : list) {
    System.out.println(p);
}
            }
            default -> System.out.println("Invalid option.");
        }
    }

    // --- Order Menu ---
    private void orderMenu() {
        System.out.println("\n--- ORDER MANAGEMENT ---");
        System.out.print("Enter Customer ID placing the order: ");
        int customerId = readInt();

        Customer customer = customerDAO.getCustomerById(customerId);
        if (customer == null) {
            System.out.println("Invalid Customer ID!");
            return;
        }

        List<OrderItem> items = new ArrayList<>();
        double grandTotal = 0;

        while (true) {
            System.out.print("Enter Product ID (or 0 to complete order): ");
            int productId = readInt();
            if (productId == 0) break;

            Product p = productDAO.getProductById(productId);
            if (p == null) {
                System.out.println("Invalid Product ID.");
                continue;
            }

            System.out.printf("Available Stock for %s: %d\n", p.getProductName(), p.getStock());
            System.out.print("Enter Quantity: ");
            int qty = readInt();

            if (qty > p.getStock()) {
                System.out.println("Insufficient stock! Item skipped.");
                continue;
            }

            items.add(new OrderItem(p.getProductId(), qty, p.getPrice()));
            grandTotal += qty * p.getPrice();
            System.out.println("Item added to cart.");
        }

        if (items.isEmpty()) {
            System.out.println("No items added. Order canceled.");
            return;
        }

        int orderId = orderDAO.createOrder(customerId, items);
        if (orderId != -1) {
            printInvoice(orderId, customer, items, grandTotal);
        }
    }

    private void printInvoice(int orderId, Customer customer, List<OrderItem> items, double total) {
        System.out.println("\n=================================================");
        System.out.println("               SMARTMART INVOICE                 ");
        System.out.println("=================================================");
        System.out.println("Order ID   : #" + orderId);
        System.out.println("Customer   : " + customer.getCustomerName() + " (" + customer.getCity() + ")");
        System.out.println("Phone      : " + customer.getPhone());
        System.out.println("-------------------------------------------------");
        System.out.printf("%-20s | %-5s | %-10s | %-10s\n", "Product", "Qty", "Unit Price", "Total");
        System.out.println("-------------------------------------------------");
        for (OrderItem item : items) {
            Product p = productDAO.getProductById(item.getProductId());
            System.out.printf("%-20s | %-5d | ₹%-9.2f | ₹%-9.2f\n",
                    p.getProductName(), item.getQuantity(), item.getPrice(), (item.getQuantity() * item.getPrice()));
        }
        System.out.println("-------------------------------------------------");
        System.out.printf("GRAND TOTAL: ₹%.2f\n", total);
        System.out.println("=================================================");
    }

    // --- Reports Menu ---
    private void reportMenu() {
        System.out.println("\n--- REPORTS MODULE ---");
        System.out.println("1. Total Sales Report");
        System.out.println("2. Best Selling Product");
        System.out.println("3. Customer Purchase Report (INNER JOIN)");
        System.out.println("4. Customers Without Orders (LEFT JOIN)");
        System.out.println("5. Top Customers Report (GROUP BY, SUM)");
        System.out.print("Choice: ");

        int choice = readInt();
        switch (choice) {
            case 1 -> salesService.getReportTotalSales();
            case 2 -> salesService.getBestSellingProduct();
            case 3 -> salesService.getCustomerPurchaseReport();
            case 4 -> salesService.getCustomersWithoutOrders();
            case 5 -> salesService.getTopCustomers();
            default -> System.out.println("Invalid choice.");
        }
    }

    private int readInt() {
        try {
            int val = Integer.parseInt(scanner.nextLine().trim());
            return val;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private double readDouble() {
        try {
            double val = Double.parseDouble(scanner.nextLine().trim());
            return val;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}