package com.stock.reportweb.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class AuthDB {

    private static final String URL = "jdbc:sqlite:web_auth.db";

    static {
        init();
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void init() {
        String usersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL
                );
                """;

        String defaultAdmin = """
                INSERT OR IGNORE INTO users (id, username, password)
                VALUES (1, 'admin', '%s');
                """.formatted(PasswordUtil.hash("admin123"));

        try (Connection c = getConnection();
             Statement s = c.createStatement()) {
            s.execute(usersTable);
            s.execute(defaultAdmin);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize auth database: " + e.getMessage(), e);
        }
    }
}