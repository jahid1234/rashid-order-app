package com.example.root.rashidgroupapp.Modal;

/**
 * Created by root on 1/26/20.
 */

public class SkuStatementModal {
    String productName;
    String avlQty;
    String unit;

    public SkuStatementModal(String productName, String avlQty, String unit) {
        this.productName = productName;
        this.avlQty = avlQty;
        this.unit = unit;
    }

    public String getProductName() {
        return productName;
    }

    public String getAvlQty() {
        return avlQty;
    }

    public String getUnit() {
        return unit;
    }
}
