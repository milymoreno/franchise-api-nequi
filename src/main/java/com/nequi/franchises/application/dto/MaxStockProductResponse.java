package com.nequi.franchises.application.dto;

public class MaxStockProductResponse {

    private String productId;
    private String productName;
    private int stock;
    private String branchId;
    private String branchName;

    public MaxStockProductResponse() {
    }

    public MaxStockProductResponse(String productId, String productName, int stock,
            String branchId, String branchName) {
        this.productId = productId;
        this.productName = productName;
        this.stock = stock;
        this.branchId = branchId;
        this.branchName = branchName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
}
