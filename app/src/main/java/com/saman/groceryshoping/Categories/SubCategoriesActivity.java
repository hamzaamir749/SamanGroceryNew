package com.saman.groceryshopping.Categories;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.saman.groceryshopping.Adapters.SectionsPagerAdapter;
import com.saman.groceryshopping.HelperClasses.Session;
import com.saman.groceryshopping.HelperClasses.URLHelper;
import com.saman.groceryshopping.HelperClasses.UserSessionManager;
import com.saman.groceryshopping.MainActivity;
import com.saman.groceryshopping.Models.CategoryModel;
import com.saman.groceryshopping.MultiTypesProductsActivity;
import com.saman.groceryshopping.R;
import com.saman.groceryshopping.databinding.ActivitySubCategoriesBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubCategoriesActivity extends AppCompatActivity {
//    LoadingDialog dialog;
  /*  List<ProductsModel> list;*/
    List<CategoryModel> listCategory;
    ActivitySubCategoriesBinding binding;
    Context context = SubCategoriesActivity.this;
/*    private LinearLayoutManager linearLayoutManagerFeature, linearLayoutManager;*/
    String cate_id="";
    UserSessionManager userSessionManager;
    static View badge;
    Session session;
    static BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubCategoriesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomNav = binding.bottomNavigation;

        badge = LayoutInflater.from(SubCategoriesActivity.this).inflate(R.layout.notification_badge
                , binding.bottomNavigation
                , false);

        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        startActivity(new Intent(SubCategoriesActivity.this, MainActivity.class)
                                .putExtra("home","home"));
                        break;
                    case R.id.nav_favorites:
                        startActivity(new Intent(SubCategoriesActivity.this, MainActivity.class)
                                .putExtra("favorite","favorite"));
                        break;
                    case R.id.nav_images:
                        startActivity(new Intent(SubCategoriesActivity.this, MainActivity.class)
                                .putExtra("gallery", "gallery"));
                        break;
                    case R.id.nav_cart:
                        startActivity(new Intent(SubCategoriesActivity.this, MainActivity.class)
                                .putExtra("cart", "cart"));
                        break;
                    case R.id.nav_profile:
                        startActivity(new Intent(SubCategoriesActivity.this, MainActivity.class)
                                .putExtra("profile", "profile"));
                        break;
                }
                return true;
            }
        });

        cate_id=getIntent().getStringExtra("cate_id");
        binding.backwardCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initViews();
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
    }

    @Override
    protected void onResume() {
        super.onResume();
//        dialog.dismiss();

        initViews();
        getData();
    }

    public void ShowBadge(int item) {
        if (item == 0) {
            SubCategoriesActivity.removeBadge(SubCategoriesActivity.bottomNav, R.id.nav_cart);
        } else {
            SubCategoriesActivity.showBadge(SubCategoriesActivity.this,SubCategoriesActivity.bottomNav,R.id.nav_cart,String.valueOf(item));
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

    void GoToMultiTypeActivity(String request_type) {
        Intent intent = new Intent(getApplicationContext(), MultiTypesProductsActivity.class);
        intent.putExtra("request_type", request_type);
        startActivity(intent);

    }
    private void getData() {
      /*  list=new ArrayList<>();*/
        listCategory=new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.GET_SUB_CATEGORIES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("homeres", response);
                try {
                    JSONObject objectCategories, objectSales;
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
//                        dialog.dismiss();
                        JSONArray arrayCategories = jsonObject.getJSONArray("sub_subcategories");
                        for (int i = 0; i < arrayCategories.length(); i++) {
                            objectCategories = arrayCategories.getJSONObject(i);
                            listCategory.add(new CategoryModel(objectCategories.getString("id"), objectCategories.getString("name")));
                        }

                        SectionsPagerAdapter adapter=new SectionsPagerAdapter(context,getSupportFragmentManager(),listCategory);
                        binding.viewPager.setAdapter(adapter);

                        binding.tabs.setupWithViewPager(binding.viewPager);

                        /*JSONArray arrayOnsale = jsonObject.getJSONArray("products");
                        for (int j = 0; j < arrayOnsale.length(); j++) {
                            objectSales = arrayOnsale.getJSONObject(j);
                            list.add(new ProductsModel(objectSales.getInt("product_id"), objectSales.getString("product_name"), objectSales.getString("unit_price"), URLHelper.BASE_URL_IMAGE + objectSales.getString("product_image"),"home",objectSales.getString("unit"),"","",objectSales.getBoolean("isveriation"),"","",objectSales.getBoolean("like")));
                        }*/
                     /*   SubCategoriesAdapter adapter=new SubCategoriesAdapter(context,listCategory);
                        binding.categoryRecycler.setAdapter(adapter);
                        HomeVerticalAdapter adapter1=new HomeVerticalAdapter(list,context);
                        binding.categoryProduct.setAdapter(adapter1);*/

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
//                dialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("subcategory_id",cate_id);
                map.put("user_id",session.getId());
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
//        dialog = new LoadingDialog(context);
  /*      linearLayoutManagerFeature = new LinearLayoutManager(context);
        binding.categoryProduct.setLayoutManager(linearLayoutManagerFeature);
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        binding.categoryRecycler.setLayoutManager(linearLayoutManager);*/
        userSessionManager=new UserSessionManager(context);
        session=userSessionManager.getSessionDetails();
//        dialog.show();

    }
}
