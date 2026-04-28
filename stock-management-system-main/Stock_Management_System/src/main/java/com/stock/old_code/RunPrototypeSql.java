package com.stock.old_code;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class RunPrototypeSql {
    public static void main(String[] args) {
        String dbUrl = "jdbc:sqlite:group_prototype.db";
        String sqlFile = "C:\\Users\\32898\\Desktop\\Stock_Management_System-main\\Stock_Management_System-main\\Stock_Management_System\\Stock_Management_M6_Prototype_Database.sql";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {

            String content = Files.readString(Path.of(sqlFile), StandardCharsets.UTF_8);

            StringBuilder current = new StringBuilder();
            for (String line : content.split("\\R")) {
                String trimmed = line.trim();

                if (trimmed.isEmpty() || trimmed.startsWith("--")) {
                    continue;
                }

                current.append(line).append("\n");

                if (trimmed.endsWith(";")) {
                    String sql = current.toString().trim();
                    if (!sql.isEmpty()) {
                        stmt.execute(sql);
                    }
                    current.setLength(0);
                }
            }

            System.out.println("SQL executed successfully.");

            System.out.println("\nTables:");
            try (ResultSet rs = stmt.executeQuery(
                    "SELECT name FROM sqlite_master WHERE type='table' ORDER BY name")) {
                while (rs.next()) {
                    System.out.println(rs.getString("name"));
                }
            }

            System.out.println("\nProduct sample data:");
            try (ResultSet rs = stmt.executeQuery(
                    "SELECT product_id, sku, name, qty_on_hand, initial_quantity, unit_cost, sell_price FROM Product LIMIT 5")) {
                while (rs.next()) {
                    System.out.println(
                            rs.getInt("product_id") + " | " +
                                    rs.getString("sku") + " | " +
                                    rs.getString("name") + " | " +
                                    rs.getInt("qty_on_hand") + " | " +
                                    rs.getInt("initial_quantity") + " | " +
                                    rs.getDouble("unit_cost") + " | " +
                                    rs.getDouble("sell_price")
                    );
                }
            }

            System.out.println("\nCategory sample data:");
            try (ResultSet rs = stmt.executeQuery(
                    "SELECT category_id, name, description, is_active FROM Category LIMIT 5")) {
                while (rs.next()) {
                    System.out.println(
                            rs.getInt("category_id") + " | " +
                                    rs.getString("name") + " | " +
                                    rs.getString("description") + " | " +
                                    rs.getInt("is_active")
                    );
                }
            }

            System.out.println("\nUser sample data:");
            try (ResultSet rs = stmt.executeQuery(
                    "SELECT user_id, username, is_active, created_at, role_id FROM User LIMIT 5")) {
                while (rs.next()) {
                    System.out.println(
                            rs.getInt("user_id") + " | " +
                                    rs.getString("username") + " | " +
                                    rs.getInt("is_active") + " | " +
                                    rs.getString("created_at") + " | " +
                                    rs.getInt("role_id")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}