package com.stock.reportweb.dao;

import com.stock.reportweb.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    public User findByUsername(String username) throws Exception {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection c = AuthDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
            }
        }

        return null;
    }

    public boolean register(String username, String password) throws Exception {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        if (password == null || password.length() < 6) {
            return false;
        }
        if (findByUsername(username) != null) {
            return false;
        }

        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection c = AuthDB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, username.trim());
            ps.setString(2, PasswordUtil.hash(password));
            return ps.executeUpdate() > 0;
        }
    }

    public boolean login(String username, String password) throws Exception {
        User user = findByUsername(username);
        return user != null && PasswordUtil.matches(password, user.getPassword());
    }
}