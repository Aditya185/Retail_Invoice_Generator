package com.adityaprakash.retailinvoicegenerator;

public class Customers {
    public String Name,Phone,purchaseTime;

    public Customers(){

    }

    public Customers(String name, String phone, String purchaseTime) {
        Name = name;
        Phone = phone;
        this.purchaseTime = purchaseTime;
    }

    public  String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public  String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public  String getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(String purchaseTime) {
        this.purchaseTime = purchaseTime;
    }
}
