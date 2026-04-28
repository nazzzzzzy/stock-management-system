package com.stock.reportweb.model;

public class TopProductPoint {
    private String productName;
    private int totalQty;
    private double totalRevenue;
    private double totalProfit;

    public TopProductPoint(String productName, int totalQty, double totalRevenue, double totalProfit) {
        this.productName = productName;
        this.totalQty = totalQty;
        this.totalRevenue = totalRevenue;
        this.totalProfit = totalProfit;
    }

    public String getProductName() {
        return productName;
    }

    public int getTotalQty() {
        return totalQty;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public double getTotalProfit() {
        return totalProfit;
    }
}