package com.saman.groceryshopping;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.saman.groceryshopping.Adapters.HomeVerticalAdapter;
import com.saman.groceryshopping.HelperClasses.Session;
import com.saman.groceryshopping.HelperClasses.URLHelper;
import com.saman.groceryshopping.HelperClasses.UserSessionManager;
import com.saman.groceryshopping.Models.ProductsModel;
import com.saman.groceryshopping.Utils.LoadingDialog;
import com.saman.groceryshopping.databinding.ActivityMultiTypesProductsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiTypesProductsActivity extends AppCompatActivity {
    ActivityMultiTypesProductsBinding binding;
    String request_type;
    Context context = MultiTypesProductsActivity.this;
    LoadingDialog dialog;
    List<ProductsModel> list;
    UserSessionManager userSessionManager;
    static BottomNavigationView bottomNav;
    static View badge;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMultiTypesProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        bottomNav = binding.bottomNavigation;

        badge = LayoutInflater.from(MultiTypesProductsActivity.this).inflate(R.layout.notification_badge
                , binding.bottomNavigation
                , false);

        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        startActivity(new Intent(MultiTypesProductsActivity.this, MainActivity.class)
                                .putExtra("home", "home"));
                        break;
                    case R.id.nav_favorites:
                        startActivity(new Intent(MultiTypesProductsActivity.this, MainActivity.class)
                                .putExtra("favorite", "favorite"));
                        break;

                    case R.id.nav_images:
                        startActivity(new Intent(MultiTypesProductsActivity.this, MainActivity.class)
                                .putExtra("gallery", "gallery"));
                        break;

                    case R.id.nav_cart:
                        startActivity(new Intent(MultiTypesProductsActivity.this, MainActivity.class)
                                .putExtra("cart", "cart"));
                        break;
                    case R.id.nav_profile:
                        startActivity(new Intent(MultiTypesProductsActivity.this, MainActivity.class)
                                .putExtra("profile", "profile"));
                        break;
                }
                return true;
            }
        });

        request_type = getIntent().getStringExtra("request_type");
        binding.cart2.setText(request_type);
        initViews();
        binding.backwardCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getData();
        binding.cart2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (binding.cart2.getText().toString().isEmpty()) {
                        binding.cart2.setError("Please Enter here");
                    } else {
                        GoToMultiTypeActivity(binding.cart2.getText().toString());
                    }
                    return true;
                }
                return false;
            }
        });


        if (UserSessionManager.cart.getTotalItems() != 0){
            MultiTypesProductsActivity.showBadge(MultiTypesProductsActivity.this, MultiTypesProductsActivity.bottomNav, R.id.nav_cart, String.valueOf(UserSessionManager.cart.getTotalItems()));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        dialog.dismiss();
        initViews();
        getData();

        if (UserSessionManager.cart.getTotalItems() != 0){
            MultiTypesProductsActivity.showBadge(MultiTypesProductsActivity.this, MultiTypesProductsActivity.bottomNav, R.id.nav_cart, String.valueOf(UserSessionManager.cart.getTotalItems()));
        }
    }

    void GoToMultiTypeActivity(String request_type) {
        Intent intent = new Intent(getApplicationContext(), MultiTypesProductsActivity.class);
        intent.putExtra("request_type", request_type);
        startActivity(intent);
        finish();
    }

    private void getData() {
        list = new ArrayList<>();
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.GET_SEARCH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("homeres", response);
                try {
                    JSONObject objectProducts,searchobject,searchobject2;
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        JSONArray arrayProducts = jsonObject.getJSONArray("products");

                            for (int k = 0; k < arrayProducts.length(); k++) {
                                objectProducts = arrayProducts.getJSONObject(k);
                                if (request_type.equals("onsale")) {
                                    list.add(new ProductsModel(objectProducts.getInt("product_id"), objectProducts.getString("product_name"), objectProducts.getString("unit_price"), URLHelper.BASE_URL_IMAGE + objectProducts.getString("product_image"), "home", objectProducts.getString("unit"), "", "", objectProducts.getBoolean("isveriation"), objectProducts.getString("originalprice"), objectProducts.getString("discount"), objectProducts.getBoolean("like")));
                                } else {
                                    list.add(new ProductsModel(objectProducts.getInt("product_id"), objectProducts.getString("product_name"), objectProducts.getString("unit_price"), URLHelper.BASE_URL_IMAGE + objectProducts.getString("product_image"), "home", objectProducts.getString("unit"), "", "", objectProducts.getBoolean("isveriation"), "", "", objectProducts.getBoolean("like")));
                                }
                        }
                        if (list.isEmpty()) {
                            binding.mtpMessage.setVisibility(View.VISIBLE);
                            return;
                        }
                        HomeVerticalAdapter adapter = new HomeVerticalAdapter(list, context);
                        binding.mtpRecycler.setAdapter(adapter);
                    } else {
                        Toast.makeText(context, "Status False", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("homeerror", error.toString());
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("type", request_type);
                map.put("user_id", session.getId());
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(400000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.getCache().clear();
        queue.add(stringRequest);
    }


    private void initViews() {
        dialog = new LoadingDialog(context);
        binding.mtpRecycler.setLayoutManager(new LinearLayoutManager(context));
        userSessionManager = new UserSessionManager(context);
        session = userSessionManager.getSessionDetails();
    }


    public void ShowBadge(int item) {
        if (item == 0) {
            MultiTypesProductsActivity.removeBadge(MultiTypesProductsActivity.bottomNav, R.id.nav_cart);
        } else {
            MultiTypesProductsActivity.showBadge(MultiTypesProductsActivity.this, MultiTypesProductsActivity.bottomNav, R.id.nav_cart, String.valueOf(item));
        }
    }

    public static void showBadge(Context context, BottomNavigationView
            bottomNavigationView, @IdRes int itemId, String value) {
        removeBadge(bottomNavigationView, itemId);
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);

        TextView text = badge.findViewById(R.id.badge_text_view);
        text.setText(value);
        itemView.addView(badge);
    }

    public static void removeBadge(BottomNavigationView bottomNavigationView, @IdRes int itemId) {
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        if (itemView.getChildCount() == 3) {
            itemView.removeViewAt(2);
        }
    }

}
