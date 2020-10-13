package com.saman.groceryshopping.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.saman.groceryshopping.Adapters.HomeVerticalAdapter;
import com.saman.groceryshopping.HelperClasses.Session;
import com.saman.groceryshopping.HelperClasses.URLHelper;
import com.saman.groceryshopping.HelperClasses.UserSessionManager;
import com.saman.groceryshopping.MainActivity;
import com.saman.groceryshopping.Models.ProductsModel;
import com.saman.groceryshopping.MultiTypesProductsActivity;
import com.saman.groceryshopping.Utils.LoadingDialog;
import com.saman.groceryshopping.databinding.FragmentFavBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FavFragment extends Fragment {

    LoadingDialog dialog;
    List<ProductsModel> list;
    FragmentFavBinding binding;
    Context context;
    UserSessionManager userSessionManager;
    Session session;
    private LinearLayoutManager linearLayoutManagerFeature;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.context = view.getContext();
        userSessionManager = new UserSessionManager(context);
        session = userSessionManager.getSessionDetails();
        dialog = new LoadingDialog(context);
        linearLayoutManagerFeature = new LinearLayoutManager(context);
        binding.fevRecycler.setLayoutManager(linearLayoutManagerFeature);
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
    public void onResume() {
        super.onResume();

        dialog.dismiss();
        getData();
    }

    void GoToMultiTypeActivity(String request_type) {
        Intent intent = new Intent(context, MultiTypesProductsActivity.class);
        intent.putExtra("request_type", request_type);
        startActivity(intent);
    }

    private void getData() {
        list = new ArrayList<>();
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.GET_FEV, new Response.Listener<String>() {
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
                            list.add(new ProductsModel(objectProducts.getInt("product_id"), objectProducts.getString("product_name"), objectProducts.getString("unit_price"), URLHelper.BASE_URL_IMAGE + objectProducts.getString("product_image"), "fev", objectProducts.getString("unit"), "", "", objectProducts.getBoolean("isveriation"), "", "", true));
                        }
                        if (list.isEmpty()) {
                            binding.fevMessageImage.setVisibility(View.VISIBLE);
                            binding.btnStartExplore.setVisibility(View.VISIBLE);

                            binding.btnStartExplore.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(context, MainActivity.class));
                                }
                            });
                            return;
                        }
                        HomeVerticalAdapter adapter = new HomeVerticalAdapter(list, context);
                        binding.fevRecycler.setAdapter(adapter);
                    } else {
                        Toast.makeText(context, "Status False", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    //Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
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
                map.put("user_id", session.getId());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.getCache().clear();
        queue.add(stringRequest);
    }
}
