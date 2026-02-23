package com.nequi.franchises.application.dto;

import java.util.List;

public class FranchiseResponse {

    private String id;
    private String name;
    private List<BranchResponse> branches;

    public FranchiseResponse() {
    }

    public FranchiseResponse(String id, String name, List<BranchResponse> branches) {
        this.id = id;
        this.name = name;
        this.branches = branches;
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

    public List<BranchResponse> getBranches() {
        return branches;
    }

    public void setBranches(List<BranchResponse> branches) {
        this.branches = branches;
    }
}
