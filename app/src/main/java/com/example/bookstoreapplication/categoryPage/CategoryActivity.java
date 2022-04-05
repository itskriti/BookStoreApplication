package com.example.bookstoreapplication.categoryPage;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstoreapplication.R;
import com.example.bookstoreapplication.api.ApiClient;
import com.example.bookstoreapplication.api.response.AllBookResponse;
import com.example.bookstoreapplication.api.response.Book;
import com.example.bookstoreapplication.api.response.Category;
import com.example.bookstoreapplication.home.fragments.home.adapters.ShopAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity {
    public static String CATEGORY_DATA_KEY = "cdk";
    Category category;
    RecyclerView allBookProductRV;
    ProgressBar loadingProgress;
    ImageView emptyIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        allBookProductRV = findViewById(R.id.allBooksRV);
        loadingProgress = findViewById(R.id.loadingProgress);
        emptyIV = findViewById(R.id.emptyIV);
        if(getIntent().getSerializableExtra(CATEGORY_DATA_KEY) == null)
            finish();
        category = (Category) getIntent().getSerializableExtra(CATEGORY_DATA_KEY);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(category.getName());
        getCategoryOnline();

    }



    private void getCategoryOnline() {
        toggleLoading(true);
        Call<AllBookResponse> getBooksByCategory = ApiClient.getClient().getBooksByCategory(category.getId());
        getBooksByCategory.enqueue(new Callback<AllBookResponse>() {
            @Override
            public void onResponse(Call<AllBookResponse> call, Response<AllBookResponse> response) {

                if (response.isSuccessful()){
                    toggleLoading(false);
                    if(!response.body().getError()){
                        if(response.body().getBooks().size() == 0) {
                            showEmptyLayout();
                        }else{
                            shoCategoriesBooks(response.body().getBooks());
                        }
                    }
                }
            }


            @Override
            public void onFailure(Call<AllBookResponse> call, Throwable t) {
                toggleLoading(false);
                Toast.makeText(CategoryActivity.this, "An Unknown Error Occurred !!!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showEmptyLayout() {
        emptyIV.setVisibility(View.VISIBLE);
    }



    //for back button
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

    private void shoCategoriesBooks(List<Book> books) {
        allBookProductRV.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        allBookProductRV.setLayoutManager(layoutManager);
        ShopAdapter shopAdapter = new ShopAdapter(books, this, false, false);
        allBookProductRV.setAdapter(shopAdapter);
    }

    private void toggleLoading(Boolean toggle){
        if(toggle)
            loadingProgress.setVisibility(View.VISIBLE);
        else
            loadingProgress.setVisibility(View.GONE);
    }



}