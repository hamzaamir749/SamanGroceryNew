package com.saman.groceryshopping.HelperClasses;

import android.content.Context;
import android.util.Log;
import android.view.View;
import com.saman.groceryshopping.Models.ProductsModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Cart {
    public  List<ProductsModel> productList=null;
    Context context;

    public Cart(Context context) {
        productList = new ArrayList<>();
        this.context = context;
    }
    public void addToCart(ProductsModel productModel) {
        Iterator iterator = productList.iterator();

        while (iterator.hasNext()) {
            ProductsModel item = (ProductsModel) iterator.next();
            if (item.getmID() == productModel.getmID()) {
                int quantity = item.getQuantity() + 1;
                productModel.setQuantity(quantity);
                iterator.remove();
                productList.add(productModel);
                return;
            }
        }
        productList.add(productModel);
        Log.d("listadata",String.valueOf(productModel.getmID())+ productModel.getmName());
    }

    public String getVariationPrice(int ProductId){
        Iterator itr = productList.iterator();
        while (itr.hasNext()) {
            ProductsModel item = (ProductsModel) itr.next();
            if (item.getmID() == ProductId) {
                return  item.getmPrice();
            }
        }
        return "0";
    }
    public String getVariationID(int ProductId){
        Iterator itr = productList.iterator();
        while (itr.hasNext()) {
            ProductsModel item = (ProductsModel) itr.next();
            if (item.getmID() == ProductId) {
                return  item.getSizeid();
            }
        }
        return "0";
    }
    public void removeFromCart(ProductsModel productModel) {
        Iterator iterator = productList.iterator();
        while (iterator.hasNext()) {
            ProductsModel item = (ProductsModel) iterator.next();
            if (item.getmID() == productModel.getmID()) {
                int quantity = item.getQuantity() - 1;
                productModel.setQuantity(quantity);
                iterator.remove();
                if (quantity > 0) {
                    productList.add(productModel);
                }
                return;
            }

        }
    }


    public void setBadge(String value){
        if(UserSessionManager.tvBadge!=null){
            //createBadge();
            UserSessionManager.tvBadge.setVisibility(View.VISIBLE);
            UserSessionManager.tvBadge.setText(value);
        }
    }

    public void removeBadge(){
        UserSessionManager.tvBadge.setVisibility(View.GONE);
    }


    public int getTotalItems() {
        int qty = 0;
        for (int i = 0; i < productList.size(); i++) {
            qty += productList.get(i).getQuantity();
        }
        return qty;
    }

    public int getItemQuantity(int productID) {
        Iterator itr = productList.iterator();
        while (itr.hasNext()) {
            ProductsModel item = (ProductsModel) itr.next();
            if (item.getmID() == productID) {
                return (int) item.getQuantity();
            }
        }
        return 0;
    }
    public double getItemPrice(int productID) {
        Iterator itr = productList.iterator();
        while (itr.hasNext()) {
            ProductsModel item = (ProductsModel) itr.next();
            if (item.getmID() == productID) {
                return (double) item.getOrignalPrice();
            }
        }
        return 0;
    }
    public double getTotalPrice() {
        double price = 0;
        for (int i = 0; i < productList.size(); i++) {
            double prc = Double.valueOf(productList.get(i).getPrice());
            price += (prc * Double.valueOf(productList.get(i).getQuantity()));
        }

        return price;
    }

    public void removeProduct(ProductsModel product) {
        Iterator itr = productList.iterator();
        while (itr.hasNext()) {
            ProductsModel item = (ProductsModel) itr.next();
            if (item.getmID() == product.getmID()) {
                itr.remove();
                return;
            }
        }
    }

}
