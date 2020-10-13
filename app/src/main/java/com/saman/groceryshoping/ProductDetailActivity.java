package com.saman.groceryshopping;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;
import com.saman.groceryshopping.Adapters.HomeAdapter;
import com.saman.groceryshopping.HelperClasses.Cart;
import com.saman.groceryshopping.HelperClasses.Session;
import com.saman.groceryshopping.HelperClasses.ShareHelper;
import com.saman.groceryshopping.HelperClasses.URLHelper;
import com.saman.groceryshopping.HelperClasses.UserSessionManager;
import com.saman.groceryshopping.Models.ProductsModel;
import com.saman.groceryshopping.Utils.LoadingDialog;
import com.saman.groceryshopping.databinding.ActivityProductDetailBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetailActivity extends AppCompatActivity {

    ActivityProductDetailBinding binding;
    ProductsModel product;
    ShareHelper shareHelper;
    private LinearLayoutManager linearLayoutManagerFeature;
    LoadingDialog dialog;
    List<ProductsModel> list;
    UserSessionManager userSessionManager;
    Session session;
    boolean liked;
    Context context = ProductDetailActivity.this;
    static BottomNavigationView bottomNav;
    static View badge;
    MainActivity home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomNav = binding.bottomNavigation;

        badge = LayoutInflater.from(ProductDetailActivity.this).inflate(R.layout.notification_badge
                , binding.bottomNavigation
                , false);

        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        startActivity(new Intent(ProductDetailActivity.this, MainActivity.class)
                                .putExtra("home","home"));
                        break;
                    case R.id.nav_favorites:
                        startActivity(new Intent(ProductDetailActivity.this, MainActivity.class)
                                .putExtra("favorite","favorite"));
                        break;
                    case R.id.nav_images:
                        startActivity(new Intent(ProductDetailActivity.this, MainActivity.class)
                                .putExtra("gallery", "gallery"));
                        break;

                    case R.id.nav_cart:
                        startActivity(new Intent(ProductDetailActivity.this, MainActivity.class)
                                .putExtra("cart", "cart"));
                        break;

                    case R.id.nav_profile:
                        startActivity(new Intent(ProductDetailActivity.this, MainActivity.class)
                                .putExtra("profile", "profile"));
                        break;
                }
                return true;
            }
        });

        liked = getIntent().getBooleanExtra("liked", false);
        initViews();
        shareHelper = new ShareHelper(ProductDetailActivity.this);
        product = new ProductsModel(Integer.valueOf(shareHelper.getKeyValue("productid")), shareHelper.getKeyValue("productname"), shareHelper.getKeyValue("productprice"), shareHelper.getKeyValue("productimage"), "home", shareHelper.getKeyValue("productquantity"), "", "", false, shareHelper.getKeyValue("productactual"), shareHelper.getKeyValue("productdiscount"), liked);
        binding.pdaName.setText(product.getmName());
        binding.cart2.setAlpha(0.9f);
        if (!shareHelper.getKeyValue("productactual").equals("")) {
            binding.pdaActual.setText("RS. "+shareHelper.getKeyValue("productactual"));
            binding.pdaActual.setVisibility(View.VISIBLE);
            binding.pdaActual.setPaintFlags(binding.pdaActual.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        if (liked) {
            binding.pdaFev.setImageResource(R.drawable.animated_heart);
        }

        Picasso.get().load(product.getmImage()).into(binding.pdaImage);
        binding.pdaPrice.setText("RS. "+product.getmPrice());
        binding.pdaQuantity.setText(product.getQuantityUnit());
        final String price = product.getmPrice();
        getData();
        if (UserSessionManager.cart != null) {
            int quantity = UserSessionManager.cart.getItemQuantity(product.getmID());
            if (quantity != 0) {
                binding.pdaTotal.setText(String.valueOf(UserSessionManager.cart.getItemQuantity(product.getmID())));
                binding.pdaPlusminusView.setVisibility(View.VISIBLE);
                binding.pdaAddcart.setVisibility(View.GONE);
            } else {
                binding.pdaPlusminusView.setVisibility(View.GONE);
                binding.pdaAddcart.setVisibility(View.VISIBLE);
            }


        }
        binding.pdaFev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFev();
            }
        });
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
        binding.backwardCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.pdaAddcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("class", product.getmName());
                product.setQuantity(1);
                product.setPrice(Double.valueOf(price));
                addToCart(product);
                binding.pdaTotal.setText(String.valueOf(UserSessionManager.cart.getItemQuantity(product.getmID())));
                binding.pdaPlusminusView.setVisibility(View.VISIBLE);
                binding.pdaAddcart.setVisibility(View.GONE);
                home.ShowBadge(UserSessionManager.cart.getTotalItems());
                ShowBadge(UserSessionManager.cart.getTotalItems());
            }
        });

        binding.pdaAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart(product);
                product.setPrice(Double.valueOf(price));
                product.setQuantity(UserSessionManager.cart.getItemQuantity(product.getmID()));
                int qty = (int) product.getQuantity();
                binding.pdaTotal.setText(String.valueOf(qty));
                home.ShowBadge(UserSessionManager.cart.getTotalItems());
                ShowBadge(UserSessionManager.cart.getTotalItems());
            }
        });
        binding.pdaMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserSessionManager.cart.removeFromCart(product);
                int qty = (int) UserSessionManager.cart.getItemQuantity(product.getmID());
                if (UserSessionManager.cart.getTotalItems() == 0) {
                    //    UserSessionManager.cart.removeBadge();
                    home.ShowBadge(0);
                    ShowBadge(0);
                    UserSessionManager.cart.productList.clear();
                    UserSessionManager.cart = null;
                } else {
                    //  UserSessionManager.cart.setBadge(UserSessionManager.cart.getTotalItems() + "");
                }
                if (qty == 0) {
                    binding.pdaPlusminusView.setVisibility(View.GONE);
                    binding.pdaAddcart.setVisibility(View.VISIBLE);
                    return;
                }
                product.setPrice(Double.valueOf(price));
                binding.pdaTotal.setText("" + qty);
                home.ShowBadge(UserSessionManager.cart.getTotalItems());
                ShowBadge(UserSessionManager.cart.getTotalItems());
            }
        });


        if (UserSessionManager.cart != null){
            if (UserSessionManager.cart.getTotalItems() != 0){
                ProductDetailActivity.showBadge(ProductDetailActivity.this, ProductDetailActivity.bottomNav, R.id.nav_cart, String.valueOf(UserSessionManager.cart.getTotalItems()));
            }
        }
    }

    private void addFev() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.ADD_FEV, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("loginres", response);
                try {
                    JSONObject object;
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        binding.pdaFev.setImageResource(R.drawable.animated_heart);
                        binding.pdaFev.setClickable(false);
                    } else {
                        Toast.makeText(context, "Item already added in your favourite list", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.d("loginexception", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("loginerror", error.toString());
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_id", session.getId());
                map.put("product_id", String.valueOf(product.getmID()));
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.getCache().clear();
        queue.add(stringRequest);
    }

    private void initViews() {
        home= new MainActivity();
        dialog = new LoadingDialog(ProductDetailActivity.this);
        linearLayoutManagerFeature = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.pdaRecycler.setLayoutManager(linearLayoutManagerFeature);
        userSessionManager = new UserSessionManager(ProductDetailActivity.this);
        session = userSessionManager.getSessionDetails();
    }

    void GoToMultiTypeActivity(String request_type) {
        Intent intent = new Intent(getApplicationContext(), MultiTypesProductsActivity.class);
        intent.putExtra("request_type", request_type);
        startActivity(intent);
    }

    private void addToCart(ProductsModel product) {
        if (UserSessionManager.cart == null) {
            UserSessionManager.cart = new Cart(ProductDetailActivity.this);
        }
        UserSessionManager.cart.addToCart(product);
        int totalItems = UserSessionManager.cart.getTotalItems();
        UserSessionManager.cart.setBadge("" + totalItems);
    }

    private void getData() {
        list = new ArrayList<>();
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.GET_SAME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("homeres", response);
                try {
                    JSONObject objectProducts;
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        JSONArray arrayProducts = jsonObject.getJSONArray("products");
                        for (int k = 0; k < arrayProducts.length(); k++) {
                            objectProducts = arrayProducts.getJSONObject(k);
                            list.add(new ProductsModel(objectProducts.getInt("product_id"), objectProducts.getString("product_name"), objectProducts.getString("unit_price"), URLHelper.BASE_URL_IMAGE + objectProducts.getString("product_image"), "home", objectProducts.getString("unit"), "", "", objectProducts.getBoolean("isveriation"), "", "", objectProducts.getBoolean("like")));
                        }

                        HomeAdapter adapter = new HomeAdapter(list, ProductDetailActivity.this);
                        binding.pdaRecycler.setAdapter(adapter);
                    } else {
                        Toast.makeText(ProductDetailActivity.this, "Status False", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
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
                map.put("product_id", String.valueOf(product.getmID()));
                map.put("user_id", session.getId());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.getCache().clear();
        queue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (UserSessionManager.cart != null){
            if (UserSessionManager.cart.getTotalItems() != 0){
                ProductDetailActivity.showBadge(ProductDetailActivity.this, ProductDetailActivity.bottomNav, R.id.nav_cart, String.valueOf(UserSessionManager.cart.getTotalItems()));
            }
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

    public void ShowBadge(int item) {
        if (item == 0) {
            ProductDetailActivity.removeBadge(ProductDetailActivity.bottomNav, R.id.nav_cart);
        } else {
            ProductDetailActivity.showBadge(ProductDetailActivity.this, ProductDetailActivity.bottomNav, R.id.nav_cart, String.valueOf(item));
        }
    }
}
