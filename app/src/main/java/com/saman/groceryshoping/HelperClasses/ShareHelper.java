package com.saman.groceryshopping.HelperClasses;

import android.content.Context;
import android.content.SharedPreferences;

public class ShareHelper {

    private static final String userDBName = "usersocialdata";
    public SharedPreferences.Editor editor;
    public Context context;
    SharedPreferences userDB;
    int private_mode = 0;

    public ShareHelper(Context context) {
        this.context = context;
        userDB = context.getSharedPreferences(userDBName, private_mode);
    }

    public void putKey(String key, String Value) {
        SharedPreferences.Editor DataDetails = userDB.edit();
        DataDetails.putString(key, Value);
        DataDetails.apply();
    }

    public String getKeyValue(String Key) {
        return userDB.getString(Key, "");
    }

    public void clearShareData() {
        SharedPreferences.Editor clientSpEditor = userDB.edit();
        clientSpEditor.clear();
        clientSpEditor.apply();
    }

}
