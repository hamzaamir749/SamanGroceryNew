package com.saman.groceryshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.saman.groceryshopping.databinding.ActivityThankYouBinding;

public class ThankYouActivity extends AppCompatActivity {
    String pricetotal = "";
    ActivityThankYouBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityThankYouBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pricetotal=getIntent().getStringExtra("price_total");
        binding.thankuTotal.setText("RS "+pricetotal);
        binding.thankuDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finishAffinity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finishAffinity();
        super.onBackPressed();
    }
}
