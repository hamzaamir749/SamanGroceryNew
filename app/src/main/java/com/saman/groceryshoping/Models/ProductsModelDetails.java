package com.saman.groceryshopping.Models;

public class ProductsModelDetails {
    public int mID;
    public String mName, mPrice, mImage,activitytype,quantityUnit;

    public ProductsModelDetails(int mID, String mName, String mPrice, String mImage, String activitytype, String quantityUnit) {
        this.mID = mID;
        this.mName = mName;
        this.mPrice = mPrice;
        this.mImage = mImage;
        this.activitytype = activitytype;
        this.quantityUnit = quantityUnit;
    }

    public ProductsModelDetails() {
    }

    public String getQuantityUnit() {
        return quantityUnit;
    }

    public int getmID() {
        return mID;
    }

    public String getmName() {
        return mName;
    }

    public String getmPrice() {
        return mPrice;
    }

    public String getmImage() {
        return mImage;
    }

    public String getActivitytype() {
        return activitytype;
    }
}
