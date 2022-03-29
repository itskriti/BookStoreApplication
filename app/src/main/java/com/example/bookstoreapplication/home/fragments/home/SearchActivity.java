package com.example.bookstoreapplication.home.fragments.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
    SearchView searchView;
    RecyclerView book_RV;
    SearchBookAdapter searchBookAdapter;
    ImageView backIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchView = findViewById(R.id.searchView);
        book_RV = findViewById(R.id.book_RV);
        backIV = findViewById(R.id.backIV);

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        searchListener();

        Call<AllBookResponse> searchResponse = ApiClient.getClient().getAllBooks();
        searchResponse.enqueue(new Callback<AllBookResponse>() {
            @Override
            public void onResponse(Call<AllBookResponse> call, Response<AllBookResponse> response) {
                if(response.isSuccessful()){
                    if(!response.body().getError()){
                        setSearchView(response.body().getBooks());
                    }
                }
            }

            @Override
            public void onFailure(Call<AllBookResponse> call, Throwable t) {

            }
        });
    }

    private void searchListener(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchBookAdapter.getFilter().filter(s);
                if(s.length() > 0){
                    book_RV.setVisibility(View.VISIBLE);
                } else {
                    book_RV.setVisibility(View.GONE);
                }
                return false;
            }
        });





    }

    private void setSearchView(List<Book> bookList){
        book_RV.setHasFixedSize(true);
        book_RV.setLayoutManager(new GridLayoutManager(SearchActivity.this, 1));
        searchBookAdapter = new SearchBookAdapter(bookList, this);
        book_RV.setAdapter(searchBookAdapter);
    }
}