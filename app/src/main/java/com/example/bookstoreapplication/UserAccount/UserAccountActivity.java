package com.example.bookstoreapplication.UserAccount;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookstoreapplication.R;
import com.example.bookstoreapplication.UserAccount.fragments.LoginFragment;
import com.example.bookstoreapplication.UserAccount.fragments.RegisterFragment;

public class UserAccountActivity extends AppCompatActivity implements View.OnClickListener {
    TextView registerTV, signUpRegisterTV;
    boolean isRegister = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        registerTV = findViewById(R.id.registerTV);
        signUpRegisterTV = findViewById(R.id.signUpRegisterTV);
        registerTV.setOnClickListener(this);
        getSupportFragmentManager().beginTransaction().add(R.id.formContainerFL, new LoginFragment()).commit();

    }

    @Override
    public void onClick(View v) {
        if (v == registerTV){
            if(!isRegister){
                getSupportFragmentManager().beginTransaction().replace(R.id.formContainerFL, new RegisterFragment()).commit();
            }
            else {
                getSupportFragmentManager().beginTransaction().replace(R.id.formContainerFL, new LoginFragment()).commit();
            }
            changeTexts();
            isRegister = !isRegister;
        }
    }

    void changeTexts(){
        if(!isRegister){
            registerTV.setText("Login");
            signUpRegisterTV.setText("Already have an account?  ");
        } else {
            registerTV.setText("Register");
            signUpRegisterTV.setText("Do not have an account?  ");
        }
    }
}
