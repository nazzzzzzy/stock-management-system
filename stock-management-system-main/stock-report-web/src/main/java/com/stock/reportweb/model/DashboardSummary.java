package com.stock.reportweb.model;

public class DashboardSummary {
    private int totalProducts;
    private int totalSalesRecords;
    private int totalSoldQty;
    private double totalRevenue;
    private double totalProfit;

    public DashboardSummary(int totalProducts, int totalSalesRecords, int totalSoldQty, double totalRevenue, double totalProfit) {
        this.totalProducts = totalProducts;
        this.totalSalesRecords = totalSalesRecords;
        this.totalSoldQty = totalSoldQty;
        this.totalRevenue = totalRevenue;
        this.totalProfit = totalProfit;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public int getTotalSalesRecords() {
        return totalSalesRecords;
    }

    public int getTotalSoldQty() {
        return totalSoldQty;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public double getTotalProfit() {
        return totalProfit;
    }
}