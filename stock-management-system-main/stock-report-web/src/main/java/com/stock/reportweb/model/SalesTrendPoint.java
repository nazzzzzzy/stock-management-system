package com.stock.reportweb.model;

public class SalesTrendPoint {
    private String date;
    private double revenue;
    private double profit;

    public SalesTrendPoint(String date, double revenue, double profit) {
        this.date = date;
        this.revenue = revenue;
        this.profit = profit;
    }

    public String getDate() {
        return date;
    }

    public double getRevenue() {
        return revenue;
    }

    public double getProfit() {
        return profit;
    }
}