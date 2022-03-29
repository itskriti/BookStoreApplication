package com.example.bookstoreapplication.admin.addBook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.bookstoreapplication.R;
import com.example.bookstoreapplication.api.ApiClient;
import com.example.bookstoreapplication.api.response.Category;
import com.example.bookstoreapplication.api.response.CategoryResponse;
import com.example.bookstoreapplication.home.fragments.home.adapters.CategoryAdapter;
import com.example.bookstoreapplication.utils.DataHolder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectCategoryActivity extends AppCompatActivity {
    RecyclerView fullCatRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("All Categories");
        setContentView(R.layout.activity_list_category);
        fullCatRV = findViewById(R.id.fullCatRV);
        getOnline();
    }

    private void getOnline(){
        Call<CategoryResponse> getCategories = ApiClient.getClient().getCategories();
        getCategories.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().getError()) {
                        DataHolder.categories = response.body().getCategories();
                        showCategoryView(response.body().getCategories());

                    }
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {

            }
        });
    }

    private void showCategoryView(List<Category> categories){
        fullCatRV.setHasFixedSize(true);
        CategoryAdapter categoryAdapter = new CategoryAdapter(categories, this, false, true, this);
        fullCatRV.setLayoutManager(new GridLayoutManager(this, 1));
        fullCatRV.setAdapter(categoryAdapter);

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