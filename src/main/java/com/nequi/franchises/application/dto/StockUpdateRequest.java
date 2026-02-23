package com.nequi.franchises.application.dto;

import jakarta.validation.constraints.Min;

public class StockUpdateRequest {

    @Min(value = 0, message = "Stock must be zero or greater")
    private int stock;

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
