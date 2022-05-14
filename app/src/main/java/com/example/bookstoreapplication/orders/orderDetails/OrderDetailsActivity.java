package com.example.bookstoreapplication.orders.orderDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookstoreapplication.R;
import com.example.bookstoreapplication.api.response.Cart;
import com.example.bookstoreapplication.api.response.OrderHistory;

import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity {
    public static String key = "oKey";
    OrderHistory historyOrder;
    TextView orderId, status, orderDate, totalTV, paymentStatus, paymentStatus1, payStatus, phoneNum, delivery_area, street;
    RecyclerView cartIV;
    double deliveryCharge = 0;
    ImageView backIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);
//        getSupportActionBar().hide();
        orderId = findViewById(R.id.orderId);
        status = findViewById(R.id.status);
        cartIV = findViewById(R.id.cartIV);
        paymentStatus = findViewById(R.id.paymentStatus);
        paymentStatus1 = findViewById(R.id.paymentStatus1);
        payStatus = findViewById(R.id.payStatus);
        totalTV = findViewById(R.id.totalTV);
        orderDate = findViewById(R.id.orderDate);
        backIV = findViewById(R.id.backIv);
        phoneNum = findViewById(R.id.phoneNo);
        delivery_area = findViewById(R.id.delivery_area);
        street = findViewById(R.id.street);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (getIntent().getSerializableExtra(key) != null) {
            historyOrder = (OrderHistory) getIntent().getSerializableExtra(key);
            setOrderHistory(historyOrder);
        }
        showOrders();
    }

    private void setOrderHistory(OrderHistory orderHistory) {
        orderId.setText("#" + orderHistory.getId() + "");
//        phoneNum.setText(orderHistory.getAddress().getPhone());
        delivery_area.setText(orderHistory.getAddress().getCity());
        street.setText(orderHistory.getAddress().getStreet());
        orderDate.setText(orderHistory.getOrderDateTime());
        if (orderHistory.getPaymentType() == 1){
            payStatus.setText("Cash On Delivery");
            paymentStatus.setVisibility(View.VISIBLE);
            paymentStatus1.setVisibility(View.GONE);
        }else {
            payStatus.setText("Khalti");
            paymentStatus.setVisibility(View.GONE);
            paymentStatus1.setVisibility(View.VISIBLE);
        }
        if (orderHistory.getStatus() == 0){
            status.setText("Pending");
        }else if (orderHistory.getStatus()  == 1){
            status.setText("Processing");
        }else
            status.setText("Completed");

    }

    private void showOrders() {
        List<Cart> cartList = historyOrder.getCart();
        cartIV.setHasFixedSize(true);
        cartIV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        OrdersDetailsAdapter ordersDetailsAdapter = new OrdersDetailsAdapter(cartList, this);
        cartIV.setAdapter(ordersDetailsAdapter);
        setPrice(cartList);
    }

    private void setPrice(List<Cart> data) {
        List<Cart> newData = data;
        double totalPrice = 0;
        for (int i = 0; i < newData.size(); i++) {
            Cart book = newData.get(i);
            int price = book.getUnitPrice();
            int q = book.getQuantity();
            totalPrice = totalPrice + price * q;
        }
        totalTV.setText("Rs. " + (totalPrice + deliveryCharge));
    }
}