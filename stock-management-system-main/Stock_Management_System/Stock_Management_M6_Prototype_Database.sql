-- Stock Management System - Milestone 6 Prototype Database
-- Group 4
-- SQLite-compatible schema and sample data

DROP TABLE IF EXISTS InventoryTransaction;
DROP TABLE IF EXISTS PurchaseOrderItem;
DROP TABLE IF EXISTS PurchaseOrder;
DROP TABLE IF EXISTS Sales;
DROP TABLE IF EXISTS Product;
DROP TABLE IF EXISTS Supplier;
DROP TABLE IF EXISTS Category;
DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS Role;

CREATE TABLE Role (
    role_id INTEGER PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE User (
    user_id INTEGER PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL,
    created_at DATETIME NOT NULL,
    role_id INTEGER NOT NULL,
    FOREIGN KEY (role_id) REFERENCES Role(role_id)
);

CREATE TABLE Category (
    category_id INTEGER PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    is_active BOOLEAN NOT NULL
);

CREATE TABLE Supplier (
    supplier_id INTEGER PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(30),
    address VARCHAR(255),
    is_active BOOLEAN NOT NULL
);

CREATE TABLE Product (
    product_id INTEGER PRIMARY KEY,
    sku VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    qty_on_hand INTEGER NOT NULL,
    initial_quantity INTEGER NOT NULL,
    unit_cost DECIMAL(10,2) NOT NULL,
    sell_price DECIMAL(10,2) NOT NULL,
    reorder_point INTEGER NOT NULL,
    is_active BOOLEAN NOT NULL,
    category_id INTEGER NOT NULL,
    supplier_id INTEGER NOT NULL,
    FOREIGN KEY (category_id) REFERENCES Category(category_id),
    FOREIGN KEY (supplier_id) REFERENCES Supplier(supplier_id)
);

CREATE TABLE Sales (
    sale_id INTEGER PRIMARY KEY,
    product_id INTEGER NOT NULL,
    qty INTEGER NOT NULL,
    unit_cost DECIMAL(10,2) NOT NULL,
    sell_price DECIMAL(10,2) NOT NULL,
    profit DECIMAL(10,2) NOT NULL,
    created_at DATETIME NOT NULL,
    FOREIGN KEY (product_id) REFERENCES Product(product_id)
);

CREATE TABLE PurchaseOrder (
    po_id INTEGER PRIMARY KEY,
    po_number VARCHAR(50) NOT NULL UNIQUE,
    supplier_id INTEGER NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    notes VARCHAR(255),
    FOREIGN KEY (supplier_id) REFERENCES Supplier(supplier_id)
);

CREATE TABLE PurchaseOrderItem (
    po_item_id INTEGER PRIMARY KEY,
    po_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    quantity_ordered INTEGER NOT NULL,
    quantity_received INTEGER NOT NULL,
    unit_cost_at_order DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (po_id) REFERENCES PurchaseOrder(po_id),
    FOREIGN KEY (product_id) REFERENCES Product(product_id)
);

CREATE TABLE InventoryTransaction (
    transaction_id INTEGER PRIMARY KEY,
    product_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    type VARCHAR(30) NOT NULL,
    quantity_change INTEGER NOT NULL,
    reason VARCHAR(255),
    created_at DATETIME NOT NULL,
    FOREIGN KEY (product_id) REFERENCES Product(product_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

INSERT INTO Role VALUES
(1, 'Admin', 'Full system access'),
(2, 'Manager', 'Can review inventory and approve stock actions'),
(3, 'Clerk', 'Handles regular inventory updates'),
(4, 'Auditor', 'Reviews reports and transaction logs'),
(5, 'DemoUser', 'Used for demonstration purposes');

INSERT INTO User VALUES
(1, 'justin_admin', 'hash_admin', 1, '2026-03-01 09:00:00', 1),
(2, 'minghui_scribe', 'hash_scribe', 1, '2026-03-01 09:05:00', 2),
(3, 'hanwen_glossary', 'hash_glossary', 1, '2026-03-01 09:10:00', 3),
(4, 'yuan_dev', 'hash_dev', 1, '2026-03-01 09:15:00', 1),
(5, 'naz_qa', 'hash_qa', 1, '2026-03-01 09:20:00', 4);

INSERT INTO Category VALUES
(1, 'Electronics', 'Electronic accessories and devices', 1),
(2, 'Office Supplies', 'Daily office and paper products', 1),
(3, 'Food', 'Packaged food and drink items', 1),
(4, 'Clothing', 'Wearable inventory items', 1),
(5, 'Hardware', 'Tools and maintenance items', 1);

INSERT INTO Supplier VALUES
(1, 'TechSource', 'sales@techsource.com', '201-555-0101', '101 Market St, Newark, NJ', 1),
(2, 'Staples Supply', 'contact@staplessupply.com', '201-555-0102', '55 Broad Ave, Jersey City, NJ', 1),
(3, 'FreshMart Wholesale', 'orders@freshmart.com', '201-555-0103', '9 River Rd, Hoboken, NJ', 1),
(4, 'BuildPro', 'support@buildpro.com', '201-555-0104', '77 Hudson Blvd, Union City, NJ', 1),
(5, 'QuickStock', 'service@quickstock.com', '201-555-0105', '22 Central St, Elizabeth, NJ', 1);

INSERT INTO Product VALUES
(1, 'ELEC-001', 'USB Drive 64GB', 'Portable storage device', 40, 50, 8.00, 14.99, 10, 1, 1, 1),
(2, 'OFF-001', 'Printer Paper Pack', '500-sheet white paper pack', 90, 100, 3.25, 6.99, 20, 1, 2, 2),
(3, 'FOOD-001', 'Water Bottle Pack', '24-pack bottled water', 25, 30, 4.10, 8.50, 8, 1, 3, 3),
(4, 'CLOTH-001', 'Safety Gloves', 'Protective work gloves', 14, 20, 5.00, 10.99, 5, 1, 4, 4),
(5, 'HARD-001', 'LED Flashlight', 'Compact flashlight', 18, 25, 6.50, 12.99, 6, 1, 5, 5);

INSERT INTO Sales VALUES
(1, 1, 5, 8.00, 14.99, 34.95, '2026-03-10 10:15:00'),
(2, 2, 10, 3.25, 6.99, 37.40, '2026-03-10 11:00:00'),
(3, 3, 4, 4.10, 8.50, 17.60, '2026-03-10 11:30:00'),
(4, 4, 3, 5.00, 10.99, 17.97, '2026-03-10 12:10:00'),
(5, 5, 2, 6.50, 12.99, 12.98, '2026-03-10 12:40:00');

INSERT INTO PurchaseOrder VALUES
(1, 'PO-2026-001', 1, 'Sent', '2026-03-08 09:00:00', '2026-03-08 09:00:00', 'USB drive replenishment'),
(2, 'PO-2026-002', 2, 'Received', '2026-03-08 09:20:00', '2026-03-09 15:10:00', 'Paper restock'),
(3, 'PO-2026-003', 3, 'Partial', '2026-03-08 09:40:00', '2026-03-10 08:30:00', 'Water order'),
(4, 'PO-2026-004', 4, 'Draft', '2026-03-08 10:00:00', '2026-03-08 10:00:00', 'Glove reorder'),
(5, 'PO-2026-005', 5, 'Closed', '2026-03-08 10:20:00', '2026-03-10 13:15:00', 'Flashlight shipment completed');

INSERT INTO PurchaseOrderItem VALUES
(1, 1, 1, 25, 0, 7.75),
(2, 2, 2, 40, 40, 3.10),
(3, 3, 3, 24, 12, 3.95),
(4, 4, 4, 15, 0, 4.80),
(5, 5, 5, 20, 20, 6.25);

INSERT INTO InventoryTransaction VALUES
(1, 1, 1, 'STOCK_OUT', -5, 'Sale completed', '2026-03-10 10:15:00'),
(2, 2, 5, 'STOCK_OUT', -10, 'Sale completed', '2026-03-10 11:00:00'),
(3, 3, 3, 'STOCK_OUT', -4, 'Sale completed', '2026-03-10 11:30:00'),
(4, 2, 2, 'PO_RECEIPT', 40, 'Purchase order received', '2026-03-09 15:10:00'),
(5, 5, 4, 'ADJUSTMENT', -2, 'Inventory count correction', '2026-03-10 09:45:00');
