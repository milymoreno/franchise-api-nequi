package com.nequi.franchises.application.dto;

import java.util.List;

public class BranchResponse {

    private String id;
    private String name;
    private List<ProductResponse> products;

    public BranchResponse() {
    }

    public BranchResponse(String id, String name, List<ProductResponse> products) {
        this.id = id;
        this.name = name;
        this.products = products;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductResponse> getProducts() {
        return products;
    }

    public void setProducts(List<ProductResponse> products) {
        this.products = products;
    }
}
