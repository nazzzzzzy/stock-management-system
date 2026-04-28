package com.stock.ui;

import com.stock.dao.ProductDAO;
import com.stock.model.Product;
import com.stock.model.ProductReportRow;
import com.stock.service.InventoryService;
import com.stock.service.ProductValidator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainView extends BorderPane {

    private final ProductDAO dao = new ProductDAO();
    private final InventoryService service = new InventoryService();
    private final TextArea messageArea = new TextArea();
    private final TextField searchField = new TextField();
    private final CheckBox lowStockOnlyCheckBox = new CheckBox("Low stock only");
    private final Label summaryLabel = new Label();

    private final TilePane productTilePane = new TilePane();
    private final ScrollPane productScrollPane = new ScrollPane();

    public MainView() {

        setStyle("-fx-background-color: #eef5f0;");


        Label title = new Label("KeanMart");
        title.setStyle(
                "-fx-font-size: 34px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: 'Georgia';"
        );

        StackPane headerPane = new StackPane(title);
        headerPane.setPadding(new Insets(18, 0, 18, 0));
        headerPane.setStyle(
                "-fx-background-color: linear-gradient(to right, #43a047, #66bb6a, #a5d6a7);" +
                        "-fx-background-radius: 0 0 14 14;"
        );

        setTop(headerPane);
        BorderPane.setMargin(headerPane, new Insets(0, 0, 10, 0));


        VBox menu = new VBox(12);
        menu.setPadding(new Insets(15));
        menu.setPrefWidth(190);
        menu.setStyle(
                "-fx-background-color: #eaf7ee;" +   // 浅绿
                        "-fx-background-radius: 12;"
        );


        Button btnView = new Button("View Products");
        Button btnAdd = new Button("Add Product");
        Button btnSell = new Button("Sell Product");
        Button btnRestock = new Button("Restock Product");
        Button btnDelete = new Button("Delete Product");
        Button btnReport = new Button("Report");
        Button btnReset = new Button("Reset Database");

        btnView.setMaxWidth(Double.MAX_VALUE);
        btnAdd.setMaxWidth(Double.MAX_VALUE);
        btnSell.setMaxWidth(Double.MAX_VALUE);
        btnRestock.setMaxWidth(Double.MAX_VALUE);
        btnDelete.setMaxWidth(Double.MAX_VALUE);
        btnReport.setMaxWidth(Double.MAX_VALUE);
        btnReset.setMaxWidth(Double.MAX_VALUE);
        String menuButtonStyle =
                "-fx-background-color: white;" +
                        "-fx-border-color: #b7d9c0;" +
                        "-fx-border-width: 1.5;" +
                        "-fx-text-fill: #2e7d32;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;" +
                        "-fx-padding: 12 18 12 18;";

        btnView.setStyle(menuButtonStyle);
        btnAdd.setStyle(menuButtonStyle);
        btnSell.setStyle(menuButtonStyle);
        btnRestock.setStyle(menuButtonStyle);
        btnDelete.setStyle(menuButtonStyle);
        btnReport.setStyle(menuButtonStyle);
        btnReset.setStyle(
                "-fx-background-color: #e74c3c;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;"
        );

        menu.getChildren().addAll(btnView, btnAdd, btnSell, btnRestock, btnDelete, btnReport, btnReset);
        setLeft(menu);

        createProductCardArea();
        VBox centerArea = new VBox(12, createSearchBar(), summaryLabel, productScrollPane);
        centerArea.setPadding(new Insets(10, 15, 10, 15));
        setCenter(centerArea);

        btnView.setOnAction(e -> {
            searchField.clear();
            lowStockOnlyCheckBox.setSelected(false);
            loadProducts();
        });
        btnAdd.setOnAction(e -> showAddProductDialog());
        btnSell.setOnAction(e -> showSellProductDialog());
        btnRestock.setOnAction(e -> showRestockProductDialog());
        btnDelete.setOnAction(e -> showDeleteProductDialog());
        btnReport.setOnAction(e -> showReportWindow());
        btnReset.setOnAction(e -> showResetDatabaseDialog());

        messageArea.setEditable(false);
        messageArea.setPrefRowCount(4);
        messageArea.setWrapText(true);
        messageArea.setText("System messages will appear here.\n");
        messageArea.setStyle(
                "-fx-control-inner-background: white;" +
                        "-fx-font-size: 15px;" +
                        "-fx-border-color: #dfeee3;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;"
        );
        setBottom(messageArea);
        BorderPane.setMargin(messageArea, new Insets(10));


        loadProducts();
    }

    private HBox createSearchBar() {
        searchField.setPromptText("Search by product name or ID");
        searchField.setPrefWidth(280);

        Button searchButton = new Button("Search");
        Button clearButton = new Button("Clear");
        Button refreshButton = new Button("Refresh");

        String actionButtonStyle =
                "-fx-background-color: white;" +
                "-fx-border-color: #b7d9c0;" +
                "-fx-text-fill: #2e7d32;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 10;" +
                "-fx-border-radius: 10;" +
                "-fx-padding: 10 16 10 16;";

        searchButton.setStyle(actionButtonStyle);
        clearButton.setStyle(actionButtonStyle);
        refreshButton.setStyle(actionButtonStyle);

        searchButton.setOnAction(e -> loadProducts());
        refreshButton.setOnAction(e -> loadProducts());
        clearButton.setOnAction(e -> {
            searchField.clear();
            lowStockOnlyCheckBox.setSelected(false);
            loadProducts();
        });
        searchField.setOnAction(e -> loadProducts());
        lowStockOnlyCheckBox.setOnAction(e -> loadProducts());

        HBox bar = new HBox(10, searchField, lowStockOnlyCheckBox, searchButton, clearButton, refreshButton);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(4, 0, 0, 0));

        summaryLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");
        return bar;
    }

    private void createProductCardArea() {
        productTilePane.setPadding(new Insets(20));
        productTilePane.setHgap(20);
        productTilePane.setVgap(20);
        productTilePane.setPrefTileWidth(220);
        productTilePane.setPrefTileHeight(220);
        productTilePane.setStyle("-fx-background-color: transparent;");

        productScrollPane.setContent(productTilePane);
        productScrollPane.setFitToWidth(true);
        productScrollPane.setStyle(
                "-fx-background: #e9f2ec;" +
                        "-fx-background-color: #e9f2ec;" +
                        "-fx-border-color: #d0e2d6;" +
                        "-fx-border-radius: 14;" +
                        "-fx-background-radius: 14;"
        );
    }

    private void loadProducts() {
        try {
            productTilePane.getChildren().clear();

            var products = dao.searchProducts(searchField.getText(), lowStockOnlyCheckBox.isSelected());
            int lowStockCount = 0;
            for (Product product : products) {
                if (product.getQuantity() <= product.getReorderPoint()) {
                    lowStockCount++;
                }
            }
            summaryLabel.setText(String.format("Showing %d product(s) | Low stock: %d", products.size(), lowStockCount));

            if (products.isEmpty()) {
                Label emptyLabel = new Label("No products match the current search/filter.");
                emptyLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: gray;");
                productTilePane.getChildren().add(emptyLabel);
                return;
            }

            for (Product product : products) {
                productTilePane.getChildren().add(createProductCard(product));
            }
        } catch (Exception e) {
            showError("Failed to load products: " + e.getMessage());
        }
    }

    private StackPane createProductCard(Product product) {
        Label iconText = new Label(product.getName().substring(0, 1).toUpperCase());
        iconText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333333;");


        Label idLabel = new Label("ID: " + product.getId());
        idLabel.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #2e7d32;"
        );

        StackPane iconBox = new StackPane(iconText);
        iconBox.setPrefSize(48, 48);
        iconBox.setMaxSize(48, 48);
        iconBox.setStyle(
                "-fx-background-color: #dff3e5;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #b7e4c7;" +
                        "-fx-border-radius: 12;"
        );

        Label nameLabel = new Label(product.getName());
        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        nameLabel.setWrapText(true);

        Label priceLabel = new Label(String.format("Price: %.2f", product.getSellPrice()));
        priceLabel.setStyle("-fx-font-size: 14px;");

        Label stockLabel = new Label("Stock: " + product.getQuantity());
        if (product.getQuantity() <= product.getReorderPoint()) {
            stockLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #d9534f; -fx-font-weight: bold;");
        } else {
            stockLabel.setStyle("-fx-font-size: 14px;");
        }

        Button detailsButton = new Button("View Details");
        detailsButton.setMaxWidth(Double.MAX_VALUE);
        detailsButton.setOnAction(e -> showProductDetails(product));

        VBox content = new VBox(10, iconBox, nameLabel, priceLabel, stockLabel, detailsButton);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(15));
        StackPane card = new StackPane(content, idLabel);
        StackPane.setAlignment(idLabel, Pos.TOP_RIGHT);
        StackPane.setMargin(idLabel, new Insets(8, 10, 0, 0));

        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(200);
        card.setMinHeight(200);
        card.setPadding(new Insets(15));
        card.setStyle(
                "-fx-background-color: #ffffff;" +
                        "-fx-border-color: #e8f5e9;" +
                        "-fx-border-radius: 14;" +
                        "-fx-background-radius: 14;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 10, 0, 0, 3);"
        );

        return card;
    }

    private void showProductDetails(Product product) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Product Details");

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(okButtonType);


        Label iconText = new Label(product.getName().substring(0, 1).toUpperCase());
        iconText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");

        StackPane iconBox = new StackPane(iconText);
        iconBox.setPrefSize(56, 56);
        iconBox.setMaxSize(56, 56);
        iconBox.setStyle(
                "-fx-background-color: #eaf7ee;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #b9e3c5;" +
                        "-fx-border-radius: 12;"
        );


        Label nameLabel = new Label(product.getName());
        nameLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        nameLabel.setMaxWidth(Double.MAX_VALUE);
        nameLabel.setAlignment(Pos.CENTER);

        VBox topSection = new VBox(12, iconBox, nameLabel);
        topSection.setAlignment(Pos.CENTER);

        // 左列
        Label leftCol = new Label(
                "Product ID: " + product.getId() + "\n\n" +
                        "Current Quantity: " + product.getQuantity() + "\n\n" +
                        "Unit Cost: " + String.format("%.2f", product.getUnitCost())
        );
        leftCol.setStyle("-fx-font-size: 18px;");
        leftCol.setWrapText(true);

        // 右列
        Label rightCol = new Label(
                "Initial Quantity: " + product.getInitialQuantity() + "\n\n" +
                        "Sell Price: " + String.format("%.2f", product.getSellPrice()) + "\n\n" +
                        "Reorder Point: " + product.getReorderPoint()
        );
        rightCol.setStyle("-fx-font-size: 18px;");
        rightCol.setWrapText(true);

        HBox infoSection = new HBox(40, leftCol, rightCol);
        infoSection.setAlignment(Pos.CENTER);
        infoSection.setPadding(new Insets(10, 0, 0, 0));

        HBox.setHgrow(leftCol, Priority.ALWAYS);
        HBox.setHgrow(rightCol, Priority.ALWAYS);
        leftCol.setMaxWidth(Double.MAX_VALUE);
        rightCol.setMaxWidth(Double.MAX_VALUE);

        VBox content = new VBox(20, topSection, infoSection);
        content.setPadding(new Insets(25));
        content.setPrefWidth(520);
        content.setStyle("-fx-background-color: white;");

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().setMinWidth(560);

        dialog.showAndWait();
    }

    private void showAddProductDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Product");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        TextField nameField = new TextField();
        TextField qtyField = new TextField();
        TextField costField = new TextField();
        TextField priceField = new TextField();
        TextField reorderField = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Label("Quantity:"), 0, 1);
        grid.add(qtyField, 1, 1);

        grid.add(new Label("Unit Cost:"), 0, 2);
        grid.add(costField, 1, 2);

        grid.add(new Label("Sell Price:"), 0, 3);
        grid.add(priceField, 1, 3);

        grid.add(new Label("Reorder Point:"), 0, 4);
        grid.add(reorderField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(result -> {
            if (result == addButtonType) {
                try {
                    String name = nameField.getText().trim();
                    int qty = Integer.parseInt(qtyField.getText().trim());
                    double cost = Double.parseDouble(costField.getText().trim());
                    double price = Double.parseDouble(priceField.getText().trim());
                    int reorder = Integer.parseInt(reorderField.getText().trim());

                    ProductValidator.validateNewProduct(name, qty, cost, price, reorder);
                    Product product = new Product(name, qty, cost, price, reorder);
                    dao.insert(product);

                    loadProducts();
                    appendMessage("Product added successfully.");
                    showInfo("Product added successfully.");
                } catch (Exception e) {
                    showError("Failed to add product: " + e.getMessage());
                }
            }
        });
    }

    private void showSellProductDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Sell Product");

        ButtonType sellButtonType = new ButtonType("Sell", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(sellButtonType, ButtonType.CANCEL);

        TextField idField = new TextField();
        TextField qtyField = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        grid.add(new Label("Product ID:"), 0, 0);
        grid.add(idField, 1, 0);

        grid.add(new Label("Quantity to Sell:"), 0, 1);
        grid.add(qtyField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(result -> {
            if (result == sellButtonType) {
                try {
                    int productId = Integer.parseInt(idField.getText().trim());
                    int qty = Integer.parseInt(qtyField.getText().trim());

                    InventoryService.SaleResult saleresult = service.sell(productId, qty);

                    loadProducts();

                    String message = "Product sold successfully.";
                    if (saleresult.autoRestocked()) {
                        message += " Auto-restock triggered.";
                    }

                    appendMessage(message);
                    showInfo(message);
                } catch (Exception e) {
                    showError("Failed to sell product: " + e.getMessage());
                }
            }
        });
    }

    private void showRestockProductDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Restock Product");

        ButtonType restockButtonType = new ButtonType("Restock", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(restockButtonType, ButtonType.CANCEL);

        TextField idField = new TextField();
        TextField qtyField = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        grid.add(new Label("Product ID:"), 0, 0);
        grid.add(idField, 1, 0);

        grid.add(new Label("Quantity to Add:"), 0, 1);
        grid.add(qtyField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(result -> {
            if (result == restockButtonType) {
                try {
                    int productId = Integer.parseInt(idField.getText().trim());
                    int addQty = Integer.parseInt(qtyField.getText().trim());

                    service.restock(productId, addQty);

                    loadProducts();
                    appendMessage("Product restocked successfully.");
                    showInfo("Product restocked successfully.");
                } catch (Exception e) {
                    showError("Failed to restock product: " + e.getMessage());
                }
            }
        });
    }

    private void showDeleteProductDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Delete Product");

        ButtonType deleteButtonType = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(deleteButtonType, cancelButtonType);

        TextField idField = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        grid.add(new Label("Product ID:"), 0, 0);
        grid.add(idField, 1, 0);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(result -> {
            if (result == deleteButtonType) {
                try {
                    int productId = Integer.parseInt(idField.getText().trim());

                    service.deleteProduct(productId);

                    loadProducts();
                    appendMessage("Product deleted successfully.");
                    showInfo("Product deleted successfully.");
                } catch (Exception e) {
                    showError("Failed to delete product: " + e.getMessage());
                }
            }
        });
    }

    private void showReportWindow() {
        try {
            var report = service.getProductReport();

            Stage stage = new Stage();
            stage.setTitle("Inventory Profit Report");

            TableView<ProductReportRow> reportTable = new TableView<>();

            TableColumn<ProductReportRow, Integer> colId = new TableColumn<>("ID");
            colId.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("id"));

            TableColumn<ProductReportRow, String> colName = new TableColumn<>("Name");
            colName.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("name"));

            TableColumn<ProductReportRow, Integer> colSoldQty = new TableColumn<>("Sold Qty");
            colSoldQty.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("soldQty"));

            TableColumn<ProductReportRow, Integer> colCurrentQty = new TableColumn<>("Current Qty");
            colCurrentQty.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("currentQty"));

            TableColumn<ProductReportRow, Double> colTotalCost = new TableColumn<>("Total Cost");
            colTotalCost.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("totalCost"));

            TableColumn<ProductReportRow, Double> colTotalSales = new TableColumn<>("Total Sales");
            colTotalSales.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("totalSales"));

            TableColumn<ProductReportRow, Double> colGrossProfit = new TableColumn<>("Gross Profit");
            colGrossProfit.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("grossProfit"));

            reportTable.getColumns().addAll(
                    colId, colName, colSoldQty, colCurrentQty,
                    colTotalCost, colTotalSales, colGrossProfit
            );

            reportTable.getItems().addAll(report);
            reportTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            int totalSold = 0;
            double totalCost = 0;
            double totalSales = 0;
            double totalProfit = 0;
            double inventoryValue = 0;

            for (ProductReportRow r : report) {
                totalSold += r.getSoldQty();
                totalCost += r.getTotalCost();
                totalSales += r.getTotalSales();
                totalProfit += r.getGrossProfit();
                inventoryValue += r.getCurrentQty() * r.getUnitCost();
            }

            Label totalSoldLabel = new Label("Total Sold Qty: " + totalSold);
            Label totalCostLabel = new Label(String.format("Total Cost: %.2f", totalCost));
            Label totalSalesLabel = new Label(String.format("Total Sales: %.2f", totalSales));
            Label totalProfitLabel = new Label(String.format("Total Profit: %.2f", totalProfit));
            Label inventoryValueLabel = new Label(String.format("Current Inventory Value: %.2f", inventoryValue));

            VBox root = new VBox(10);
            root.setPadding(new Insets(15));
            root.getChildren().addAll(
                    reportTable,
                    totalSoldLabel,
                    totalCostLabel,
                    totalSalesLabel,
                    totalProfitLabel,
                    inventoryValueLabel
            );

            Scene scene = new Scene(root, 1000, 600);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            showError("Failed to load report: " + e.getMessage());
        }
    }

    private void showResetDatabaseDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Reset Database");

        ButtonType resetButtonType = new ButtonType("Reset", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(resetButtonType, cancelButtonType);

        Label warningLabel = new Label("This will DELETE ALL products and sales records.\nType RESET to confirm:");
        TextField confirmField = new TextField();

        VBox box = new VBox(10);
        box.setPadding(new Insets(20));
        box.getChildren().addAll(warningLabel, confirmField);

        dialog.getDialogPane().setContent(box);

        dialog.showAndWait().ifPresent(result -> {
            if (result == resetButtonType) {
                try {
                    String confirm = confirmField.getText().trim();
                    if (!"RESET".equalsIgnoreCase(confirm)) {
                        showError("Reset cancelled. You must type RESET.");
                        return;
                    }

                    service.resetAll();

                    loadProducts();
                    appendMessage("Database reset successfully.");
                    showInfo("Database reset successfully.");
                } catch (Exception e) {
                    showError("Failed to reset database: " + e.getMessage());
                }
            }
        });
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Operation Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void appendMessage(String message) {
        messageArea.appendText(message + "\n");
    }
}