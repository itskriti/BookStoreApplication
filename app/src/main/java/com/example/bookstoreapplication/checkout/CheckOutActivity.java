package com.example.bookstoreapplication.checkout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.audiofx.DynamicsProcessing;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstoreapplication.R;
import com.example.bookstoreapplication.api.ApiClient;
import com.example.bookstoreapplication.api.response.Address;
import com.example.bookstoreapplication.api.response.AllBookResponse;
import com.example.bookstoreapplication.api.response.Book;
import com.example.bookstoreapplication.api.response.RegisterResponse;
import com.example.bookstoreapplication.checkout.address.AddressActivity;
import com.example.bookstoreapplication.checkout.orderComplete.OrderCompleteActivity;
import com.example.bookstoreapplication.home.fragments.home.adapters.ShopAdapter;
import com.example.bookstoreapplication.utils.SharedPrefUtils;
//import com.khalti.checkout.helper.Config;
//import com.khalti.checkout.helper.KhaltiCheckOut;
//import com.khalti.checkout.helper.OnCheckOutListener;
//import com.khalti.checkout.helper.PaymentPreference;
//import com.khalti.utils.Constant;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOutActivity extends AppCompatActivity {

    public static  String CHECK_OUT_PRODUCTS = "sd";
    RecyclerView allBookRV;
    AllBookResponse allBookResponse;
    ImageView backIV;

    ImageView cashOnDevIV, khaltiIV;
    LinearLayout addressLL, checkOutLL;
    Address address;
    TextView emptyAddressTv, cityStreetTV, provinceTV, totalTV, subTotalTV, shippingTV, totalPriceTV, discountTV;
    List<Book> books;
    double subTotalPrice = 0;
    double shippingCharge = 100;
    int p_type = 1;
    String p_ref = "COD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);
        backIV = findViewById(R.id.backIV);
        emptyAddressTv = findViewById(R.id.emptyAddressTv);
        addressLL = findViewById(R.id.addressLL);
        checkOutLL = findViewById(R.id.checkOutLL);
        cityStreetTV = findViewById(R.id.cityStreetAddTV);
        provinceTV = findViewById(R.id.provinceTV);
        allBookRV = findViewById(R.id.allBooksCheckRV);
        totalTV = findViewById(R.id.totalTV);
        subTotalTV = findViewById(R.id.subTotalTV);
        shippingTV = findViewById(R.id.shippingTV);
        totalPriceTV = findViewById(R.id.totalPriceTv);
        discountTV = findViewById(R.id.discountTV);
        khaltiIV = findViewById(R.id.khaltiIV);
        cashOnDevIV = findViewById(R.id.cashonDevIV);
        allBookResponse = (AllBookResponse) getIntent().getSerializableExtra(CHECK_OUT_PRODUCTS);
        books = allBookResponse.getBooks();
        setClickListenrs();
        loadCartList();

    }

    private void setClickListenrs(){
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        addressLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CheckOutActivity.this, AddressActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        emptyAddressTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CheckOutActivity.this, AddressActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        checkOutLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(address != null){
                    if(p_type == 1){
                        checkOut();
                    } else {
                        khaltiCheckOut();
                    }
                } else {
                    Toast.makeText(CheckOutActivity.this, "Please select a address", Toast.LENGTH_SHORT).show();
                }
            }
        });
        khaltiIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p_type = 2;
                khaltiIV.setBackground(getResources().getDrawable(R.drawable.box_shape_selected));
                cashOnDevIV.setBackground(getResources().getDrawable(R.drawable.box));

            }
        });
        cashOnDevIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p_type = 1;
                cashOnDevIV.setBackground(getResources().getDrawable(R.drawable.box_shape_selected));
                khaltiIV.setBackground(getResources().getDrawable(R.drawable.box));
            }
        });

    }

    private void khaltiCheckOut(){
//        Map<String, Object> map = new HashMap<>();
//        map.put("merchant_extra", "This is extra data");
//
//        Config.Builder builder = new Config.Builder(Constant.pub, "" + books.get(0).getId(), books.get(0).getName(), (long) (subTotalPrice + shippingCharge) * 100, new OnCheckOutListener() {
//            @Override
//            public void onError(@NonNull String action, @NonNull Map<String, String> errorMap) {
//                Log.i(action, errorMap.toString());
//                Toast.makeText(CheckOutActivity.this, errorMap.toString(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onSuccess(@NonNull Map<String, Object> data) {
//                Log.i("success", data.toString());
//                p_type = 2;
//                p_ref = data.toString();
//                checkOut();
//
//            }
//        })
//                .paymentPreferences(new ArrayList<PaymentPreference>() {{
//                    add(PaymentPreference.KHALTI);
//                    add(PaymentPreference.EBANKING);
//                    add(PaymentPreference.MOBILE_BANKING);
//                    add(PaymentPreference.CONNECT_IPS);
//                    add(PaymentPreference.SCT);
//                }})
//                .additionalData(map)
//                .productUrl("https://bazarhub.com.np/router-ups")
//                .mobile("9802778788");
//        Config config = builder.build();
//        KhaltiCheckOut khaltiCheckOut = new KhaltiCheckOut(this, config);
//        khaltiCheckOut.show();
//



    }

    private void loadCartList(){
        allBookRV.setHasFixedSize(true);
        allBookRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ShopAdapter shopAdapter = new ShopAdapter(books, this, true);
        shopAdapter.setRemoveEnabled(false);
        allBookRV.setAdapter(shopAdapter);
        setPrice();

    }

    private void setPrice() {
        double discount = 0;
        for(int i = 0; i < books.size(); i++){
            if(books.get(i).getDiscountPrice() != 0 || books.get(i).getDiscountPrice() != null){
                subTotalPrice = subTotalPrice + books.get(i).getDiscountPrice();
                discount = discount + books.get(i).getPrice() - books.get(i).getDiscountPrice();

            }
            else
                subTotalPrice = subTotalPrice + books.get(i).getPrice();
        }

        subTotalTV.setText("Rs. " + (subTotalPrice));
        totalTV.setText("Rs. " + (subTotalPrice + shippingCharge));
        totalPriceTV.setText("( Rs. " + subTotalPrice + " )");
        shippingTV.setText("Rs. " + shippingCharge);
        discountTV.setText("-" + "Rs. " + discount);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            assert  data != null;
            if(data.getSerializableExtra(AddressActivity.ADDRESS_SELECTED_KEY) != null){
                showSelectedAddress((Address) data.getSerializableExtra(AddressActivity.ADDRESS_SELECTED_KEY));
            }
        }
    }

    private void showSelectedAddress(Address adress) {
        address = adress;
        emptyAddressTv.setVisibility(View.GONE);
        cityStreetTV.setText(adress.getCity() + " " + adress.getStreet());
        provinceTV.setText(adress.getProvince());
        addressLL.setVisibility(View.VISIBLE);
    }

    private void checkOut() {
        String key = SharedPrefUtils.getString(this, "apk");
        Call<RegisterResponse> orderCall = ApiClient.getClient().order(key, p_type, address.getId(), p_ref);
        orderCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if(response.isSuccessful()){
                    Intent intent = new Intent(CheckOutActivity.this, OrderCompleteActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {

            }
        });
    }
}