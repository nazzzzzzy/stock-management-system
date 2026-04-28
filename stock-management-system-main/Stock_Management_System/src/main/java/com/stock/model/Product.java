package com.stock.model;

public class Product {
    private Integer id;
    private String name;
    private int quantity;
    private double unitCost;
    private double sellPrice;
    private int reorderPoint;
    private int initialQuantity;

    public Product() {}

    public Product(String name, int quantity, double unitCost, double sellPrice, int reorderPoint) {
        this.name = name;
        this.quantity = quantity;
        this.unitCost = unitCost;
        this.sellPrice = sellPrice;
        this.reorderPoint = reorderPoint;
        this.initialQuantity = quantity;
    }

    // getters / setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getUnitCost() { return unitCost; }
    public void setUnitCost(double unitCost) { this.unitCost = unitCost; }

    public double getSellPrice() { return sellPrice; }
    public void setSellPrice(double sellPrice) { this.sellPrice = sellPrice; }

    public int getReorderPoint() { return reorderPoint; }
    public void setReorderPoint(int reorderPoint) { this.reorderPoint = reorderPoint; }

    public int getInitialQuantity() { return initialQuantity; }
    public void setInitialQuantity(int initialQuantity) { this.initialQuantity = initialQuantity; }

    @Override
    public String toString() {
        return String.format("Product{id=%s, name='%s', qty=%d, cost=%.2f, price=%.2f, reorder=%d}",
                id, name, quantity, unitCost, sellPrice, reorderPoint);
    }
}
