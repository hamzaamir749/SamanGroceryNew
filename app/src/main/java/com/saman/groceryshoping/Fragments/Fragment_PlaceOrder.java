package com.saman.groceryshopping.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.saman.groceryshopping.HelperClasses.Session;
import com.saman.groceryshopping.HelperClasses.URLHelper;
import com.saman.groceryshopping.HelperClasses.UserSessionManager;
import com.saman.groceryshopping.MainActivity;
import com.saman.groceryshopping.Models.AddressClass;
import com.saman.groceryshopping.R;
import com.saman.groceryshopping.Utils.LoadingDialog;
import com.saman.groceryshopping.databinding.FragmentPlaceOrderBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class Fragment_PlaceOrder extends Fragment {

    FragmentPlaceOrderBinding binding;
    Context context;
    String priceTotal = "";
    List<AddressClass> list;
    LoadingDialog dialog;
    UserSessionManager userSessionManager;
    Session session;
    LinearLayout linearLayoutOne, linearLayoutTwo;
    TextView total_price;
    Button done;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlaceOrderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.context = view.getContext();
        initViews();
        total_price = view.findViewById(R.id.complete_total);
        done = view.findViewById(R.id.complete_done);
        linearLayoutOne = view.findViewById(R.id.po_linearOne);
        linearLayoutTwo = view.findViewById(R.id.po_linearTwo);
        priceTotal = String.valueOf(UserSessionManager.cart.getTotalPrice());
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context.getApplicationContext(), MainActivity.class));
                ((Activity) context).finishAffinity();
            }
        });
        view.findViewById(R.id.checkout_btnCheck).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.checkoutName.getText().toString().isEmpty() && binding.checkoutAddress.getText().toString().isEmpty() && binding.checkoutCity.getText().toString().isEmpty() && binding.checkoutPhone.getText().toString().isEmpty()) {
                    binding.checkoutName.setError("Please Enter");
                    binding.checkoutAddress.setError("Please Enter");
                    binding.checkoutCity.setError("Please Enter");
                    binding.checkoutPhone.setError("Please Enter");
                } else if (binding.checkoutName.getText().toString().isEmpty()) {
                    binding.checkoutName.setError("Please Enter");
                } else if (binding.checkoutAddress.getText().toString().isEmpty()) {
                    binding.checkoutAddress.setError("Please Enter");
                } else if (binding.checkoutCity.getText().toString().isEmpty()) {
                    binding.checkoutCity.setError("Please Enter");
                } else if (binding.checkoutPhone.getText().toString().isEmpty()) {
                    binding.checkoutPhone.setError("Please Enter");
                } else {
                    list = new ArrayList<>();
                    list.add(new AddressClass(binding.checkoutName.getText().toString(), "", binding.checkoutAddress.getText().toString(), "Pakistan", binding.checkoutCity.getText().toString(), "12345", binding.checkoutPhone.getText().toString(), "logged"));

                    SaveInfoToDb();
                }
            }
        });

    }

    private void initViews() {
        dialog = new LoadingDialog(context);
        userSessionManager = new UserSessionManager(context);
        session = userSessionManager.getSessionDetails();
        binding.checkoutPhone.setText(session.getPhone());
        binding.checkoutName.setText(session.getName());
        binding.checkoutAddress.setText(session.getPaddress());
    }

    private void SaveInfoToDb() {
        Gson gson = new Gson();

        final String address = gson.toJson(new AddressClass(binding.checkoutName.getText().toString(), "", binding.checkoutAddress.getText().toString(), "Pakistan", binding.checkoutCity.getText().toString(), "12345", binding.checkoutPhone.getText().toString(), "logged"));
        final String products = gson.toJson(UserSessionManager.cart.productList);
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.CHECKOUT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("checkres",response);
                dialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        linearLayoutOne.setVisibility(View.GONE);
                        linearLayoutTwo.setVisibility(View.VISIBLE);
                        total_price.setText("RS " + String.valueOf(UserSessionManager.cart.getTotalPrice()));
                        UserSessionManager.cart.productList.clear();
                        new CartFragment().stepView.go(new CartFragment().stepView.getCurrentStep() + 1, true);
                    } else {
                        final PrettyDialog pDialog = new PrettyDialog(context);
                        pDialog
                                .setTitle("Status").setIcon(R.drawable.ic_error_outline_black_24dp)
                                .setMessage(jsonObject.getString("message"))
                                .addButton(
                                        "Ok",
                                        R.color.pdlg_color_white,
                                        R.color.colorPrimaryDark,
                                        new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                pDialog.dismiss();

                                            }
                                        }
                                )
                                .show();

                    }
                } catch (JSONException e) {
                    Log.d("loginexception", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                SaveInfoToDb();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_id", session.getId());
                map.put("total", priceTotal);
                map.put("products", products);
                map.put("address", address);
                Log.d("checkoutdata", map.toString());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.getCache().clear();
        queue.add(request);
    }
}

