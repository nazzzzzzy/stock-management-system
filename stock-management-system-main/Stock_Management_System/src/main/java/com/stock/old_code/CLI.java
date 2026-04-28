package com.stock.old_code;

import com.stock.dao.ProductDAO;
import com.stock.service.InventoryService;
import com.stock.model.Product;

import java.util.List;
import java.util.Scanner;

public class CLI {
    private final Scanner sc = new Scanner(System.in);
    private final ProductDAO dao = new ProductDAO();


    public void start() {
        while (true) {
            System.out.println("\n=== Stock Management System ===");
            System.out.println("1) Add Product");
            System.out.println("2) List Products");
            System.out.println("3) Sell Product");
            System.out.println("4) Restock Product");
            System.out.println("5) Delete Product");
            System.out.println("6) Inventory Profit Report");
            System.out.println("0) Exit");
            System.out.print("Choice: ");

            int choice = readInt();
            try {
                switch (choice) {
                    case 1 -> addProduct();
                    case 2 -> listProducts();
                    case 3 -> sellProduct();
                    case 4 -> restockProduct();
                    case 5 -> deleteProduct();
                    case 6 -> Inventory_Profit_Report();
                    case -1 -> resetDatabase();
                    case 0 -> {
                        System.out.println("Thank you for using Stock Management System!");
                        return;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    private void addProduct() throws Exception {
        System.out.print("Name: ");
        String name = sc.nextLine().trim();

        System.out.print("Quantity: ");
        int qty = readInt();

        System.out.print("Unit cost: ");
        double cost = readDouble();

        System.out.print("Sell price: ");
        double price = readDouble();

        System.out.print("Reorder point: ");
        int rp = readInt();

        int id = dao.insert(new Product(name, qty, cost, price, rp));
        System.out.println(" Added product id=" + id);
    }

    private void listProducts() throws Exception {
        List<Product> list = dao.listAll();
        if (list.isEmpty()) {
            System.out.println("(No products yet)");
            return;
        }

        System.out.println("\nProduct list:");
        System.out.println("ID | Name | Qty | Cost | Price | Reorder");
        for (Product p : list) {
            System.out.printf("%d | %s | %d | %.2f | %.2f | %d%n",
                    p.getId(), p.getName(), p.getQuantity(),
                    p.getUnitCost(), p.getSellPrice(), p.getReorderPoint());
        }
        System.out.println();
    }

    private void sellProduct() throws Exception {
        listProducts();

        System.out.print("Product ID: ");
        int id = readInt();


        System.out.print("Quantity to sell: ");
        int qty = readInt();

        var service = new com.stock.service.InventoryService();
        var result = service.sell(id, qty);

        System.out.printf("down! Sold %d of %s. Remaining=%d%n",
                result.soldQty(), result.productName(), result.remainingQty());

        System.out.printf("Profit for this sale: %.2f%n", result.profit());

        if (result.lowStock()) {
            System.out.printf("!!REMIND!!: Low stock! Reorder point=%d%n", result.reorderPoint());
        }
        if (result.autoRestocked()) {
            System.out.println("Auto-restocked to initial quantity.");
        }
    }

    private void restockProduct() throws Exception {
        listProducts();

        System.out.print("Product ID: ");
        int id = readInt();

        System.out.print("Quantity to add: ");
        int addQty = readInt();

        try {
            var service = new InventoryService();
            Product updated = service.restock(id, addQty);
            System.out.printf("OK, Restocked. New quantity=%d%n", updated.getQuantity());
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private void deleteProduct() throws Exception {
        listProducts();

        System.out.print("Product ID to delete: ");
        int id = readInt();

        System.out.print("Confirm delete? (y/n): ");
        String confirm = sc.nextLine().trim().toLowerCase();
        if (!confirm.equals("y")) {
            System.out.println("Cancelled.");
            return;
        }

        try {
            var service = new InventoryService();
            service.deleteProduct(id);
            System.out.println("down! Deleted product id=" + id);
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }


    private int readInt() {
        while (true) {
            String s = sc.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (Exception e) {
                System.out.print("Enter an integer: ");
            }
        }
    }

    private double readDouble() {
        while (true) {
            String s = sc.nextLine().trim();
            try {
                return Double.parseDouble(s);
            } catch (Exception e) {
                System.out.print("Enter a number: ");
            }
        }
    }

    private void Inventory_Profit_Report() throws Exception {
        InventoryService service = new InventoryService();
        var report = service.getProductReport();

        System.out.println("\n========================= Inventory Profit Report =========================");
        System.out.printf("%-3s | %-10s | %-8s | %-11s | %-10s | %-11s | %-12s%n",
                "ID", "Name", "Sold Qty", "Current Qty", "Total Cost", "Total Sales", "Gross Profit");

        double sumCost = 0;
        double sumSales = 0;
        double sumProfit = 0;
        int sumSold = 0;
        double inventoryValue = 0;

        for (var r : report) {
            System.out.printf("%-3d | %-10s | %-8d | %-11d | %-10.2f | %-11.2f | %-12.2f%n",
                    r.getId(),
                    r.getName(),
                    r.getSoldQty(),
                    r.getCurrentQty(),
                    r.getTotalCost(),
                    r.getTotalSales(),
                    r.getGrossProfit());

            sumSold += r.getSoldQty();
            sumCost += r.getTotalCost();
            sumSales += r.getTotalSales();
            sumProfit += r.getGrossProfit();

            inventoryValue += r.getCurrentQty() * r.getUnitCost();
        }

        System.out.println("------------------------------------------------------------------------");
        System.out.printf("%-3s | %-10s | %-8d | %-11s | %-10.2f | %-11.2f | %-12.2f%n",
                "", "TOTAL", sumSold, "", sumCost, sumSales, sumProfit);
        System.out.printf("Current Inventory Value: %.2f%n", inventoryValue);
    }

    //--Do not modify the backend method.
    private void resetDatabase() throws Exception {
        System.out.println("This will DELETE ALL products and ALL sales records!");
        System.out.print("Type RESET to confirm: ");
        String confirm = sc.nextLine().trim();

        if (!confirm.equalsIgnoreCase("RESET")) {
            System.out.println("Cancelled.");
            return;
        }

        var service = new InventoryService();
        service.resetAll();

        System.out.println(" Database reset done (products + sales cleared).");
    }



}
