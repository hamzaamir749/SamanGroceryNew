package com.saman.groceryshopping.Models;

public class VariationsModel {
    String name,id,price;

    public VariationsModel(String name, String id, String price) {
        this.name = name;
        this.id = id;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPrice() {
        return price;
    }
}
