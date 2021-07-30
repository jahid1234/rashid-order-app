package com.example.root.rashidgroupapp.Modal;

public class OrderListHistoryModal {
    String product;
    String unitPrice;
    String quantity;
    String uom;
    double price;
    String customerName;

    public OrderListHistoryModal(String product, String unitPrice, String quantity, String uom, double price,String customerName) {
        this.product = product;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.uom = uom;
        this.price = price;
        this.customerName = customerName;
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
