package com.example.bookstoreapplication.checkout.address;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.bookstoreapplication.R;
import com.example.bookstoreapplication.api.ApiClient;
import com.example.bookstoreapplication.api.response.Address;
import com.example.bookstoreapplication.api.response.AddressResponse;
import com.example.bookstoreapplication.utils.SharedPrefUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressActivity extends AppCompatActivity {
    RecyclerView addressRV;
    public static String ADDRESS_SELECTED_KEY = "DFa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        addressRV = findViewById(R.id.addressRV);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Select Address");
        getAddressOnline();
    }

    private void getAddressOnline() {
        String key = SharedPrefUtils.getString(this, "apk");
        Call<AddressResponse> addressResponseCall = ApiClient.getClient().getMyAddresses(key);
        addressResponseCall.enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                if (response.isSuccessful()) {
                    if(response.body().getAddresses().size() != 0) {
                        listAddress(response.body().getAddresses());
                    }else{
                        Toast.makeText(getApplicationContext(), "No data", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {

            }
        });
    }

    private void listAddress(List<Address> addresses) {
        addressRV.setHasFixedSize(true);
        addressRV.setLayoutManager(new LinearLayoutManager(this));
        AddressAdapter addressAdapter = new AddressAdapter(addresses, this);
        addressAdapter.setOnAddressItemClickListener(new AddressAdapter.OnAddressItemClickListener() {
            @Override
            public void onAddressClick(int position, Address address) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(ADDRESS_SELECTED_KEY, address);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        addressRV.setAdapter(addressAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}