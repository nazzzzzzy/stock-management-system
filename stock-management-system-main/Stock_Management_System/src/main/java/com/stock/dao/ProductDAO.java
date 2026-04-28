package com.stock.dao;

import com.stock.model.Product;
import com.stock.model.ProductReportRow;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.stock.model.ProductReportRow;

public class ProductDAO {

    public int insert(Product p) throws SQLException {
        String sql = "INSERT INTO product(name, quantity, initial_quantity, unit_cost, sell_price, reorder_point) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getName());
            ps.setInt(2, p.getQuantity());
            ps.setInt(3, p.getInitialQuantity());
            ps.setDouble(4, p.getUnitCost());
            ps.setDouble(5, p.getSellPrice());
            ps.setInt(6, p.getReorderPoint());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            return -1;
        }
    }

    public Product getById(int id) throws SQLException {
        String sql = "SELECT * FROM product WHERE id=?";

        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return mapRow(rs);
            }
        }
    }

    public List<Product> listAll() throws SQLException {
        return searchProducts("", false);
    }

    public List<Product> searchProducts(String term, boolean lowStockOnly) throws SQLException {
        List<Product> out = new ArrayList<>();
        String normalized = term == null ? "" : term.trim();
        boolean hasTerm = !normalized.isEmpty();

        StringBuilder sql = new StringBuilder("SELECT * FROM product WHERE 1=1");
        if (hasTerm) {
            sql.append(" AND (LOWER(name) LIKE LOWER(?) OR CAST(id AS TEXT) LIKE ?)");
        }
        if (lowStockOnly) {
            sql.append(" AND quantity <= reorder_point");
        }
        sql.append(" ORDER BY id");

        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {

            int idx = 1;
            if (hasTerm) {
                String like = "%" + normalized + "%";
                ps.setString(idx++, like);
                ps.setString(idx++, like);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(mapRow(rs));
                }
            }
        }
        return out;
    }

    public boolean updateQuantity(int id, int newQty) throws SQLException {
        String sql = "UPDATE product SET quantity=? WHERE id=?";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, newQty);
            ps.setInt(2, id);

            return ps.executeUpdate() == 1;
        }
    }
    public boolean addQuantity(int id, int addQty) throws SQLException {
        String sql = "UPDATE product SET quantity = quantity + ? WHERE id = ?";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, addQty);
            ps.setInt(2, id);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean deleteById(int id) throws SQLException {
        String sql = "DELETE FROM product WHERE id=?";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }


    public void recordSale(int productId, int qty, double unitCost, double sellPrice, double profit) throws SQLException {
        String sql = "INSERT INTO sales(product_id, qty, unit_cost, sell_price, profit) VALUES(?,?,?,?,?)";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setInt(2, qty);
            ps.setDouble(3, unitCost);
            ps.setDouble(4, sellPrice);
            ps.setDouble(5, profit);
            ps.executeUpdate();
        }
    }
    public double totalProfit() throws SQLException {
        String sql = "SELECT COALESCE(SUM(profit),0) AS total FROM sales";
        try (Connection c = DB.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            return rs.getDouble("total");
        }
    }

//Do not modify the backend method.
    public void resetDatabase() throws SQLException {
        try (Connection c = DB.getConnection();
             Statement s = c.createStatement()) {

            s.executeUpdate("DELETE FROM sales");
            s.executeUpdate("DELETE FROM product");

            s.executeUpdate("DELETE FROM sqlite_sequence WHERE name='sales'");
            s.executeUpdate("DELETE FROM sqlite_sequence WHERE name='product'");
        }
    }
//查询报表
    public List<ProductReportRow> getProductReport() throws SQLException {
        String sql = """
    SELECT 
        p.id,
        p.name,
        p.quantity AS current_qty,
        p.unit_cost,
        COALESCE(SUM(s.qty), 0) AS sold_qty,
        COALESCE(SUM(s.qty * s.unit_cost), 0) AS total_cost,
        COALESCE(SUM(s.qty * s.sell_price), 0) AS total_sales,
        COALESCE(SUM(s.profit), 0) AS gross_profit
    FROM product p
    LEFT JOIN sales s ON p.id = s.product_id
    GROUP BY p.id, p.name, p.quantity, p.unit_cost
    ORDER BY p.id
    """;
        List<ProductReportRow> list = new ArrayList<>();

        try (Connection c = DB.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                ProductReportRow row = new ProductReportRow(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("sold_qty"),
                        rs.getInt("current_qty"),
                        rs.getDouble("total_cost"),
                        rs.getDouble("total_sales"),
                        rs.getDouble("gross_profit"),
                        rs.getDouble("unit_cost")
                );
                list.add(row);
            }
        }

        return list;
    }


    private Product mapRow(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getInt("id"));
        p.setName(rs.getString("name"));
        p.setQuantity(rs.getInt("quantity"));
        p.setInitialQuantity(rs.getInt("initial_quantity"));
        p.setUnitCost(rs.getDouble("unit_cost"));
        p.setSellPrice(rs.getDouble("sell_price"));
        p.setReorderPoint(rs.getInt("reorder_point"));
        return p;
    }
}
