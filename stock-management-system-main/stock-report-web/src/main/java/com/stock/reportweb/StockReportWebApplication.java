package com.stock.reportweb;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StockReportWebApplication {
    public static void main(String[] args) {
//        Schema.init();不再创建数据库
        SpringApplication.run(StockReportWebApplication.class, args);
    }
}