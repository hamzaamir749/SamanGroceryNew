package com.saman.groceryshopping.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.saman.groceryshopping.Models.PageViewModel;
import com.saman.groceryshopping.Models.ProductsModel;
import com.saman.groceryshopping.R;
import com.saman.groceryshopping.databinding.ActivitySubCategoriesBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubCategoriesFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private PageViewModel pageViewModel;
//    LoadingDialog dialog;
    List<ProductsModel> list;
    private LinearLayoutManager linearLayoutManager;
    UserSessionManager userSessionManager;
    Session session;
    Context context;
    View view;
    RecyclerView recyclerView;

    public static SubCategoriesFragment newInstance(int id) {
        SubCategoriesFragment fragment = new SubCategoriesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sub_categories, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        this.context = view.getContext();
        recyclerView = view.findViewById(R.id.category_product);
        initViews();
        pageViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                getData(s);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        initViews();
        pageViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                getData(s);
            }
        });
    }

    private void getData(final String categiryId) {
        list = new ArrayList<>();
//        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.GET_SUB_SUB_CATEGORIES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                dialog.dismiss();
                Log.d("homeres", response);
                try {
                    JSONObject objectSales;
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {

                        JSONArray arrayOnsale = jsonObject.getJSONArray("products");
                        for (int j = 0; j < arrayOnsale.length(); j++) {
                            objectSales = arrayOnsale.getJSONObject(j);
                            list.add(new ProductsModel(objectSales.getInt("product_id"), objectSales.getString("product_name"), objectSales.getString("unit_price"), URLHelper.BASE_URL_IMAGE + objectSales.getString("product_image"), "home", objectSales.getString("unit"), "", "", objectSales.getBoolean("isveriation"), "", "", objectSales.getBoolean("like")));
                        }

                        HomeVerticalAdapter adapter1 = new HomeVerticalAdapter(list, context);
                        recyclerView.setAdapter(adapter1);

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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("subsubcategory_id", categiryId);
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
//        dialog = new LoadingDialog(context);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        userSessionManager = new UserSessionManager(context);
        session = userSessionManager.getSessionDetails();

    }
}
