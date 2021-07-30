package com.example.root.rashidgroupapp.Modal;

/**
 * Created by root on 1/28/20.
 */

public class PartyBalanceModal  {
    String customerCode;
    String customerName;
    String balance;

    public PartyBalanceModal(String customerCode, String customerName, String balance) {
        this.customerCode = customerCode;
        this.customerName = customerName;
        this.balance = balance;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getBalance() {
        return balance;
    }
}
