package com.example.bookstoreapplication.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.bookstoreapplication.MainActivity;
import com.example.bookstoreapplication.R;
import com.example.bookstoreapplication.api.ApiClient;
import com.example.bookstoreapplication.api.response.ProfileResponse;
import com.example.bookstoreapplication.utils.SharedPrefUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {
    TextView nameTV, emailTV;
    ImageView backIV;
    LinearLayout saveLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        nameTV = findViewById(R.id.nameTV);
        emailTV = findViewById(R.id.emailTV);
        saveLL = findViewById(R.id.saveLL);
        backIV = findViewById(R.id.backIV);

        nameTV.setText(SharedPrefUtils.getString(UserProfileActivity.this, getString(R.string.name_key)));
        emailTV.setText(SharedPrefUtils.getString(UserProfileActivity.this, getString(R.string.email_key)));

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        saveLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    String key = SharedPrefUtils.getString(UserProfileActivity.this, getString(R.string.api_key));
                    Call<ProfileResponse>  profileResponseCall = ApiClient.getClient().updateProfile(key, nameTV.getText().toString(), emailTV.getText().toString());
                    profileResponseCall.enqueue(new Callback<ProfileResponse>() {
                        @Override
                        public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                            if(response.isSuccessful()){
                                if(!response.body().getError()){
                                    SharedPrefUtils.setString(UserProfileActivity.this, getString(R.string.name_key), nameTV.getText().toString());
                                    SharedPrefUtils.setString(UserProfileActivity.this, getString(R.string.email_id), emailTV.getText().toString());
                                    Toast.makeText(UserProfileActivity.this, "Profi;e Updated Successfully", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ProfileResponse> call, Throwable t) {

                        }
                    });

                }

            }
        });

    }

    public boolean validate() {
        if(emailTV.getText().toString().isEmpty() || nameTV.getText().toString().isEmpty()){
            Toast.makeText(UserProfileActivity.this, "None of the above fields can be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Patterns.EMAIL_ADDRESS.matcher(emailTV.getText().toString()).matches()){
            return true;
        } else{
            Toast.makeText(this, "Enter valid email address !!", Toast.LENGTH_SHORT).show();
        } return false;

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




