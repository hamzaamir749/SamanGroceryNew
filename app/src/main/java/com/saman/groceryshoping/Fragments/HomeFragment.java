package com.saman.groceryshopping.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.example.moeidbannerlibrary.banner.BannerLayout;
import com.example.moeidbannerlibrary.banner.BaseBannerAdapter;
import com.saman.groceryshopping.Adapters.CustomPagerAdapter;
import com.saman.groceryshopping.Adapters.HomeAdapter;
import com.saman.groceryshopping.Adapters.HomeCategoryAdapter;
import com.saman.groceryshopping.HelperClasses.Session;
import com.saman.groceryshopping.HelperClasses.URLHelper;
import com.saman.groceryshopping.HelperClasses.UserSessionManager;
import com.saman.groceryshopping.MainActivity;
import com.saman.groceryshopping.Models.CategoriesModel;
import com.saman.groceryshopping.Models.ProductsModel;
import com.saman.groceryshopping.MultiTypesProductsActivity;
import com.saman.groceryshopping.R;
import com.saman.groceryshopping.Utils.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;


public class HomeFragment extends Fragment implements MainActivity.resumeIt{
    LinearLayout optionsLayout, dotsLayout;
    RelativeLayout sliderLayout;
    ViewPager sliderView;
    int currentSlidePosition = 0;
    CustomPagerAdapter pagerAdapter;
    Handler handler;
    Runnable runnable;
    Timer timer;
    Activity myActivity;
    TextView[] tvDots;
    View view;
    Context context;
    LoadingDialog dialog;
    List<CategoriesModel> listCategories;
    List<List<CategoriesModel>> childCategories;
    List<ProductsModel> listOnSale;
    List<ProductsModel> listFeature;
    RecyclerView recyclerViewSale, recyclerViewFeature, recyclerViewCategories;
    LinearLayoutManager linearLayoutManagerSale, linearLayoutManagerFeature, linearLayoutManager;
    EditText searchText;
    ImageView btnSearch;
    Button btnViewSales, btnViewFeatured;
    UserSessionManager userSessionManager;
    Session session;
    List<CategoriesModel> list;
    TextView homeNote;
    boolean refresh=false;

