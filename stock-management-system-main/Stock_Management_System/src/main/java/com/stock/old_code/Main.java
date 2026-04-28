package com.stock.old_code;

import com.stock.dao.Schema;

public class Main {
    public static void main(String[] args) {
        Schema.init();
        new CLI().start();
    }
}
