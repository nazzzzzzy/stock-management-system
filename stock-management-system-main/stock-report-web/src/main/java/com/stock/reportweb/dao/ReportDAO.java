package com.stock.reportweb.dao;

import com.stock.reportweb.model.DashboardSummary;
import com.stock.reportweb.model.SaleRecordRow;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.stock.reportweb.model.SalesTrendPoint;
import com.stock.reportweb.model.TopProductPoint;

public class ReportDAO {

    public DashboardSummary getDashboardSummary() throws Exception {
        try (Connection c = DB.getConnection();
             Statement s = c.createStatement()) {

            int totalProducts = 0;
            int totalSalesRecords = 0;
            int totalSoldQty = 0;
            double totalRevenue = 0;
            double totalProfit = 0;

            ResultSet rs1 = s.executeQuery("SELECT COUNT(*) AS total FROM product");
            if (rs1.next()) totalProducts = rs1.getInt("total");

            ResultSet rs2 = s.executeQuery("SELECT COUNT(*) AS total FROM sales");
            if (rs2.next()) totalSalesRecords = rs2.getInt("total");

            ResultSet rs3 = s.executeQuery("SELECT COALESCE(SUM(qty), 0) AS total FROM sales");
            if (rs3.next()) totalSoldQty = rs3.getInt("total");

            ResultSet rs4 = s.executeQuery("SELECT COALESCE(SUM(qty * sell_price), 0) AS total FROM sales");
            if (rs4.next()) totalRevenue = rs4.getDouble("total");

            ResultSet rs5 = s.executeQuery("SELECT COALESCE(SUM(profit), 0) AS total FROM sales");
            if (rs5.next()) totalProfit = rs5.getDouble("total");

            return new DashboardSummary(
                    totalProducts,
                    totalSalesRecords,
                    totalSoldQty,
                    totalRevenue,
                    totalProfit
            );
        }
    }

    public List<SaleRecordRow> getAllSalesRecords() throws Exception {
        List<SaleRecordRow> list = new ArrayList<>();

        String sql = """
                SELECT s.id,
                       p.name AS product_name,
                       s.qty,
                       s.unit_cost,
                       s.sell_price,
                       s.profit,
                       s.created_at
                FROM sales s
                JOIN product p ON s.product_id = p.id
                ORDER BY s.id DESC
                """;

        try (Connection c = DB.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new SaleRecordRow(
                        rs.getInt("id"),
                        rs.getString("product_name"),
                        rs.getInt("qty"),
                        rs.getDouble("unit_cost"),
                        rs.getDouble("sell_price"),
                        rs.getDouble("profit"),
                        rs.getString("created_at")
                ));
            }
        }

        return list;
    }

    public List<SalesTrendPoint> getSalesTrend() throws Exception {
        List<SalesTrendPoint> list = new ArrayList<>();

        String sql = """
            SELECT substr(created_at, 1, 10) AS sales_date,
                   COALESCE(SUM(qty * sell_price), 0) AS total_revenue,
                   COALESCE(SUM(profit), 0) AS total_profit
            FROM sales
            GROUP BY substr(created_at, 1, 10)
            ORDER BY sales_date
            """;

        try (Connection c = DB.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new SalesTrendPoint(
                        rs.getString("sales_date"),
                        rs.getDouble("total_revenue"),
                        rs.getDouble("total_profit")
                ));
            }
        }

        return list;
    }

    public List<TopProductPoint> getTopProducts() throws Exception {
        List<TopProductPoint> list = new ArrayList<>();

        String sql = """
            SELECT p.name AS product_name,
                   COALESCE(SUM(s.qty), 0) AS total_qty,
                   COALESCE(SUM(s.qty * s.sell_price), 0) AS total_revenue,
                   COALESCE(SUM(s.profit), 0) AS total_profit
            FROM sales s
            JOIN product p ON s.product_id = p.id
            GROUP BY p.id, p.name
            ORDER BY total_qty DESC
            LIMIT 10
            """;

        try (Connection c = DB.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new TopProductPoint(
                        rs.getString("product_name"),
                        rs.getInt("total_qty"),
                        rs.getDouble("total_revenue"),
                        rs.getDouble("total_profit")
                ));
            }
        }

        return list;
    }
}