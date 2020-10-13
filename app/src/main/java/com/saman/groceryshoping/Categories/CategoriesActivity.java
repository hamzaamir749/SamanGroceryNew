package com.saman.groceryshopping.Categories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.saman.groceryshopping.HelperClasses.Session;
import com.saman.groceryshopping.HelperClasses.UserSessionManager;
import com.saman.groceryshopping.Models.CategoryModel;
import com.saman.groceryshopping.Models.ProductsModel;
import com.saman.groceryshopping.MultiTypesProductsActivity;
import com.saman.groceryshopping.Utils.LoadingDialog;
import com.saman.groceryshopping.databinding.ActivityCategoriesBinding;

import java.util.List;

public class CategoriesActivity extends AppCompatActivity {
    LoadingDialog dialog;
    List<ProductsModel> list;
    List<CategoryModel> listCategory;
    ActivityCategoriesBinding binding;
    Context context = CategoriesActivity.this;
    private LinearLayoutManager linearLayoutManagerFeature, linearLayoutManager;
    String cate_id="";
    UserSessionManager userSessionManager;
    Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoriesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cate_id=getIntent().getStringExtra("cate_id");

        binding.backwardCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initViews();
       // getData();
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
    void GoToMultiTypeActivity(String request_type) {
        Intent intent = new Intent(getApplicationContext(), MultiTypesProductsActivity.class);
        intent.putExtra("request_type", request_type);
        startActivity(intent);

    }
  /*  private void getData() {
        list=new ArrayList<>();
        listCategory=new ArrayList<>();
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.GET_CATEGORIES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("homeres", response);
                try {
                    JSONObject objectCategories, objectSales, objectFeatures, objectImages;
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        JSONArray arrayCategories = jsonObject.getJSONArray("sub_categories");
                        for (int i = 0; i < arrayCategories.length(); i++) {
                            objectCategories = arrayCategories.getJSONObject(i);
                            listCategory.add(new CategoryModel(objectCategories.getString("id"), objectCategories.getString("name")));
                        }
                        JSONArray arrayOnsale = jsonObject.getJSONArray("products");
                        for (int j = 0; j < arrayOnsale.length(); j++) {
                            objectSales = arrayOnsale.getJSONObject(j);
                            list.add(new ProductsModel(objectSales.getInt("product_id"), objectSales.getString("product_name"), objectSales.getString("unit_price"), URLHelper.BASE_URL_IMAGE + objectSales.getString("product_image"),"home",objectSales.getString("unit"),"","",objectSales.getBoolean("isveriation"),"","",objectSales.getBoolean("like")));
                        }
                        CategoryAdapter adapter=new CategoryAdapter(context,listCategory);
                        binding.categoryRecycler.setAdapter(adapter);
                        HomeVerticalAdapter adapter1=new HomeVerticalAdapter(list,context);
                        binding.categoryProduct.setAdapter(adapter1);

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
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("category_id",cate_id);
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
    }*/

    private void initViews() {
        dialog = new LoadingDialog(context);
        binding.categoryProduct.setLayoutManager(new LinearLayoutManager(context));
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        binding.categoryRecycler.setLayoutManager(linearLayoutManager);
        userSessionManager=new UserSessionManager(context);
        session=userSessionManager.getSessionDetails();
    }
}
