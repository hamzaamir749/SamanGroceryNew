package com.saman.groceryshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
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
import com.saman.groceryshopping.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    Context context = LoginActivity.this;
    LoadingDialog dialog;
    UserSessionManager userSessionManager;
    Session session;
    boolean showPass = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initViews();

        binding.loginBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToRegisterActivity();
            }
        });

        binding.loginBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });

        binding.passEyeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showPass == false){
                    binding.loginPassword.setTransformationMethod(null);
                    showPass = true;
                }
                else if (showPass == true){
                    binding.loginPassword.setTransformationMethod(new PasswordTransformationMethod());
                    showPass = false;
                }
            }
        });

        binding.forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgetPasswordActivity.class));
            }
        });

    }

    private void initViews() {
        dialog = new LoadingDialog(LoginActivity.this);
        userSessionManager = new UserSessionManager(context);

    }

    private void CheckData() {
        if (binding.loginNumber.getText().toString().isEmpty() && binding.loginPassword.getText().toString().isEmpty()) {
            binding.loginNumber.setError("Please Fill");
            binding.loginPassword.setError("Please Fill");
        } else if (binding.loginNumber.getText().toString().isEmpty()) {
            binding.loginNumber.setError("Please Fill");
        } else if (binding.loginPassword.getText().toString().isEmpty()) {
            binding.loginPassword.setError("Please Fill");
        } else {
            LoginUser();
        }
    }

    private void LoginUser() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLHelper.LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("loginres", response);
                try {
                    JSONObject object;
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        object = jsonObject.getJSONObject("user");
                        session = new Session(object.getString("id"), object.getString("name"), object.getString("phone"), object.getString("address"),object.getString("permanent_address"));
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
                    Log.d("loginexception", e.toString());
                    Toast.makeText(context, "Exception : "+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("loginerror", error.toString());
                Toast.makeText(context, "Error : "+error.toString(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("phone", binding.loginNumber.getText().toString());
                map.put("password", binding.loginPassword.getText().toString());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.getCache().clear();
        queue.add(stringRequest);

    }

    private void GoToHomeActivity() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finishAffinity();
    }

    private void GoToRegisterActivity() {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
    }
}
