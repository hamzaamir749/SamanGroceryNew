package com.saman.groceryshopping.Models;

public class CategoriesModel {
    String id,name,discription,image;

    public CategoriesModel(String id, String name, String discription, String image) {
        this.id = id;
        this.name = name;
        this.discription = discription;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDiscription() {
        return discription;
    }

    public String getImage() {
        return image;
    }
}
