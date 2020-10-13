package com.saman.groceryshopping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.saman.groceryshopping.Adapters.PurchaseAdapter;
import com.saman.groceryshopping.HelperClasses.DatePickerFragment;
import com.saman.groceryshopping.HelperClasses.Session;
import com.saman.groceryshopping.HelperClasses.URLHelper;
import com.saman.groceryshopping.HelperClasses.UserSessionManager;
import com.saman.groceryshopping.Models.PurchaseModel;
import com.saman.groceryshopping.Utils.LoadingDialog;
import com.saman.groceryshopping.databinding.ActivityPurcahseHistoryBinding;
import com.saman.groceryshopping.databinding.ActivityPurchaseHistoryBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Purchase_HistoryActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    List<PurchaseModel> list;
    LoadingDialog dialog;
    UserSessionManager userSessionManager;
    Session session;
    Context context = Purchase_HistoryActivity.this;
    ActivityPurchaseHistoryBinding binding;
    private String Date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPurchaseHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();

        binding.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickDate();
            }
        });
        binding.backwardPurchaseHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initViews() {
        dialog=new LoadingDialog(context);
        userSessionManager = new UserSessionManager(context);
        session=userSessionManager.getSessionDetails();
        binding.purchaseRecycler.setLayoutManager(new LinearLayoutManager(context));
        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
        Date = currentDate.format(calFordDate.getTime());
        binding.date.setText(Date);
        getHistory();

    }
    private void PickDate() {
        DialogFragment dialogFragment = new DatePickerFragment();
        dialogFragment.show(getSupportFragmentManager(), "Pick Date");
    }

    private void getHistory() {
        list = new ArrayList<>();
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.GET_HISTORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("homeres", response);
                try {
                    JSONObject objectProducts;
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        JSONArray arrayProducts = jsonObject.getJSONArray("orders");
                        for (int k = 0; k < arrayProducts.length(); k++) {
                            objectProducts = arrayProducts.getJSONObject(k);
                            list.add(new PurchaseModel(objectProducts.getString("products"),objectProducts.getString("status"),objectProducts.getJSONObject("order").getString("id"),objectProducts.getJSONObject("order").getString("grand_total"),objectProducts.getJSONObject("order").getString("created_at")));
                        }
                        PurchaseAdapter adapter = new PurchaseAdapter(list, context);
                        binding.purchaseRecycler.setAdapter(adapter);
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
                map.put("date",Date);
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.getCache().clear();
        queue.add(stringRequest);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        String monthstr = String.valueOf(month + 1);
        String day=String.valueOf(dayOfMonth);
        if (monthstr.length()==1)
        {
            monthstr="0"+monthstr;
        }
        if (day.length()==1)
        {
            day="0"+dayOfMonth;
        }

        Date=String.valueOf(year)+"-"+monthstr+"-"+day;

        getHistory();
        binding.date.setText(Date);

    }

}
