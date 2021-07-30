package com.example.root.rashidgroupapp.Modal;

public class OrderListModal {
    String docNo;
    String productID;
    String product;
    String unitPrice;
    String quantity;
    String uom;
    double price;
    String customerName;

    public OrderListModal(String docNo,String productID,String product, String unitPrice, String quantity, String uom, double price,String customerName) {
        this.productID   = productID;
        this.product = product;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.uom = uom;
        this.price = price;
        this.docNo = docNo;
        this.customerName = customerName;
    }

    public String getDocNo() {
        return docNo;
    }

    public String getProductID() {
        return productID;
    }

    public String getProduct() {
        return product;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getUom() {
        return uom;
    }

    public double getPrice() {
        return price;
    }

    public String getCustomerName() {
        return customerName;
    }
}
