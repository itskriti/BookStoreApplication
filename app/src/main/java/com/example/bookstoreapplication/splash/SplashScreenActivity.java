package com.example.bookstoreapplication.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookstoreapplication.MainActivity;
import com.example.bookstoreapplication.R;
import com.example.bookstoreapplication.UserAccount.UserAccountActivity;
import com.example.bookstoreapplication.utils.SharedPrefUtils;

public class SplashScreenActivity extends AppCompatActivity {
    boolean isLoggedIn = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getIsLoggedInOtNot();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLoggedIn)
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                else
                    startActivity(new Intent(SplashScreenActivity.this, UserAccountActivity.class));
                finish();
            }
        }, 1000);
    }

    public void getIsLoggedInOtNot(){
        isLoggedIn = SharedPrefUtils.getBool(this, getString(R.string.isLogged), false);
    }
}




