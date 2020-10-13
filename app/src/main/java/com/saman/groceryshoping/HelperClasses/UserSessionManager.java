package com.saman.groceryshopping.HelperClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import java.util.ArrayList;

public class UserSessionManager {
    private static final String userDBName = "userData";
    public SharedPreferences.Editor editor;
    public Context context;
    SharedPreferences userDB;
    int private_mode = 0;
    public static Cart cart;
    public static TextView tvBadge;
    public static ArrayList<String> sliderImages;
    public UserSessionManager(Context context) {
        this.context = context;
        userDB = context.getSharedPreferences(userDBName, private_mode);
    }

    public Session getSessionDetails() {
        String id = userDB.getString("id", "");
        String name = userDB.getString("name", "");
        String address = userDB.getString("address", "");
        String paddress = userDB.getString("paddress", "");
        String phoneno = userDB.getString("phone", "");
        Session sessionDetails = new Session(id,name,phoneno,address,paddress);
        return sessionDetails;
    }

    public void setSessionDetails(Session sessionDetails) {


        SharedPreferences.Editor DataDetails = userDB.edit();
        DataDetails.putString("id", sessionDetails.getId());
        DataDetails.putString("name", sessionDetails.getName());
        DataDetails.putString("address", sessionDetails.getAddress());
        DataDetails.putString("paddress", sessionDetails.getPaddress());
        DataDetails.putString("phone", sessionDetails.getPhone());

        DataDetails.apply();

    }

    public boolean isLoggedIn() {
        return userDB.getBoolean("loggedIn", false);
    }

    public void setLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor riderSpEditor = userDB.edit();
        riderSpEditor.putBoolean("loggedIn", loggedIn);
        riderSpEditor.putString("user", "All");
        riderSpEditor.apply();
    }

    public void clearSessionData() {
        SharedPreferences.Editor clientSpEditor = userDB.edit();
        clientSpEditor.clear();
        clientSpEditor.apply();
    }


}