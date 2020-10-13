package com.saman.groceryshopping.Models;

public class PurchaseModel {
    public String products, status, id,price,date;

    public PurchaseModel(String products, String status, String id, String price, String date) {
        this.products = products;
        this.status = status;
        this.id = id;
        this.price = price;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getPrice() {
        return price;
    }

    public String getId() {
        return id;
    }

    public String getProducts() {
        return products;
    }

    public String getStatus() {
        return status;
    }

}
