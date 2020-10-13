package com.saman.groceryshopping.Adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.saman.groceryshopping.Fragments.CartFragment;
import com.saman.groceryshopping.Fragments.FavFragment;
import com.saman.groceryshopping.Fragments.HomeFragment;
import com.saman.groceryshopping.Fragments.ProfileFragment;

public class TabAdapter  extends FragmentPagerAdapter {
    private Context myContext;
    int totalTabs;

    public TabAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                FavFragment favFragment = new FavFragment();
                return favFragment;
            case 2:
                CartFragment cartFragment = new CartFragment();
                return cartFragment;
            case 3:
                ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;


            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }



}
