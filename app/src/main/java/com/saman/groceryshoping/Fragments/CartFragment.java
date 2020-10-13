package com.saman.groceryshopping.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuhart.stepview.StepView;
import com.saman.groceryshopping.HelperClasses.UserSessionManager;
import com.saman.groceryshopping.R;
import com.saman.groceryshopping.databinding.FragmentCartBinding;

import java.util.ArrayList;

public class CartFragment extends Fragment {
    FragmentCartBinding binding;
    static TextView btnCheckLayout;
    Context context;
    View view;
    public static StepView stepView;
    public static Button checkoutbtn;
    UserSessionManager userSessionManager;
   public static RelativeLayout checkLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.context = view.getContext();
        this.view = view;
        stepView = view.findViewById(R.id.step_view);
        btnCheckLayout = view.findViewById(R.id.txt_RS);
        checkLayout=view.findViewById(R.id.check_out);
        checkoutbtn = view.findViewById(R.id.checkout_btnCheckToSecond);
        initViews();

        stepView.setSteps(new ArrayList<String>() {{
            add("Fill Your Cart");
            add("Checkout");
            add("Completed");
        }});

        loadFragment(new Fragment_yourBill());
        binding.checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserSessionManager.cart.getTotalPrice()==0){
                    Toast.makeText(context, "No Product Available", Toast.LENGTH_SHORT).show();
                }
                else {
                    stepView.go(stepView.getCurrentStep() + 1, true);
                    loadFragment(new Fragment_PlaceOrder());
                    binding.checkOut.setVisibility(View.GONE);
                }
            }
        });

    }

    private void initViews() {
        userSessionManager = new UserSessionManager(context);

    }


    public void setTotalPrice() {
        int price = (int) UserSessionManager.cart.getTotalPrice();
        btnCheckLayout.setText("RS" + String.valueOf(price));
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = null;
        if (fm != null) {
            fragmentTransaction = fm.beginTransaction();
        }
        if (fragmentTransaction != null) {
            fragmentTransaction.replace(R.id.frameLayout, fragment);
        }
        if (fragmentTransaction != null) {
            fragmentTransaction.commit(); // save the changes
        }

    }
}
