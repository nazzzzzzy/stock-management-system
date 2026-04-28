package com.stock.service;

public final class ProductValidator {
    private ProductValidator() {}

    public static void validateNewProduct(String name, int qty, double cost, double price, int reorderPoint) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name is required.");
        }
        if (qty < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        if (cost < 0) {
            throw new IllegalArgumentException("Unit cost cannot be negative.");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Sell price cannot be negative.");
        }
        if (price < cost) {
            throw new IllegalArgumentException("Sell price should be greater than or equal to unit cost.");
        }
        if (reorderPoint < 0) {
            throw new IllegalArgumentException("Reorder point cannot be negative.");
        }
        if (reorderPoint > qty) {
            throw new IllegalArgumentException("Reorder point should not be greater than starting quantity.");
        }
    }
}
