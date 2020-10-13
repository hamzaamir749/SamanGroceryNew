package com.saman.groceryshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

public class ForgetPasswordActivity extends AppCompatActivity {

    TextInputEditText forget_number, new_password;
    Button verifyNumber;
    CountryCodePicker ccp;
    String pass, num;
    ImageView passEyeImage;

    boolean showPass = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        forget_number =findViewById(R.id.forget_number);
        new_password =findViewById(R.id.new_password);
        verifyNumber = findViewById(R.id.verifyNumber);
        passEyeImage = findViewById(R.id.passEyeImage);

        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ccp.enableHint(false);
        ccp.registerPhoneNumberTextView(forget_number);
        pass = new_password.getText().toString();

        passEyeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showPass == false){
                    new_password.setTransformationMethod(null);
                    showPass = true;
                }
                else if (showPass == true){
                    new_password.setTransformationMethod(new PasswordTransformationMethod());
                    showPass = false;
                }
            }
        });

        verifyNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass = new_password.getText().toString();
                num = forget_number.getText().toString();

                if (!pass.equals("") && !forget_number.getText().toString().equals("")){
                    Toast.makeText(ForgetPasswordActivity.this, ""+ccp.getNumber(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ForgetPasswordActivity.this, NumberVerificationActivity.class)
                            .putExtra("number", ccp.getNumber())
                            .putExtra("password", pass)
                            .putExtra("actualNum", num));
                }
                else {
                    Toast.makeText(ForgetPasswordActivity.this, "Enter Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}