    BannerLayout banner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        this.context = view.getContext();
        MainActivity.resumeIt=this;
        initViews();
        getHomeData();

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (searchText.getText().toString().isEmpty()) {
                        searchText.setError("Please Enter here");
                    } else {
                        GoToMultiTypeActivity(searchText.getText().toString());
                    }
                    return true;
                }
                return false;
            }
        });
        btnViewFeatured.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToMultiTypeActivity("featured");
            }
        });
        btnViewSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToMultiTypeActivity("onsale");
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchText.getText().toString().isEmpty()) {
                    searchText.setError("Please Enter here");
                } else {
                    GoToMultiTypeActivity(searchText.getText().toString());
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        dialog.dismiss();

        initViews();
        getHomeData();
    }

    void GoToMultiTypeActivity(String request_type) {
        Intent intent = new Intent(context.getApplicationContext(), MultiTypesProductsActivity.class);
        intent.putExtra("request_type", request_type);
        context.startActivity(intent);
    }

    public void getHomeData() {
        listCategories = new ArrayList<>();
        listOnSale = new ArrayList<>();
        listFeature = new ArrayList<>();
        childCategories = new ArrayList<>();

        UserSessionManager.sliderImages = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.GET_HOME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("homeres", response);
                try {
                    JSONObject objectCategories, objectSubCategories, objectSales, objectFeatures, objectImages;
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");

                    if (status) {
                        dialog.dismiss();
                        homeNote.setText(jsonObject.getString("message"));
                        JSONArray arrayCategories = jsonObject.getJSONArray("categories");
                        for (int i = 0; i < arrayCategories.length(); i++) {
                            objectCategories = arrayCategories.getJSONObject(i);
                            listCategories.add(new CategoriesModel(objectCategories.getString("id"), objectCategories.getString("name"), "All Categories", URLHelper.BASE_URL_IMAGE + objectCategories.getString("icon")));
                            JSONArray arraychilCategories = objectCategories.getJSONArray("subcategories");
                            list = new ArrayList<>();
                            for (int ii = 0; ii < arraychilCategories.length(); ii++) {
                                objectSubCategories = arraychilCategories.getJSONObject(ii);
                                list.add(new CategoriesModel(objectSubCategories.getString("id"), objectSubCategories.getString("name"), "All Categories", URLHelper.BASE_URL_IMAGE + objectSubCategories.getString("image")));
                            }
                            childCategories.add(list);
                        }
                        JSONArray arrayOnsale = jsonObject.getJSONArray("onsale");
                        for (int j = 0; j < arrayOnsale.length(); j++) {
                            objectSales = arrayOnsale.getJSONObject(j);
                            listOnSale.add(new ProductsModel(objectSales.getInt("product_id"), objectSales.getString("product_name"), objectSales.getString("unit_price"), URLHelper.BASE_URL_IMAGE + objectSales.getString("product_image"), "home", objectSales.getString("unit"), "", "", objectSales.getBoolean("isveriation"), objectSales.getString("originalprice"), objectSales.getString("discount"), objectSales.getBoolean("like")));
                        }

                        JSONArray arrayFeature = jsonObject.getJSONArray("featured");
                        for (int k = 0; k < arrayFeature.length(); k++) {
                            objectFeatures = arrayFeature.getJSONObject(k);
                            listFeature.add(new ProductsModel(objectFeatures.getInt("product_id"), objectFeatures.getString("product_name"), objectFeatures.getString("unit_price"), URLHelper.BASE_URL_IMAGE + objectFeatures.getString("product_image"), "home", objectFeatures.getString("unit"), "", "", objectFeatures.getBoolean("isveriation"), "", "", objectFeatures.getBoolean("like")));
                        }
                        if (UserSessionManager.sliderImages.size() == 0) {
                            JSONArray arrayImages = jsonObject.getJSONArray("banners");
                            for (int i = 0; i < arrayImages.length(); i++) {
                                objectImages = arrayImages.getJSONObject(i);
                                UserSessionManager.sliderImages.add(URLHelper.BASE_URL_IMAGE + objectImages.getString("photo"));
                            }
//                            createSlideShow();
                            showBanner();
                        }
                        else {
//                            createSlideShow();
                            showBanner();
                        }


                        HomeCategoryAdapter adapterCategory = new HomeCategoryAdapter(listCategories, childCategories, context);
                        recyclerViewCategories.setAdapter(adapterCategory);

                        HomeAdapter adapterSale = new HomeAdapter(listOnSale, context);
                        recyclerViewSale.setAdapter(adapterSale);
                        HomeAdapter adapterFeature = new HomeAdapter(listFeature, context);
                        recyclerViewFeature.setAdapter(adapterFeature);



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

        userSessionManager = new UserSessionManager(context);
        session = userSessionManager.getSessionDetails();
        searchText = view.findViewById(R.id.home_searchText);
        btnSearch = view.findViewById(R.id.home_searchBtn);
        homeNote = view.findViewById(R.id.home_note);
        btnViewSales = view.findViewById(R.id.home_btnOnsale);
        btnViewFeatured = view.findViewById(R.id.home_btnFeatured);
        sliderView = view.findViewById(R.id.sliderView);
        dotsLayout = view.findViewById(R.id.sliderDotsLayout);
        dialog = new LoadingDialog(getContext());
        UserSessionManager.sliderImages = new ArrayList<>();
        recyclerViewSale = view.findViewById(R.id.home_recyclerSale);
        linearLayoutManagerSale = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSale.setLayoutManager(linearLayoutManagerSale);
        recyclerViewFeature = view.findViewById(R.id.home_recyclerFeature);
        linearLayoutManagerFeature = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewFeature.setLayoutManager(linearLayoutManagerFeature);
        recyclerViewCategories = view.findViewById(R.id.home_recyclerCategories);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerViewCategories.setLayoutManager(linearLayoutManager);

        banner=(BannerLayout) view.findViewById(R.id.Banner);

        dialog.show();
    }


//    private void createSlideShow() {
//        handler = new Handler();
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                if (currentSlidePosition == UserSessionManager.sliderImages.size()) {
//                    currentSlidePosition = 0;
//                }
//                sliderView.setCurrentItem(currentSlidePosition++, true);
//            }
//        };
//        pagerAdapter = new CustomPagerAdapter(context);
//        sliderView.setAdapter(pagerAdapter);
//        sliderView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i1) {
//            }
//
//            @Override
//            public void onPageSelected(int currentPosition) {
//                addBottomDots(currentPosition);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//            }
//        });
//        //sliderView.setOffscreenPageLimit(8);
//        currentSlidePosition = 0;
//        addBottomDots(currentSlidePosition);
//        if (timer != null) {
//            timer.cancel();
//            timer = null;
//        }
//        timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(runnable);
//            }
//        }, 5000, 7000);
//    }
//
//
//
//    private void addBottomDots(int currentPosition) {
//        tvDots = new TextView[UserSessionManager.sliderImages.size()];
//        dotsLayout.removeAllViews();
//
//        for (int i = 0; i < tvDots.length; i++) {
//            tvDots[i] = new TextView(context);
//            tvDots[i].setText(Html.fromHtml("&#8226;"));
//            tvDots[i].setTextSize(35);
//            tvDots[i].setTextColor(Color.parseColor("#9f9f9f"));
//            dotsLayout.addView(tvDots[i]);
//        }
//
//        if (tvDots.length > 0) {
//            tvDots[currentPosition].setTextColor(Color.parseColor("#2f383a"));
//        }
//    }

    @Override
    public void resumeFragment() {
        Toast.makeText(context, "Resume Done", Toast.LENGTH_SHORT).show();
    }

    private void showBanner(){
        if (UserSessionManager.sliderImages != null){
            BaseBannerAdapter webBannerAdapter=new BaseBannerAdapter(getContext(),UserSessionManager.sliderImages);
            webBannerAdapter.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
                @Override
                public void onItemClick(int position)
                {


                }
            });
            banner.clearFocus();
            banner.setAdapter(webBannerAdapter);
        }
    }
}
