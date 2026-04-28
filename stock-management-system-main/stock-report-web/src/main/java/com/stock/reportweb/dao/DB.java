package com.stock.reportweb.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

    private static final String TEMP_DIR = "temp";
    private static final String TEMP_DB_PATH = TEMP_DIR + "/selected.db";

    public static Connection getConnection() throws SQLException {
        File dbFile = new File(TEMP_DB_PATH);

        if (!dbFile.exists()) {
            throw new SQLException("No database selected. Please upload a desktop database (.db) file first.");
        }

        String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
        System.out.println("Connecting to DB: " + dbFile.getAbsolutePath());
        return DriverManager.getConnection(url);
    }

    public static File getTempDbFile() {
        return new File(TEMP_DB_PATH);
    }

    public static File getTempDir() {
        return new File(TEMP_DIR);
    }

    public static void clearTempDb() {
        File dbFile = getTempDbFile();
        if (dbFile.exists()) {
            dbFile.delete();
        }
    }
}