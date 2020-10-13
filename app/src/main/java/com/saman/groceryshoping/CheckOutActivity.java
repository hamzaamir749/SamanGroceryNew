package com.saman.groceryshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.saman.groceryshopping.HelperClasses.Session;
import com.saman.groceryshopping.HelperClasses.URLHelper;
import com.saman.groceryshopping.HelperClasses.UserSessionManager;
import com.saman.groceryshopping.Models.AddressClass;
import com.saman.groceryshopping.Utils.LoadingDialog;
import com.saman.groceryshopping.databinding.ActivityCheckOutBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class CheckOutActivity extends AppCompatActivity {
    LoadingDialog dialog;
    UserSessionManager userSessionManager;
    Session session;
    ActivityCheckOutBinding binding;
    Context context = CheckOutActivity.this;
    JSONObject jsonObject;
    String priceTotal = "";
    List<AddressClass> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckOutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        priceTotal = String.valueOf(UserSessionManager.cart.getTotalPrice());
        initViews();


    }

    private void SaveInfoToDb() {
        Gson gson = new Gson();

      final String address =gson.toJson(new AddressClass(session.getName(),"",session.getAddress(),"Pakistan",binding.checkoutCity.getText().toString(),"12345",session.getPhone(),"logged"));
       // final String address =gson.toJson(jsonObject);
        final String products = gson.toJson(UserSessionManager.cart.productList);
        dialog.show();

        StringRequest request=new StringRequest(Request.Method.POST, URLHelper.CHECKOUT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        final PrettyDialog pDialog = new PrettyDialog(context);
                        pDialog
                                .setTitle("Status").setIcon(R.drawable.ic_check_circle_black_24dp)
                                .setMessage(jsonObject.getString("message"))
                                .addButton(
                                        "Ok",
                                        R.color.pdlg_color_white,
                                        R.color.colorPrimaryDark,
                                        new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                pDialog.dismiss();
                                            UserSessionManager.cart.productList.clear();
                                                Intent intent = new Intent(getApplicationContext(), ThankYouActivity.class);
                                                intent.putExtra("price_total", priceTotal);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                )
                                .show();
                    } else {
                        final PrettyDialog pDialog = new PrettyDialog(context);
                        pDialog
                                .setTitle("Status").setIcon(R.drawable.ic_error_outline_black_24dp)
                                .setMessage(jsonObject.getString("message"))
                                .addButton(
                                        "Ok",
                                        R.color.pdlg_color_white,
                                        R.color.colorPrimaryDark,
                                        new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                pDialog.dismiss();

                                            }
                                        }
                                )
                                .show();

                    }
                } catch (JSONException e) {
                    Log.d("loginexception", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                SaveInfoToDb();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_id", session.getId());
                map.put("total", priceTotal);
                map.put("products", products);
                map.put("address", address);
                Log.d("checkoutdata",map.toString());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.getCache().clear();
        queue.add(request);
       /* StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.CHECKOUT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("loginres", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        final PrettyDialog pDialog = new PrettyDialog(context);
                        pDialog
                                .setTitle("Status").setIcon(R.drawable.ic_check_circle_black_24dp)
                                .setMessage(jsonObject.getString("message"))
                                .addButton(
                                        "Ok",
                                        R.color.pdlg_color_white,
                                        R.color.colorPrimaryDark,
                                        new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                pDialog.dismiss();
                                            *//*    UserSessionManager.cart.productList.clear();
                                                Intent intent = new Intent(getApplicationContext(), ThankYouActivity.class);
                                                intent.putExtra("price_total", priceTotal);
                                                startActivity(intent);
                                                finish();*//*
                                            }
                                        }
                                )
                                .show();
                    } else {
                        final PrettyDialog pDialog = new PrettyDialog(context);
                        pDialog
                                .setTitle("Status").setIcon(R.drawable.ic_error_outline_black_24dp)
                                .setMessage(jsonObject.getString("message"))
                                .addButton(
                                        "Ok",
                                        R.color.pdlg_color_white,
                                        R.color.colorPrimaryDark,
                                        new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                pDialog.dismiss();

                                            }
                                        }
                                )
                                .show();

                    }
                } catch (JSONException e) {
                    Log.d("loginexception", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                SaveInfoToDb();
            }
        })
        {
            *//*@Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> header=new HashMap<>();
                header.put("Accept","application/json");
                header.put("X-Requested-With","XMLHttpRequest");
                header.put("Content-Type","application/json");
                return header;
            }*//*
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_id", session.getId());
                map.put("total", priceTotal);
            *//*    map.put("products", products);
                map.put("address", address);*//*
                Log.d("checkoutdata",map.toString());
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.getCache().clear();
        queue.add(stringRequest);*/
    }

    private void initViews() {
        dialog = new LoadingDialog(context);
        userSessionManager = new UserSessionManager(context);
        session = userSessionManager.getSessionDetails();
        binding.checkoutTotal.setText("Your Total Bill is: " + priceTotal);

    }
}
