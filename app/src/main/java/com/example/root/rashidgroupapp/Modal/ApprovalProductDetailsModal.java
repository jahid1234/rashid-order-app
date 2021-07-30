package com.example.root.rashidgroupapp.Modal;

public class ApprovalProductDetailsModal {
    String productName;
    String kgQty;
    String bagQty;
    String rate;
    String totalTaka;

    public ApprovalProductDetailsModal(String productName, String kgQty, String bagQty, String rate, String totalTaka) {
        this.productName = productName;
        this.kgQty = kgQty;
        this.bagQty = bagQty;
        this.rate = rate;
        this.totalTaka = totalTaka;
    }

    public String getProductName() {
        return productName;
    }

    public String getKgQty() {
        return kgQty;
    }

    public String getBagQty() {
        return bagQty;
    }

    public String getRate() {
        return rate;
    }

    public String getTotalTaka() {
        return totalTaka;
    }
}
