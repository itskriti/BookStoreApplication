package com.example.bookstoreapplication.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.bookstoreapplication.R;
import com.example.bookstoreapplication.api.ApiClient;
import com.example.bookstoreapplication.api.response.RegisterResponse;
import com.example.bookstoreapplication.utils.DataHolder;
import com.example.bookstoreapplication.utils.SharedPrefUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText currentPwET, newPwET, confirmPwET;
    LinearLayout changePwLL;
    String passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getSupportActionBar().setTitle("Change Password");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        currentPwET= findViewById(R.id.currentPwET);
        newPwET = findViewById(R.id.newPwET);
        confirmPwET = findViewById(R.id.confirmPwET);
        changePwLL = findViewById(R.id.changePwLL);

        changePwLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassword();
            }
        });
    }

    private void updatePassword() {
        if (validateAll()) {
            String key = SharedPrefUtils.getString(this, getString(R.string.api_key));
            retrofit2.Call<RegisterResponse> changePassword = ApiClient.getClient().forgetPassword(key, newPwET.getText().toString());
            changePassword.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(retrofit2.Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    if (response.isSuccessful()) {
                        if (!response.body().getError()) {
                            Toast.makeText(ChangePasswordActivity.this, "Password Successfully changed", Toast.LENGTH_SHORT).show();
                            SharedPrefUtils.setString(ChangePasswordActivity.this,  DataHolder.PASSWORD_KEY, newPwET.getText().toString());
                            finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                }
            });
        }
    }

    private boolean validateAll() {
        if (validatePassword() && newPasswordET()) {
            return true;
        }
        return false;
    }

    private boolean validatePassword() {
        String dbPassword = SharedPrefUtils.getString(this, DataHolder.PASSWORD_KEY);

        passwordText = currentPwET.getText().toString().trim();
        if (passwordText.isEmpty()) {
            Toast.makeText(this, "Field cannot be left empty!!!", Toast.LENGTH_SHORT).show();

        } else if(!passwordText.equals(dbPassword)) {
            Toast.makeText(this, "Old password doesn't match!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean newPasswordET() {
        passwordText = currentPwET.getText().toString().trim();
        String confirmPasswordText = newPwET.getText().toString().trim();

        if (confirmPasswordText.isEmpty()) {
            Toast.makeText(this, "Field cannot be left empty!!!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (confirmPasswordText.equals(passwordText)) {
            Toast.makeText(this, "New password must different from old password!!!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!newPwET.getText().toString().equals(confirmPwET.getText().toString())) {
            confirmPwET.setError("Password does not match please check!!!");
            return false;
        }
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}