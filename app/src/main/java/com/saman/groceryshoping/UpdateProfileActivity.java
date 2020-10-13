package com.saman.groceryshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.saman.groceryshopping.HelperClasses.Session;
import com.saman.groceryshopping.HelperClasses.URLHelper;
import com.saman.groceryshopping.HelperClasses.UserSessionManager;
import com.saman.groceryshopping.Utils.LoadingDialog;
import com.saman.groceryshopping.databinding.ActivityUpdateProfileBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class UpdateProfileActivity extends AppCompatActivity {
    UserSessionManager userSessionManager;
    Context context = UpdateProfileActivity.this;
    Session session;
    ActivityUpdateProfileBinding binding;
    LoadingDialog dialog;
    String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
        binding.registerBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });
    }

    private void CheckData() {
        if (binding.registerName.getText().toString().isEmpty() && binding.registerName.getText().toString().isEmpty() && binding.registerPhone.getText().toString().isEmpty() && binding.registerPaddress.getText().toString().isEmpty()) {
            binding.registerName.setError("Please Fill");
            binding.registerPhone.setError("Please Fill");

            binding.registerPaddress.setError("Please Fill");
        } else if (binding.registerName.getText().toString().isEmpty()) {
            binding.registerName.setError("Please Fill");
        } else if (binding.registerPhone.getText().toString().isEmpty()) {
            binding.registerPhone.setError("Please Fill");
        } else if (binding.registerPaddress.getText().toString().isEmpty()) {
            binding.registerPaddress.setError("Please Fill");
        } else {
            if (!binding.registerPassword.getText().toString().isEmpty()) {
                password = binding.registerPassword.getText().toString();
                if (binding.registerConfirmpassword.getText().toString().isEmpty()) {
                    binding.registerConfirmpassword.setError("Please Enter");
                } else if (password.equals(binding.registerConfirmpassword.getText().toString())) {
                    RegisterUser();
                } else {
                    Toast.makeText(context, "Password And Confirm Password Must Be Same", Toast.LENGTH_SHORT).show();
                }
            } else {
                RegisterUser();
            }

        }
    }

    private void initViews() {
        dialog = new LoadingDialog(UpdateProfileActivity.this);
        userSessionManager = new UserSessionManager(context);
        session = userSessionManager.getSessionDetails();
        binding.registerPhone.setText(session.getPhone());
        binding.registerName.setText(session.getName());
        binding.registerPaddress.setText(session.getPaddress());
    }


    private void RegisterUser() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.UPDATE_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("updateres", response);
                try {
                    JSONObject object;
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        object = jsonObject.getJSONObject("user");
                        session = new Session(object.getString("id"), object.getString("name"), object.getString("phone"), object.getString("address"), object.getString("permanent_address"));
                        userSessionManager.setSessionDetails(session);
                        GoToHomeActivity();
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
                    Log.d("updateexception", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("updateerror", error.toString());
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_id", session.getId());
                map.put("phone", binding.registerPhone.getText().toString());
                map.put("password", password);
                map.put("name", binding.registerName.getText().toString());
                map.put("address", binding.registerPaddress.getText().toString());
                map.put("permanentaddress", binding.registerPaddress.getText().toString());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.getCache().clear();
        queue.add(stringRequest);
    }

    private void GoToHomeActivity() {
        startActivity(new Intent(context, MainActivity.class));
        finishAffinity();
    }
}
