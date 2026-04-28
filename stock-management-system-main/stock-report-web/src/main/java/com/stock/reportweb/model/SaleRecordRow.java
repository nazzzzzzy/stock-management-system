package com.stock.reportweb.model;

public class SaleRecordRow {
    private int id;
    private String productName;
    private int qty;
    private double unitCost;
    private double sellPrice;
    private double profit;
    private String createdAt;

    public SaleRecordRow(int id, String productName, int qty, double unitCost, double sellPrice, double profit, String createdAt) {
        this.id = id;
        this.productName = productName;
        this.qty = qty;
        this.unitCost = unitCost;
        this.sellPrice = sellPrice;
        this.profit = profit;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public int getQty() {
        return qty;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public double getProfit() {
        return profit;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}