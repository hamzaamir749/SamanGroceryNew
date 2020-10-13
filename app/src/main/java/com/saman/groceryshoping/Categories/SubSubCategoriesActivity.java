package com.saman.groceryshopping.Categories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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
import com.saman.groceryshopping.Adapters.HomeVerticalAdapter;
import com.saman.groceryshopping.HelperClasses.Session;
import com.saman.groceryshopping.HelperClasses.URLHelper;
import com.saman.groceryshopping.HelperClasses.UserSessionManager;
import com.saman.groceryshopping.Models.ProductsModel;
import com.saman.groceryshopping.MultiTypesProductsActivity;
import com.saman.groceryshopping.databinding.ActivitySubSubCategoriesBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubSubCategoriesActivity extends AppCompatActivity {
//    LoadingDialog dialog;
    List<ProductsModel> list;
    Context context = SubSubCategoriesActivity.this;
    private LinearLayoutManager linearLayoutManagerFeature, linearLayoutManager;
    String cate_id="";
    ActivitySubSubCategoriesBinding binding;
    UserSessionManager userSessionManager;
    Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubSubCategoriesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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

        initViews();
        getData();
    }

    void GoToMultiTypeActivity(String request_type) {
        Intent intent = new Intent(getApplicationContext(), MultiTypesProductsActivity.class);
        intent.putExtra("request_type", request_type);
        startActivity(intent);
    }
    private void getData() {
        list=new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.GET_SUB_SUB_CATEGORIES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("homeres", response);
                try {
                    JSONObject objectSales;
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
//                        dialog.dismiss();
                        JSONArray arrayOnsale = jsonObject.getJSONArray("products");
                        for (int j = 0; j < arrayOnsale.length(); j++) {
                            objectSales = arrayOnsale.getJSONObject(j);
                            list.add(new ProductsModel(objectSales.getInt("product_id"), objectSales.getString("product_name"), objectSales.getString("unit_price"), URLHelper.BASE_URL_IMAGE + objectSales.getString("product_image"),"home",objectSales.getString("unit"),"","",objectSales.getBoolean("isveriation"),"","",objectSales.getBoolean("isveriation")));
                        }
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
//                dialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("subsubcategory_id",cate_id);
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
        binding.categoryProduct.setLayoutManager(new LinearLayoutManager(context));
        userSessionManager=new UserSessionManager(context);
        session=userSessionManager.getSessionDetails();
//        dialog.show();
    }
}
