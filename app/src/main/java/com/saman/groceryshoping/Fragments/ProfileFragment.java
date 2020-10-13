package com.saman.groceryshopping.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.saman.groceryshopping.Contacct_usActivity;
import com.saman.groceryshopping.HelperClasses.Session;
import com.saman.groceryshopping.HelperClasses.UserSessionManager;
import com.saman.groceryshopping.Purchase_HistoryActivity;
import com.saman.groceryshopping.StartActivity;
import com.saman.groceryshopping.UpdateProfileActivity;
import com.saman.groceryshopping.databinding.FragmentProfileBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    Context context;
    UserSessionManager userSessionManager;
    Session session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();
        initViews();
        binding.fragmentProfileSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSessionManager.setLoggedIn(false);
                userSessionManager.clearSessionData();
                context.startActivity(new Intent(context, StartActivity.class));
                ((Activity) context).finishAffinity();
            }
        });

        binding.fragmentProfileContactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Contacct_usActivity.class));
            }
        });
        binding.fragmentProfilePurchasehistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

      startActivity(new Intent(getActivity(),Purchase_HistoryActivity.class));
            }
        });
        binding.fragmentProfileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, UpdateProfileActivity.class));
            }
        });
    }

    private void initViews() {
        userSessionManager = new UserSessionManager(context);
        session = userSessionManager.getSessionDetails();
        binding.fragmentProfileName.setText(session.getName());
        binding.fragmentProfileNumber.setText(session.getPhone());
        binding.fragmentProfilePaddress.setText(session.getAddress());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
