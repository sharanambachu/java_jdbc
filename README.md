# SmartMart Sales & Inventory Management System

A Java console-based application built using JDBC and MySQL for managing store sales, customer data, product inventory, and generating analytics reports.

---

## Technical Features
- **Object-Oriented Programming**: Clear separation of concerns using the DAO & Model design patterns.
- **Database Transaction Management**: Atomic transactions for placing orders and updating stock safely.
- **PreparedStatement Integration**: Prevents SQL Injection vulnerabilities.
- **Advanced SQL Queries**: Implementation of `INNER JOIN`, `LEFT JOIN`, aggregate functions (`SUM`, `COUNT`), and `GROUP BY`.

---

## Package Structure
src/
├── model/           # Data Transfer Objects (Customer, Product, Order, OrderItem)
├── dao/             # Data Access Objects (CustomerDAO, ProductDAO, OrderDAO)
├── db/              # Database Connection Utility (DBConnection)
├── service/         # Business Logic & SQL Analytics (SalesService)
├── util/            # CLI Navigation & Display (Menu)
└── Main.java        # Main Entry Point

## Setup Instructions

1. **Database Setup**:
   - Run the provided `database.sql` script in MySQL Workbench or CLI.
   - Update credentials inside `src/db/DBConnection.java` (`USER` and `PASSWORD`).

2. **Dependencies**:
   - Ensure the MySQL Connector JAR (`mysql-connector-j-x.x.x.jar`) is added to your module dependencies.

3. **Execution**:
   - Run `src/Main.java`.