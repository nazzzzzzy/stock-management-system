package com.stock.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    private static final String URL = "jdbc:sqlite:inventory.db";

    public static Connection getConnection() throws SQLException {
         return DriverManager.getConnection(URL);
    }
}
