package com.saman.groceryshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class SplashTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_test);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                init();
                Toast.makeText(SplashTestActivity.this, "Splash Ok", Toast.LENGTH_SHORT).show();
            }
        }, 4000);
    }
}