package com.stock.reportweb.controller;

import com.stock.reportweb.dao.DB;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/db")
public class DatabaseController {

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Object> uploadDatabase(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();

        try {
            if (file.isEmpty()) {
                result.put("success", false);
                result.put("message", "No file selected.");
                return result;
            }

            String originalName = file.getOriginalFilename();
            if (originalName == null || !originalName.toLowerCase().endsWith(".db")) {
                result.put("success", false);
                result.put("message", "Only .db files are allowed.");
                return result;
            }
            if (file.getSize() > 10 * 1024 * 1024) {
                result.put("success", false);
                result.put("message", "Database file is too large. Maximum size is 10 MB.");
                return result;
            }

            File tempDir = DB.getTempDir();
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }

            File target = DB.getTempDbFile();

            try (FileOutputStream fos = new FileOutputStream(target)) {
                fos.write(file.getBytes());
            }

            validateDatabase(target);

            result.put("success", true);
            result.put("message", "Database uploaded successfully.");
            result.put("fileName", originalName);
            result.put("path", target.getAbsolutePath());

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Upload failed: " + e.getMessage());
        }

        return result;
    }

    @GetMapping("/status")
    public Map<String, Object> getDatabaseStatus() {
        Map<String, Object> result = new HashMap<>();
        File dbFile = DB.getTempDbFile();

        result.put("selected", dbFile.exists());
        result.put("path", dbFile.exists() ? dbFile.getAbsolutePath() : null);

        return result;
    }

    private void validateDatabase(File target) throws Exception {
        String url = "jdbc:sqlite:" + target.getAbsolutePath();
        try (Connection c = java.sql.DriverManager.getConnection(url);
             Statement s = c.createStatement()) {
            ResultSet product = s.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='product'");
            boolean hasProduct = product.next();
            ResultSet sales = s.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='sales'");
            boolean hasSales = sales.next();
            if (!hasProduct || !hasSales) {
                throw new IllegalArgumentException("Uploaded file is missing required product and sales tables.");
            }
        }
    }
}
