package com.saman.groceryshopping.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.saman.groceryshopping.Adapters.CartAdapter;
import com.saman.groceryshopping.HelperClasses.UserSessionManager;
import com.saman.groceryshopping.MainActivity;
import com.saman.groceryshopping.Models.ProductsModel;
import com.saman.groceryshopping.R;
import com.saman.groceryshopping.databinding.FragmentCartBinding;
import com.saman.groceryshopping.databinding.FragmentYourBillBinding;

import java.util.ArrayList;
import java.util.List;


public class Fragment_yourBill extends Fragment {

    static List<ProductsModel> list;
    FragmentYourBillBinding binding;
    Context context;
    private LinearLayoutManager linearLayoutManagerFeature;
    public static RelativeLayout linearLayout;
    public static RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentYourBillBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.context = view.getContext();
        recyclerView = view.findViewById(R.id.cart_recycler);
        linearLayout = view.findViewById(R.id.cart_linearShowEmpty);
        initViews();
        SetCart();

        binding.startShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, MainActivity.class));
                ((Activity) context).finish();
            }
        });

    }

    private void initViews() {
        linearLayoutManagerFeature = new LinearLayoutManager(context);
        binding.cartRecycler.setLayoutManager(linearLayoutManagerFeature);
    }

    private void SetCart() {
        list = new ArrayList<>();
        if (UserSessionManager.cart != null) {
            if (UserSessionManager.cart.getTotalPrice() == 0) {
                new CartFragment().checkLayout.setVisibility(View.GONE);
                binding.cartLinearShowEmpty.setVisibility(View.VISIBLE);
            } else {
                binding.cartLinearShowEmpty.setVisibility(View.GONE);
                list = UserSessionManager.cart.productList;
                CartAdapter adapter = new CartAdapter(list, context);
                binding.cartRecycler.setAdapter(adapter);
                new CartFragment().setTotalPrice();
                new CartFragment().checkLayout.setVisibility(View.VISIBLE);
            }

        }
    }

}
