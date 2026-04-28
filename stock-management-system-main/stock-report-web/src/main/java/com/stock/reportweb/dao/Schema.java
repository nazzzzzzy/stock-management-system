package com.stock.reportweb.dao;

import java.sql.Connection;
import java.sql.Statement;

public class Schema {

    public static void init() {
        String productTable = """
        CREATE TABLE IF NOT EXISTS product(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            quantity INTEGER NOT NULL,
            initial_quantity INTEGER NOT NULL,
            unit_cost REAL NOT NULL,
            sell_price REAL NOT NULL,
            reorder_point INTEGER NOT NULL
        );
        """;

        String salesTable = """
        CREATE TABLE IF NOT EXISTS sales (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            product_id INTEGER NOT NULL,
            qty INTEGER NOT NULL,
            unit_cost REAL NOT NULL,
            sell_price REAL NOT NULL,
            profit REAL NOT NULL,
            created_at TEXT DEFAULT (datetime('now'))
        );
        """;

        try (Connection c = DB.getConnection();
             Statement s = c.createStatement()) {
            s.execute(productTable);
            s.execute(salesTable);
        } catch (Exception e) {
            throw new RuntimeException("Schema init failed: " + e.getMessage(), e);
        }
    }
}