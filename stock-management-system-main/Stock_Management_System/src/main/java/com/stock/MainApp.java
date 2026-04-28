package com.stock;

import com.stock.dao.Schema;
import com.stock.ui.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        Schema.init();

        MainView root = new MainView();
        Scene scene = new Scene(root, 1000, 600);

        stage.setTitle("Stock Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}