package com.saman.groceryshopping.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

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
import com.squareup.picasso.Picasso;
import com.saman.groceryshopping.Adapters.GalleryAdapter;
import com.saman.groceryshopping.HelperClasses.GridSpacingItemDecoration;
import com.saman.groceryshopping.HelperClasses.Session;
import com.saman.groceryshopping.HelperClasses.ShareHelper;
import com.saman.groceryshopping.HelperClasses.URLHelper;
import com.saman.groceryshopping.HelperClasses.UserSessionManager;
import com.saman.groceryshopping.Models.ProductsModel;
import com.saman.groceryshopping.MultiTypesProductsActivity;
import com.saman.groceryshopping.ProductDetailActivity;
import com.saman.groceryshopping.ProductDetailsVeriationActivity;
import com.saman.groceryshopping.Utils.LoadingDialog;
import com.saman.groceryshopping.databinding.FragmentGalleryBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment {
    FragmentGalleryBinding binding;
    Context context;
    LoadingDialog dialog;
    List<ProductsModel> list;
    String productid, productname, productprice, productimage, productquantity, productdiscount, productactual;
    boolean isLiked, isVariation;
    String productid2, productname2, productprice2, productimage2, productquantity2, productdiscount2, productactual2;
    boolean isLiked2, isVariation2;
    String productid3, productname3, productprice3, productimage3, productquantity3, productdiscount3, productactual3;
    boolean isLiked3, isVariation3;
    ShareHelper shareHelper;
    UserSessionManager userSessionManager;
    Session session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.context = view.getContext();
        dialog = new LoadingDialog(context);
        shareHelper=new ShareHelper(context);
        userSessionManager=new UserSessionManager(context);
        session=userSessionManager.getSessionDetails();
        binding.galleryRecycler.setLayoutManager(new GridLayoutManager(getContext(), 3));
        binding.galleryRecycler.addItemDecoration(new GridSpacingItemDecoration(5));
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

        binding.galleryImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVariation) {
                    Intent intent = new Intent(context.getApplicationContext(), ProductDetailsVeriationActivity.class);
                    intent.putExtra("product_id", productid);
                    shareHelper.putKey("productdiscount", productdiscount);
                    shareHelper.putKey("productactual", productactual);
                    intent.putExtra("liked", isLiked);
                    context.startActivity(intent);
                } else {

                    shareHelper.putKey("productid", productid);
                    shareHelper.putKey("productname", productname);
                    shareHelper.putKey("productprice", productprice);
                    shareHelper.putKey("productimage", URLHelper.BASE_URL_IMAGE+productimage);
                    shareHelper.putKey("productquantity", productquantity);
                    shareHelper.putKey("productdiscount", productdiscount);
                    shareHelper.putKey("productactual", productactual);

                    Intent intent = new Intent(context.getApplicationContext(), ProductDetailActivity.class);
                    intent.putExtra("liked", isLiked);
                    context.startActivity(intent);
                }
            }
        });
        binding.galleryImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVariation) {
                    Intent intent = new Intent(context.getApplicationContext(), ProductDetailsVeriationActivity.class);
                    intent.putExtra("product_id", productid2);
                    shareHelper.putKey("productdiscount", productdiscount2);
                    shareHelper.putKey("productactual", productactual2);
                    intent.putExtra("liked", isLiked2);
                    context.startActivity(intent);
                } else {

                    shareHelper.putKey("productid", productid2);
                    shareHelper.putKey("productname", productname2);
                    shareHelper.putKey("productprice", productprice2);
                    shareHelper.putKey("productimage", URLHelper.BASE_URL_IMAGE+productimage2);
                    shareHelper.putKey("productquantity", productquantity2);
                    shareHelper.putKey("productdiscount", productdiscount2);
                    shareHelper.putKey("productactual", productactual2);

                    Intent intent = new Intent(context.getApplicationContext(), ProductDetailActivity.class);
                    intent.putExtra("liked", isLiked2);
                    context.startActivity(intent);
                }
            }
        });
        binding.galleryImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVariation) {
                    Intent intent = new Intent(context.getApplicationContext(), ProductDetailsVeriationActivity.class);
                    intent.putExtra("product_id", productid3);
                    shareHelper.putKey("productdiscount", productdiscount3);
                    shareHelper.putKey("productactual", productactual3);
                    intent.putExtra("liked", isLiked3);
                    context.startActivity(intent);
                } else {

                    shareHelper.putKey("productid", productid3);
                    shareHelper.putKey("productname", productname3);
                    shareHelper.putKey("productprice", productprice3);
                    shareHelper.putKey("productimage", URLHelper.BASE_URL_IMAGE+productimage3);
                    shareHelper.putKey("productquantity", productquantity3);
                    shareHelper.putKey("productdiscount", productdiscount3);
                    shareHelper.putKey("productactual", productactual3);

                    Intent intent = new Intent(context.getApplicationContext(), ProductDetailActivity.class);
                    intent.putExtra("liked", isLiked3);
                    context.startActivity(intent);
                }
            }
        });
        getData();
    }

    private void getData() {
        list = new ArrayList<>();
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.GET_GALLERY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("homeres", response);
                try {
                    JSONObject objectFeatures, productOne, productTwo, productThree;
                    JSONObject jsonObject = new JSONObject(response);


                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        productOne = jsonObject.getJSONObject("product1");
                        productid = productOne.getString("product_id");
                        productname = productOne.getString("product_name");
                        productprice = productOne.getString("unit_price");
                        productimage = productOne.getString("product_image");
                        productquantity = productOne.getString("unit");
                        productdiscount = "";
                        productactual = "";
                        isLiked = productOne.getBoolean("like");
                        isVariation = productOne.getBoolean("isveriation");
                        Picasso.get().load(URLHelper.BASE_URL_IMAGE + productimage).into(binding.galleryImage1);

                        productTwo = jsonObject.getJSONObject("product2");
                        productid2 = productTwo.getString("product_id");
                        productname2 = productTwo.getString("product_name");
                        productprice2 = productTwo.getString("unit_price");
                        productimage2 = productTwo.getString("product_image");
                        productquantity2 = productTwo.getString("unit");
                        productdiscount2 = "";
                        productactual2 = "";
                        isLiked2 = productTwo.getBoolean("like");
                        isVariation2 = productTwo.getBoolean("isveriation");
                        Picasso.get().load(URLHelper.BASE_URL_IMAGE + productimage2).into(binding.galleryImage2);


                        productThree = jsonObject.getJSONObject("product3");
                        productid3 = productThree.getString("product_id");
                        productname3 = productThree.getString("product_name");
                        productprice3 = productThree.getString("unit_price");
                        productimage3 = productThree.getString("product_image");
                        productquantity3 = productThree.getString("unit");
                        productdiscount3 = "";
                        productactual3 = "";
                        isLiked3 = productThree.getBoolean("like");
                        isVariation3 = productThree.getBoolean("isveriation");
                        Picasso.get().load(URLHelper.BASE_URL_IMAGE + productimage3).into(binding.galleryImage3);


                        JSONArray jsonArray = jsonObject.getJSONArray("products");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            objectFeatures = jsonArray.getJSONObject(i);
                            list.add(new ProductsModel(objectFeatures.getInt("product_id"), objectFeatures.getString("product_name"), objectFeatures.getString("unit_price"), URLHelper.BASE_URL_IMAGE + objectFeatures.getString("product_image"), "home", objectFeatures.getString("unit"), "", "", objectFeatures.getBoolean("isveriation"), "", "", objectFeatures.getBoolean("like")));
                        }
                        binding.galleryRecycler.setAdapter(new GalleryAdapter(list, context));
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
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("user_id",session.getId());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.getCache().clear();
        queue.add(stringRequest);
    }

    void GoToMultiTypeActivity(String request_type) {
        Intent intent = new Intent(context, MultiTypesProductsActivity.class);
        intent.putExtra("request_type", request_type);
        startActivity(intent);
    }
}
