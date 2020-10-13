package com.saman.groceryshopping.Models;

public class ProductsModel {

    public int mID;
    public String mName, mPrice, mImage,activitytype,quantityUnit,size,sizeid;
    boolean isVeriations;
    String actualprice,discount;
    boolean isLike;
    public int quantity;
    public double orignalPrice;
    public double price=0;

    public ProductsModel(int mID, String mName, String mPrice, String mImage, String activitytype, String quantityUnit, String size, String sizeid, boolean isVeriations, String actualprice, String discount, boolean isLike) {
        this.mID = mID;
        this.mName = mName;
        this.mPrice = mPrice;
        this.mImage = mImage;
        this.activitytype = activitytype;
        this.quantityUnit = quantityUnit;
        this.size = size;
        this.sizeid = sizeid;
        this.isVeriations = isVeriations;
        this.actualprice = actualprice;
        this.discount = discount;
        this.isLike = isLike;
    }

    public boolean isLike() {
        return isLike;
    }

    public String getActualprice() {
        return actualprice;
    }

    public String getDiscount() {
        return discount;
    }

    public void setActivitytype(String activitytype) {
        this.activitytype = activitytype;
    }

    public String getQuantityUnit() {
        return quantityUnit;
    }

    public void setQuantityUnit(String quantityUnit) {
        this.quantityUnit = quantityUnit;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSizeid() {
        return sizeid;
    }

    public void setSizeid(String sizeid) {
        this.sizeid = sizeid;
    }

    public boolean isVeriations() {
        return isVeriations;
    }

    public void setVeriations(boolean veriations) {
        isVeriations = veriations;
    }

    public double getOrignalPrice() {
        return orignalPrice;
    }

    public void setOrignalPrice(double orignalPrice) {
        this.orignalPrice = orignalPrice;
    }

    public String getActivitytype() {
        return activitytype;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public int getmID() {
        return mID;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }
}
