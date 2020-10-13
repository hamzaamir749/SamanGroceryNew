package com.saman.groceryshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;

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
import com.saman.groceryshopping.databinding.ActivityRegisterBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    Context context = RegisterActivity.this;
    LoadingDialog dialog;
    UserSessionManager userSessionManager;
    Session session;
    boolean showPass = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initViews();

        binding.registerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToLoginActivity();
            }
        });

        binding.registerBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });

        binding.passEyeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showPass == false){
                    binding.registerPassword.setTransformationMethod(null);
                    binding.registerConfirmpassword.setTransformationMethod(null);
                    showPass = true;
                }
                else if (showPass == true){
                    binding.registerPassword.setTransformationMethod(new PasswordTransformationMethod());
                    binding.registerConfirmpassword.setTransformationMethod(new PasswordTransformationMethod());
                    showPass = false;
                }
            }
        });
    }

    private void CheckData() {
        if (binding.registerName.getText().toString().isEmpty() && binding.registerPhone.getText().toString().isEmpty() && binding.registerAddress.getText().toString().isEmpty() && binding.registerPassword.getText().toString().isEmpty() && binding.registerConfirmpassword.getText().toString().isEmpty()) {
            binding.registerName.setError("Please Fill");
            binding.registerPhone.setError("Please Fill");
            binding.registerAddress.setError("Please Fill");
            binding.registerPassword.setError("Please Fill");
            binding.registerConfirmpassword.setError("Please Fill");
        } else if (binding.registerName.getText().toString().isEmpty()) {
            binding.registerName.setError("Please Fill");
        } else if (binding.registerPhone.getText().toString().isEmpty()) {
            binding.registerPhone.setError("Please Fill");
        } else if (binding.registerAddress.getText().toString().isEmpty()) {
            binding.registerAddress.setError("Please Fill");
        } else if (binding.registerPassword.getText().toString().isEmpty()) {
            binding.registerPassword.setError("Please Fill");
        } else if (binding.registerConfirmpassword.getText().toString().isEmpty()) {
            binding.registerConfirmpassword.setError("Please Fill");
        }
        else if (!binding.registerPassword.getText().toString().equals(binding.registerConfirmpassword.getText().toString())){
            binding.registerConfirmpassword.setError("Please Enter Same Password");
        }
        else {
            RegisterUser();
        }
    }

    private void RegisterUser() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("registerres", response);
                try {
                    JSONObject object;
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        object = jsonObject.getJSONObject("user");
                        session = new Session(object.getString("id"), object.getString("name"), object.getString("phone"), object.getString("address"),"");
                        userSessionManager.setLoggedIn(true);
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
                    Log.d("registerexception", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("registererror", error.toString());
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("phone", binding.registerPhone.getText().toString());
                map.put("password", binding.registerPassword.getText().toString());
                map.put("name", binding.registerName.getText().toString());
                map.put("address", binding.registerAddress.getText().toString());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.getCache().clear();
        queue.add(stringRequest);
    }

    private void GoToHomeActivity() {
        startActivity(new Intent(context, MainActivity.class));
        finish();
    }

    private void GoToLoginActivity() {
        startActivity(new Intent(context, LoginActivity.class));
        finish();
    }

    private void initViews() {
        dialog = new LoadingDialog(RegisterActivity.this);
        userSessionManager = new UserSessionManager(context);
    }
}
