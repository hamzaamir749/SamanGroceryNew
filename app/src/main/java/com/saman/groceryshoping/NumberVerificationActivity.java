package com.saman.groceryshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.saman.groceryshopping.Models.MDForgetPass;
import com.saman.groceryshopping.retrofit.RetroServices;
import com.saman.groceryshopping.retrofit.RetrofitClientInstance;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NumberVerificationActivity extends AppCompatActivity {

    Button btnVerify;
    TextView cellNumtxt;

    String number, password, actualNum;
    String otpCode = "";

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private OtpView otpView;
    private final String TAG = "PHONE_VERIFICATION";

    ImageView backPressed_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_verification);


        mAuth = FirebaseAuth.getInstance();

        number = getIntent().getExtras().getString("number");
        password = getIntent().getExtras().getString("password");
        actualNum = getIntent().getExtras().getString("actualNum");

        btnVerify = findViewById(R.id.btn_verify);
        cellNumtxt= findViewById(R.id.cellNumtxt);
        otpView = findViewById(R.id.otp_view);
        backPressed_register = findViewById(R.id.backPressed_register);


        cellNumtxt.setText(number);

        backPressed_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        init();
        verifyNumber(number);
    }

    private void register(){
        /*Create handle for the RetrofitInstance interface*/
        RetroServices service = RetrofitClientInstance.getApiClient().create(RetroServices.class);
        Call<MDForgetPass> call = service.resetPass(actualNum, password);
        call.enqueue(new Callback<MDForgetPass>() {

            @Override
            public void onResponse(Call<MDForgetPass> call, Response<MDForgetPass> response) {
//                Toast.makeText(NumberVerficationScreen.this, "Ok"+"\n"+response, Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()){

                    MDForgetPass mdForgetPass = response.body();
                    String status = mdForgetPass.getStatus();
                    String message = mdForgetPass.getMessage();

                    if (status.equals("true")){
                        startActivity(new Intent(NumberVerificationActivity.this, LoginActivity.class));
                        Toast.makeText(NumberVerificationActivity.this, ""+ message, Toast.LENGTH_SHORT).show();
                        finishAffinity();
                    }if (status.equals("false")){
                        Toast.makeText(NumberVerificationActivity.this, ""+ message, Toast.LENGTH_SHORT).show();
                    }
                }

//                Log.e("response", response.toString());
            }

            @Override
            public void onFailure(Call<MDForgetPass> call, Throwable t) {
                Toast.makeText(NumberVerificationActivity.this, "Fail To Register", Toast.LENGTH_SHORT).show();
                Log.e("exception", t.toString());
            }
        });
    }

    private void init() {
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboardFrom(NumberVerificationActivity.this, otpView);
                if (otpCode != null && otpCode.isEmpty()){
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otpCode);
                    signInWithPhoneAuthCredential(credential);
                }else {
                    Toast.makeText(NumberVerificationActivity.this, "Enter Code", Toast.LENGTH_SHORT).show();
                }
            }
        });


        otpView.setCursorVisible(true);
//        otpView.setCursorColor(getResources().getColor(R.color.black));
        otpView.setAnimationEnable(false);
        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                hideKeyboardFrom(NumberVerificationActivity.this, otpView);
                otpCode = otp;
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
                signInWithPhoneAuthCredential(credential);
            }
        });

    }


    private void isValid() {
        if (number.isEmpty()) {
            showSnackBar("Phone number is required");
        } else {
            verifyNumber(number);
        }
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void showSnackBar(String msg) {
        Toast.makeText(this, ""+msg, Toast.LENGTH_SHORT).show();
    }

    private void verifyNumber(String phoneNumber) {
        initaliseCallback();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);

    }


    private void initaliseCallback() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);

//                    ProjectUtils.pauseProgressDialog();
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
//                    ProjectUtils.pauseProgressDialog();

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(NumberVerificationActivity.this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Toast.makeText(NumberVerificationActivity.this, "SMS quota has been exceeded", Toast.LENGTH_SHORT).show();
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
//                    ProjectUtils.pauseProgressDialog();
//                    phoneLayout.setVisibility(View.GONE);
//                    verfiyLayout.setVisibility(View.VISIBLE);

                // ...
            }
        };
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//            ProjectUtils.showProgressDialog(this, false, getResources().getString(R.string.please_wait));
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
//                            ProjectUtils.pauseProgressDialog();
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
//                                Intent intent = new Intent();
                            register();
                            Log.d(TAG, "signInWithCredential:success" + user.getPhoneNumber());
//                                intent.putExtra("data", user.getPhoneNumber());
//                                setResult(RESULT_OK, intent);
//                                finish();
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                    Intent intent = new Intent();
//                                    intent.putExtra("data", "");
//                                    setResult(RESULT_CANCELED, intent);
//                                    finish();
                            }
                        }
                    }
                });
    }
}