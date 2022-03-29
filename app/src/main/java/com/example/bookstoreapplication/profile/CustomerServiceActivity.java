package com.example.bookstoreapplication.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.bookstoreapplication.R;

public class CustomerServiceActivity extends AppCompatActivity {

    LinearLayout callStaffLL, backLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service);
        callStaffLL = findViewById(R.id.callStaffLL);
        backLL = findViewById(R.id.backLL);

        backLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });

        callStaffLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call("+9779846950151");
            }
        });
    }

    private void call(String number) {

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
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