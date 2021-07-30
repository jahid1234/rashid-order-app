package com.example.root.rashidgroupapp.Modal;

public class OrderSummaryModal {
    String docNo;
    String date;
    String customerName;
    String orderPrice;

    public OrderSummaryModal(String docNo, String date, String customerName, String orderPrice) {
        this.docNo = docNo;
        this.date = date;
        this.customerName = customerName;
        this.orderPrice = orderPrice;
    }

    public String getDocNo() {
        return docNo;
    }

    public String getDate() {
        return date;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getOrderPrice() {
        return orderPrice;
    }
}
