package com.example.bookstoreapplication.home.fragments.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.example.bookstoreapplication.R;
import com.example.bookstoreapplication.api.ApiClient;
import com.example.bookstoreapplication.api.response.AllBookResponse;
import com.example.bookstoreapplication.api.response.Book;
import com.example.bookstoreapplication.home.fragments.home.adapters.SearchBookAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    android.widget.SearchView searchView;
    RecyclerView product_RV;
    SearchBookAdapter searchAdapter;
    LinearLayout SearchLL;
    ImageView backIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);
//        getSupportActionBar().hide();
        searchView = findViewById(R.id.searchView);
        product_RV = findViewById(R.id.book_RV);
        SearchLL = findViewById(R.id.searchLayout);
        backIV = findViewById(R.id.backIV);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        searchListener();
        Call<AllBookResponse> searchProductResponseCall = ApiClient.getClient().getAllBooks();
        searchProductResponseCall.enqueue(new Callback<AllBookResponse>() {
            @Override
            public void onResponse(Call<AllBookResponse> call, Response<AllBookResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().getError()) {
                        setSearchView(response.body().getBooks());
                    }
                }
            }

            @Override
            public void onFailure(Call<AllBookResponse> call, Throwable t) {

            }
        });


    }

    private void searchListener() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (searchAdapter != null) {
                    searchAdapter.getFilter().filter(s);
                }
//                if(s.length()>0){
//                    product_RV.setVisibility(View.VISIBLE);
//                    SearchLL.setVisibility(View.GONE);
//                }else{
//                    product_RV.setVisibility(View.GONE);
//                    SearchLL.setVisibility(View.VISIBLE);
//                }
                return false;
            }
        });

    }

    private void setSearchView(List<Book> books) {
        product_RV.setHasFixedSize(true);
        product_RV.setLayoutManager(new GridLayoutManager(SearchActivity.this, 1));
        searchAdapter = new SearchBookAdapter(books, this);
        product_RV.setAdapter(searchAdapter);
    }
}
