package com.saman.groceryshopping;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.saman.groceryshopping.Fragments.CartFragment;
import com.saman.groceryshopping.Fragments.FavFragment;
import com.saman.groceryshopping.Fragments.GalleryFragment;
import com.saman.groceryshopping.Fragments.HomeFragment;
import com.saman.groceryshopping.Fragments.ProfileFragment;
import com.saman.groceryshopping.HelperClasses.UserSessionManager;

public class MainActivity extends AppCompatActivity {

    static BottomNavigationView bottomNav;
    static View badge;
    Bundle bundle;

    public static resumeIt resumeIt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnNavigationItemSelectedListener(navListener);
        badge = LayoutInflater.from(MainActivity.this).inflate(R.layout.notification_badge, bottomNav, false);

        bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString("home") != null) {
                bottomNav.setSelectedItemId(R.id.nav_home);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
            }
            if (bundle.getString("favorite") != null) {
                bottomNav.setSelectedItemId(R.id.nav_favorites);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FavFragment()).commit();
            }
            if (bundle.getString("gallery") != null) {
                bottomNav.setSelectedItemId(R.id.nav_images);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new GalleryFragment()).commit();
            }
            if (bundle.getString("cart") != null) {
                bottomNav.setSelectedItemId(R.id.nav_cart);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CartFragment()).commit();
            }
            if (bundle.getString("profile") != null) {
                bottomNav.setSelectedItemId(R.id.nav_profile);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
            }
        } else {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
            }
        }
        closeKeyboard();

        if (UserSessionManager.cart != null){
            if (UserSessionManager.cart.getTotalItems() != 0){
                MainActivity.showBadge(MainActivity.this, MainActivity.bottomNav, R.id.nav_cart, String.valueOf(UserSessionManager.cart.getTotalItems()));
            }
        }
    }

    public void ShowBadge(int item) {
        if (item == 0) {
            MainActivity.removeBadge(MainActivity.bottomNav, R.id.nav_cart);
        } else {
            MainActivity.showBadge(MainActivity.this, MainActivity.bottomNav, R.id.nav_cart, String.valueOf(item));
        }
    }

    public static void showBadge(Context context, BottomNavigationView
            bottomNavigationView, @IdRes int itemId, String value) {
        removeBadge(bottomNavigationView, itemId);
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);

        TextView text = badge.findViewById(R.id.badge_text_view);
        text.setText(value);
        itemView.addView(badge);
    }

    public static void removeBadge(BottomNavigationView bottomNavigationView, @IdRes int itemId) {
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        if (itemView.getChildCount() == 3) {
            itemView.removeViewAt(2);
        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (resumeIt!=null){
            resumeIt.resumeFragment();
        }
    }

    public interface resumeIt {
        void resumeFragment();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_favorites:
                            selectedFragment = new FavFragment();
                            break;
                        case R.id.nav_images:
                            selectedFragment = new GalleryFragment();
                            break;
                        case R.id.nav_cart:
                            selectedFragment = new CartFragment();
                            break;
                        case R.id.nav_profile:
                            selectedFragment = new ProfileFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };

    @Override
    protected void onResume() {
        super.onResume();

        if (UserSessionManager.cart != null){
            if (UserSessionManager.cart.getTotalItems() != 0){
                MainActivity.showBadge(MainActivity.this, MainActivity.bottomNav, R.id.nav_cart, String.valueOf(UserSessionManager.cart.getTotalItems()));
            }
        }
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }


}
