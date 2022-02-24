package com.example.bookstoreapplication.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bookstoreapplication.R;
import com.example.bookstoreapplication.admin.addCategory.ListCategoryActivity;
import com.example.bookstoreapplication.admin.books.ListBookActivity;
import com.example.bookstoreapplication.api.ApiClient;
import com.example.bookstoreapplication.api.response.Dash;
import com.example.bookstoreapplication.api.response.DashResponse;
import com.example.bookstoreapplication.utils.SharedPrefUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends AppCompatActivity {
    private static final int TAKE_PICTURE = 2;
    private static final int PICK_PICTURE = 1;
    private static final String TEMP_DIRECT = "/Book/Picture/.temp/";
    TextView pendingOrdersTV, totalOrdersTV, shippedOrdersTV, totalCategoriesTV, totalCustomersTV, totalProductsTV;
    LinearLayout addCategory, imageLayout, categoryList, productsLL;
    private Uri imageUri;
    String currentPhotoPath;
    ImageView selectedIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Admin Area");
        findIds();
        getDash();
    }

    private void getDash(){
        String key = SharedPrefUtils.getString(this, getString(R.string.api_key));
        Call<DashResponse> addressResponseCall = ApiClient.getClient().getDash(key);
        addressResponseCall.enqueue(new Callback<DashResponse>() {
            @Override
            public void onResponse(Call<DashResponse> call, Response<DashResponse> response) {
                if(response.isSuccessful()){
                //    setDash(response.body().getDash());
                }
            }

            @Override
            public void onFailure(Call<DashResponse> call, Throwable t) {

            }
        });
    }

    private void setDash(Dash dash){
//        pendingOrdersTV.setText(dash.getPendingOrders().toString());
//        totalCategoriesTV.setText(dash.getCategories().toString());
//        totalCustomersTV.setText(dash.getCustomers().toString());
//        totalOrdersTV.setText(dash.getProcessingOrders().toString());
//        shippedOrdersTV.setText(dash.getShippedOrders().toString());
//        totalProductsTV.setText(dash.getProducts().toString());
    }

    private void findIds(){
        pendingOrdersTV = findViewById(R.id.pendingOrdersTV);
        totalCategoriesTV = findViewById(R.id.totalCategoriesTV);
        totalCustomersTV = findViewById(R.id.totalCustomersTV);
        totalOrdersTV = findViewById(R.id.totalOrdersTV);
        shippedOrdersTV = findViewById(R.id.shippedOrdersTV);
        totalProductsTV = findViewById(R.id.totalProductsTV);
        addCategory = findViewById(R.id.addCategory);
        categoryList = findViewById(R.id.categoryList);
        productsLL = findViewById(R.id.productsLL);
        setClickListeners();
    }

    private void setClickListeners(){
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddCategoryView();
            }
        });
        categoryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, ListCategoryActivity.class);
                startActivity(intent);
            }
        });
        productsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, ListBookActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openAddCategoryView(){

    }
}