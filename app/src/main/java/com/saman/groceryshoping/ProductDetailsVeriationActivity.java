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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
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
import com.saman.groceryshopping.Models.VariationsModel;
import com.saman.groceryshopping.Utils.LoadingDialog;
import com.saman.groceryshopping.databinding.ActivityProductDetailsVeriationBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetailsVeriationActivity extends AppCompatActivity {

    String productID = "";
    LoadingDialog dialog;
    List<ProductsModel> list;
    ActivityProductDetailsVeriationBinding binding;
    List<VariationsModel> variationsList;
    Context context = ProductDetailsVeriationActivity.this;
    private LinearLayoutManager linearLayoutManagerFeature;
    VariationsModel selectedVariation;
    private String variationSize = "";
    private String variationPrice = "", variationID = "";
    private String productName, productPrice, productImage;
    String productCartSize = "", unit = "";
    UserSessionManager userSessionManager;
    Session session;
    boolean liked;
    ShareHelper shareHelper;
    MainActivity home;
    static BottomNavigationView bottomNav;
    static View badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailsVeriationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomNav = binding.bottomNavigation;

        badge = LayoutInflater.from(ProductDetailsVeriationActivity.this).inflate(R.layout.notification_badge
                , binding.bottomNavigation
                , false);

        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        startActivity(new Intent(ProductDetailsVeriationActivity.this, MainActivity.class)
                                .putExtra("home","home"));
                        break;
                    case R.id.nav_favorites:
                        startActivity(new Intent(ProductDetailsVeriationActivity.this, MainActivity.class)
                                .putExtra("favorite","favorite"));
                        break;
                    case R.id.nav_images:
                        startActivity(new Intent(ProductDetailsVeriationActivity.this, MainActivity.class)
                                .putExtra("gallery", "gallery"));
                        break;

                    case R.id.nav_cart:
                        startActivity(new Intent(ProductDetailsVeriationActivity.this, MainActivity.class)
                                .putExtra("cart", "cart"));
                        break;

                    case R.id.nav_profile:
                        startActivity(new Intent(ProductDetailsVeriationActivity.this, MainActivity.class)
                                .putExtra("profile", "profile"));
                        break;
                }
                return true;
            }
        });

        liked = getIntent().getBooleanExtra("liked", false);
        productID = getIntent().getStringExtra("product_id");
        initViews();
        binding.cart2.setAlpha(0.9f);
        getProductDetails();
        if (!shareHelper.getKeyValue("productactual").equals("")) {
            binding.pdvActual.setText("RS. " + shareHelper.getKeyValue("productactual"));
            binding.pdvActual.setVisibility(View.VISIBLE);
            binding.pdvActual.setPaintFlags(binding.pdvActual.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        if (liked) {
            binding.pdvFev.setImageResource(R.drawable.animated_heart);
        }

        binding.pdvFev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFev();
            }
        });
        binding.pdvAddcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (variationSize.equals("")) {
                    Toast.makeText(context, "Select Size", Toast.LENGTH_SHORT).show();
                } else {
                    ProductsModel productsModel = new ProductsModel(Integer.valueOf(productID), productName, variationPrice, URLHelper.BASE_URL_IMAGE + productImage, "home", unit, variationSize, variationID, true, new ShareHelper(ProductDetailsVeriationActivity.this).getKeyValue("productactual"), new ShareHelper(ProductDetailsVeriationActivity.this).getKeyValue("productdiscount"), liked);
                    addToCart(productsModel);
                    productsModel.setPrice(Double.valueOf(variationPrice));
                    productsModel.setQuantity(1);
                    int qty = (int) productsModel.getQuantity();
                    binding.pdvTotal.setText(String.valueOf(qty));
                    binding.pdvAddcart.setVisibility(View.GONE);
                    binding.pdvPlusminusView.setVisibility(View.VISIBLE);
                    home.ShowBadge(UserSessionManager.cart.getTotalItems());
                    ShowBadge(UserSessionManager.cart.getTotalItems());
                }
            }
        });

        binding.pdvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductsModel productsModel = new ProductsModel(Integer.valueOf(productID), productName, variationPrice, URLHelper.BASE_URL_IMAGE + productImage, "home", unit, variationSize, variationID, true, new ShareHelper(ProductDetailsVeriationActivity.this).getKeyValue("productactual"), new ShareHelper(ProductDetailsVeriationActivity.this).getKeyValue("productdiscount"), liked);
                addToCart(productsModel);
                productsModel.setPrice(Double.valueOf(variationPrice));
                productsModel.setQuantity(UserSessionManager.cart.getItemQuantity(Integer.valueOf(productID)));
                int qty = (int) productsModel.getQuantity();
                binding.pdvTotal.setText(String.valueOf(qty));
                home.ShowBadge(UserSessionManager.cart.getTotalItems());
                ShowBadge(UserSessionManager.cart.getTotalItems());
            }
        });
        binding.pdvMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductsModel productsModel = new ProductsModel(Integer.valueOf(productID), productName, variationPrice, URLHelper.BASE_URL_IMAGE + productImage, "home", unit, variationSize, variationID, true, new ShareHelper(ProductDetailsVeriationActivity.this).getKeyValue("productactual"), new ShareHelper(ProductDetailsVeriationActivity.this).getKeyValue("productdiscount"), liked);
                UserSessionManager.cart.removeFromCart(productsModel);
                int qty = (int) UserSessionManager.cart.getItemQuantity(Integer.valueOf(productID));
                if (UserSessionManager.cart.getTotalItems() == 0) {
                    home.ShowBadge(0);
                    ShowBadge(0);
                    UserSessionManager.cart.productList.clear();
                    UserSessionManager.cart = null;
                } else {
                    //  UserSessionManager.cart.setBadge(UserSessionManager.cart.getTotalItems() + "");
                }
                if (qty == 0) {
                    binding.pdvPlusminusView.setVisibility(View.GONE);
                    binding.pdvAddcart.setVisibility(View.VISIBLE);
                    return;
                }
                productsModel.setPrice(Double.valueOf(variationPrice));
                binding.pdvTotal.setText("" + qty);
                home.ShowBadge(UserSessionManager.cart.getTotalItems());
                ShowBadge(UserSessionManager.cart.getTotalItems());

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


        if (UserSessionManager.cart.getTotalItems() != 0){
            ProductDetailsVeriationActivity.showBadge(ProductDetailsVeriationActivity.this, ProductDetailsVeriationActivity.bottomNav, R.id.nav_cart, String.valueOf(UserSessionManager.cart.getTotalItems()));
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
                        binding.pdvFev.setImageResource(R.drawable.animated_heart);
                        binding.pdvFev.setClickable(false);
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
                map.put("product_id", productID);
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.getCache().clear();
        queue.add(stringRequest);
    }

    void GoToMultiTypeActivity(String request_type) {
        Intent intent = new Intent(getApplicationContext(), MultiTypesProductsActivity.class);
        intent.putExtra("request_type", request_type);
        startActivity(intent);
    }

    private void addToCart(ProductsModel product) {
        if (UserSessionManager.cart == null) {
            UserSessionManager.cart = new Cart(ProductDetailsVeriationActivity.this);
        }
        UserSessionManager.cart.addToCart(product);
        int totalItems = UserSessionManager.cart.getTotalItems();
        UserSessionManager.cart.setBadge("" + totalItems);
    }

    private void getProductDetails() {
        variationsList = new ArrayList<>();
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.GET_PRODUCT_WITH_VARIATIONS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject objectVar = null;
                    JSONObject object = new JSONObject(response);
                    boolean status = object.getBoolean("status");
                    if (status) {
                        JSONObject productdetail = object.getJSONObject("product");
                        productName = productdetail.getString("product_name");
                        productPrice = productdetail.getString("unit_price");
                        productImage = productdetail.getString("product_image");
                        unit = productdetail.getString("unit");
                        JSONArray array = object.getJSONArray("variants");
                        for (int i = 0; i < array.length(); i++) {
                            objectVar = array.getJSONObject(i);
                            variationsList.add(new VariationsModel(objectVar.getString("variant"), objectVar.getString("variant_id"), objectVar.getString("price")));
                        }
                        setVariationsSpinner();
                        Picasso.get().load(URLHelper.BASE_URL_IMAGE + productImage).into(binding.pdvImage);
                        binding.pdvPrice.setText("RS " + productPrice);
                        binding.pdvQuantity.setText(unit);
                        binding.pdvName.setText(productName);
                        CheckAlreadyAdded();
                    } else {
                        Toast.makeText(context, "Status False", Toast.LENGTH_SHORT).show();
                    }
                    getData();
                } catch (JSONException e) {
                    dialog.dismiss();
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getProductDetails();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("product_id", productID);
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.getCache().clear();
        queue.add(stringRequest);
    }

    private void CheckAlreadyAdded() {
        if (UserSessionManager.cart != null) {
            int quantity = UserSessionManager.cart.getItemQuantity(Integer.valueOf(productID));
            if (quantity != 0) {
                binding.pdvTotal.setText(String.valueOf(UserSessionManager.cart.getItemQuantity(Integer.valueOf(productID))));
                variationSize = UserSessionManager.cart.getVariationID(Integer.valueOf(productID));
                variationPrice = UserSessionManager.cart.getVariationPrice(Integer.valueOf(productID));
                binding.pdvPlusminusView.setVisibility(View.VISIBLE);
                binding.pdvAddcart.setVisibility(View.GONE);
            } else {
                binding.pdvPlusminusView.setVisibility(View.GONE);
                binding.pdvAddcart.setVisibility(View.VISIBLE);
            }
        }
    }

    void setVariationsSpinner() {

        SpinnerAdapter spinnerAdater;
        List<String> cities = getVeriationsForSpinner();

        spinnerAdater = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cities);
        binding.pdvSpinner.setAdapter(spinnerAdater);

        binding.pdvSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tvSelectedCity = parent.getItemAtPosition(position).toString();
                if (tvSelectedCity.equals("Select Size")) {
                    selectedVariation = null;
                    //  areaVillageSpinnerLayout.setVisibility(View.GONE);
                    return;
                }

                for (VariationsModel item : variationsList) {
                    if (item.getName().equals(tvSelectedCity)) {
                        selectedVariation = item;
                    }
                }
                variationSize = selectedVariation.getName();
                variationPrice = selectedVariation.getPrice();
                variationID = selectedVariation.getId();
                binding.pdvPrice.setText("RS " + variationPrice);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private List<String> getVeriationsForSpinner() {
        List<String> cities = new ArrayList<>();
        cities.add("Select Size");
        for (VariationsModel item : variationsList) {
            cities.add(item.getName());
        }
        return cities;
    }


    private void initViews() {
        home=new MainActivity();
        shareHelper = new ShareHelper(context);
        dialog = new LoadingDialog(context);
        linearLayoutManagerFeature = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.pdvRecycler.setLayoutManager(linearLayoutManagerFeature);
        userSessionManager = new UserSessionManager(context);
        session = userSessionManager.getSessionDetails();
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

                        HomeAdapter adapter = new HomeAdapter(list, ProductDetailsVeriationActivity.this);
                        binding.pdvRecycler.setAdapter(adapter);
                    } else {
                        Toast.makeText(ProductDetailsVeriationActivity.this, "Status False", Toast.LENGTH_SHORT).show();
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
                map.put("product_id", productID);
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

        if (UserSessionManager.cart.getTotalItems() != 0){
            ProductDetailsVeriationActivity.showBadge(ProductDetailsVeriationActivity.this, ProductDetailsVeriationActivity.bottomNav, R.id.nav_cart, String.valueOf(UserSessionManager.cart.getTotalItems()));
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
            ProductDetailsVeriationActivity.removeBadge(ProductDetailsVeriationActivity.bottomNav, R.id.nav_cart);
        } else {
            ProductDetailsVeriationActivity.showBadge(ProductDetailsVeriationActivity.this, ProductDetailsVeriationActivity.bottomNav, R.id.nav_cart, String.valueOf(item));
        }
    }
}
