package com.example.bookstoreapplication.orders.orderDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.bookstoreapplication.R;
import com.example.bookstoreapplication.api.ApiClient;
import com.example.bookstoreapplication.api.response.OrderHistoryResponse;
import com.example.bookstoreapplication.orders.OrdersAdapter;
import com.example.bookstoreapplication.utils.SharedPrefUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {
    ImageView backIV;
    RecyclerView orderHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        getWindow().setStatusBarColor(Color.WHITE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_order_history);
        backIV = findViewById(R.id.backIV);
        orderHistory = findViewById(R.id.orderHistory);

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });
        getOrderHistory();

    }

    private void getOrderHistory(){
        String key = SharedPrefUtils.getString(this, "apk");
        Call<OrderHistoryResponse> historyResponseCall = ApiClient.getClient().orderHistory(key);
        historyResponseCall.enqueue(new Callback<OrderHistoryResponse>() {
            @Override
            public void onResponse(Call<OrderHistoryResponse> call, Response<OrderHistoryResponse> response) {
                if(response.isSuccessful()){
                    if(!response.body().getError()){
                        showOrders(response.body().getOrderHistory());
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderHistoryResponse> call, Throwable t) {

            }
        });
    }

    private void showOrders(List<com.example.bookstoreapplication.api.response.OrderHistory>order){
        orderHistory.setHasFixedSize(true);
        orderHistory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        OrdersAdapter ordersAdapter = new OrdersAdapter(order, this);
        orderHistory.setAdapter(ordersAdapter);
    }

}