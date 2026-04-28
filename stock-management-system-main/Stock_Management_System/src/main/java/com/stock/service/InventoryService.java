package com.stock.service;

import com.stock.dao.ProductDAO;
import com.stock.model.Product;
import com.stock.model.ProductReportRow;

import java.util.List;

public class InventoryService {
    private final ProductDAO dao = new ProductDAO();




    public SaleResult sell(int productId, int sellQty) throws Exception {
        if (sellQty <= 0) throw new IllegalArgumentException("Quantity must be > 0");

        Product p = dao.getById(productId);
        if (p == null) throw new IllegalArgumentException("Product not found: id=" + productId);

        if (p.getQuantity() < sellQty) {
            throw new IllegalArgumentException("Not enough stock. Available=" + p.getQuantity());
        }

        int newQty = p.getQuantity() - sellQty;
        boolean lowStock = newQty <= p.getReorderPoint();
        boolean autoRestocked = false;

        if (lowStock) {
            newQty = p.getInitialQuantity();
            autoRestocked = true;
        }

        dao.updateQuantity(productId, newQty);

        double profit = (p.getSellPrice() - p.getUnitCost()) * sellQty;

        dao.recordSale(productId, sellQty, p.getUnitCost(), p.getSellPrice(), profit);

        return new SaleResult(
                p.getName(),
                sellQty,
                newQty,
                profit,
                lowStock,
                p.getReorderPoint(),
                autoRestocked
        );
    }



    public record SaleResult(
            String productName,
            int soldQty,
            int remainingQty,
            double profit,
            boolean lowStock,
            int reorderPoint,
            boolean autoRestocked
    ) {}
    public Product restock(int id, int addQty) throws Exception {
        if (addQty <= 0) throw new IllegalArgumentException("Restock quantity must be > 0");

        Product p = dao.getById(id);
        if (p == null) throw new IllegalArgumentException("Product not found: id=" + id);

        boolean ok = dao.addQuantity(id, addQty);
        if (!ok) throw new IllegalStateException("Failed to restock: id=" + id);

        return dao.getById(id);
    }

    public void deleteProduct(int id) throws Exception {
        boolean ok = dao.deleteById(id);
        if (!ok) {
            throw new IllegalArgumentException("Product not found: id=" + id);
        }
    }

    public double totalProfit() throws Exception {
        return dao.totalProfit();
    }

    //Do not modify the backend method.
    public void resetAll() throws Exception {
        dao.resetDatabase();
    }

    public List<ProductReportRow> getProductReport() throws Exception {
        return dao.getProductReport();
    }


}
