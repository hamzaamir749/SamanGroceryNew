package com.saman.groceryshopping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;
import com.saman.groceryshopping.Adapters.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends AppCompatActivity {

    Button login, register;
    ViewPagerAdapter adapter;
    ViewPager viewPager;
    TabLayout indicator;
    List<Integer> listImages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        register = findViewById(R.id.btn_start_register);
        login = findViewById(R.id.btn_start_login);
        indicator = (TabLayout) findViewById(R.id.indicator);
        viewPager = findViewById(R.id.flipper_layout);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, LoginActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, RegisterActivity.class));
            }
        });
        listImages = new ArrayList<>();
        listImages.add(R.drawable.slideimage1);
        listImages.add(R.drawable.slideimage2);
        listImages.add(R.drawable.slideimage3);
        SetSlider();
    }

    private void SetSlider() {

        adapter = new ViewPagerAdapter(StartActivity.this, listImages);
        viewPager.setAdapter(adapter);
        indicator.setupWithViewPager(viewPager, true);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 5000, 7000);

    }

    private class SliderTimer extends TimerTask {
        @Override
        public void run() {
            StartActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < listImages.size() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }

}