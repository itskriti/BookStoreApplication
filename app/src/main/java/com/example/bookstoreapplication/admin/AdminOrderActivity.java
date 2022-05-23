package com.example.bookstoreapplication.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.bookstoreapplication.R;
import com.example.bookstoreapplication.api.ApiClient;
import com.example.bookstoreapplication.api.response.OrderHistory;
import com.example.bookstoreapplication.api.response.OrderHistoryResponse;
import com.example.bookstoreapplication.orders.OrdersAdapter;
import com.example.bookstoreapplication.utils.SharedPrefUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminOrderActivity extends AppCompatActivity {
    ImageView backIv;
    RecyclerView orderHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);
        getSupportActionBar().hide();
        backIv = findViewById(R.id.backIv);
        orderHistory = findViewById(R.id.orderHistory);

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getOrderHistory();
    }

    private void getOrderHistory() {
        String key = SharedPrefUtils.getString(this, getString(R.string.api_key));
        Call<OrderHistoryResponse> orderHistoryResponseCall = ApiClient.getClient().orderHistory(key);
        orderHistoryResponseCall.enqueue(new Callback<OrderHistoryResponse>() {
            @Override
            public void onResponse(Call<OrderHistoryResponse> call, Response<OrderHistoryResponse> response) {
                if (response.isSuccessful()){
                    if (!response.body().getError()){
                        showOrders(response.body().getOrderHistory());
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderHistoryResponse> call, Throwable t) {

            }
        });

    }

    private void showOrders(List<OrderHistory> order) {
        orderHistory.setHasFixedSize(true);
        orderHistory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        OrdersAdapter orderAdapter = new OrdersAdapter(order, this);
        orderHistory.setAdapter(orderAdapter);

    }
}