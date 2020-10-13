package com.saman.groceryshopping.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;
import com.saman.groceryshopping.HelperClasses.UserSessionManager;
import com.saman.groceryshopping.HelperClasses.Utils;

import java.util.ArrayList;

public class CustomPagerAdapter extends PagerAdapter {
    private ImageView ivSlideImage;
    private ArrayList<Integer> imagesList;

    private Context context;

    public CustomPagerAdapter(Context ctx) {
        this.context = ctx;
        imagesList = Utils.getSliderImages();
    }

    @Override
    public int getCount() {
        return UserSessionManager.sliderImages.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ivSlideImage = new ImageView(context);
        ivSlideImage.setScaleType(ImageView.ScaleType.FIT_XY);

        //Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),image_resources[position]);
        //ivSlideImage.setImageBitmap(bitmap);
        if (position >= imagesList.size()) {
            imagesList.addAll(imagesList);
        }

        Picasso.get()
                .load(UserSessionManager.sliderImages.get(position))
                .placeholder(imagesList.get(position))
                .fit()
                .into(ivSlideImage);
        container.addView(ivSlideImage);
        return ivSlideImage;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ImageView view = (ImageView) object;
        container.removeView(view);
    }
}
