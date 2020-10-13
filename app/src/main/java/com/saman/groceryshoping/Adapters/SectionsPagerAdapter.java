package com.saman.groceryshopping.Adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.saman.groceryshopping.Fragments.SubCategoriesFragment;
import com.saman.groceryshopping.Models.CategoryModel;

import java.util.List;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private List<CategoryModel> list;
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm,List<CategoryModel> list) {
        super(fm);
        mContext = context;
        this.list=list;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        int pos=Integer.valueOf(list.get(position).getId());
        return SubCategoriesFragment.newInstance(pos);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position).getName();
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return list.size();
    }
}
