package com.stock.model;

public class ProductReportRow {
    private int id;
    private String name;
    private int soldQty;
    private int currentQty;
    private double totalCost;
    private double totalSales;
    private double grossProfit;
    private double unitCost;

    public ProductReportRow(int id, String name, int soldQty, int currentQty,
                            double totalCost, double totalSales, double grossProfit,double unitCost) {
        this.id = id;
        this.name = name;
        this.soldQty = soldQty;
        this.currentQty = currentQty;
        this.totalCost = totalCost;
        this.totalSales = totalSales;
        this.grossProfit = grossProfit;
        this.unitCost = unitCost;

    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getSoldQty() { return soldQty; }
    public int getCurrentQty() { return currentQty; }
    public double getTotalCost() { return totalCost; }
    public double getTotalSales() { return totalSales; }
    public double getGrossProfit() { return grossProfit; }
    public double getUnitCost() { return unitCost; }
}