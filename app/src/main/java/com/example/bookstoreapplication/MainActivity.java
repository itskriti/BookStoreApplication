package com.example.bookstoreapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.example.bookstoreapplication.api.response.Category;
import com.example.bookstoreapplication.home.fragments.CartFragment;
import com.example.bookstoreapplication.home.fragments.CategoryFragment;
import com.example.bookstoreapplication.home.fragments.ProfileFragment;
import com.example.bookstoreapplication.home.fragments.WishListFragment;
import com.example.bookstoreapplication.home.fragments.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment;
    CartFragment cartFragment;
    ProfileFragment profileFragment;
    WishListFragment wishListFragment;
    CategoryFragment categoryFragment;
    Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.mainBottomNav);
        homeFragment = new HomeFragment();
        homeFragment.setBottomNavigationView(bottomNavigationView);
        currentFragment = homeFragment;
        getSupportFragmentManager().beginTransaction().add(R.id.mainFrameContainer, homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getTitle().equals(getString(R.string.home))){
                    if(homeFragment == null)
                        homeFragment = new HomeFragment();
                    changeFragment(homeFragment);
                    return true;

                }

                   if(item.getTitle().equals("Categories")){
                       if (categoryFragment == null)
                          categoryFragment = new CategoryFragment();
                       changeFragment(categoryFragment);
                       return true;
                   }

                if(item.getTitle().equals("Cart")){
                    if (cartFragment == null)
                        cartFragment = new CartFragment();
                    changeFragment(cartFragment);
                    return true;
                }

                if(item.getTitle().equals("Wishlist")){
                    if (wishListFragment == null)
                        wishListFragment = new WishListFragment();
                    changeFragment(wishListFragment);
                    return true;
                }


                if(item.getTitle().equals("Profile")){
                    if(profileFragment == null)
                        profileFragment = new ProfileFragment();
                    changeFragment(profileFragment);
                    return true;
                }
                return false;
            }
        });
    }

    void changeFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().hide(currentFragment).commit();

        if(fragment.isAdded()){
            getSupportFragmentManager().beginTransaction().show(fragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().add(R.id.mainFrameContainer, fragment).commit();
        }
        currentFragment = fragment;
    }







}