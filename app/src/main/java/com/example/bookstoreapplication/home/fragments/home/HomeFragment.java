package com.example.bookstoreapplication.home.fragments.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstoreapplication.R;
import com.example.bookstoreapplication.api.ApiClient;
import com.example.bookstoreapplication.api.response.AllBookResponse;
import com.example.bookstoreapplication.api.response.Book;
import com.example.bookstoreapplication.api.response.Category;
import com.example.bookstoreapplication.api.response.CategoryResponse;
import com.example.bookstoreapplication.api.response.Slider;
import com.example.bookstoreapplication.api.response.SliderResponse;
import com.example.bookstoreapplication.categoryPage.CategoryActivity;
import com.example.bookstoreapplication.home.fragments.home.adapters.CategoryAdapter;
import com.example.bookstoreapplication.home.fragments.home.adapters.ShopAdapter;
import com.example.bookstoreapplication.home.fragments.home.adapters.SliderAdapter;
import com.example.bookstoreapplication.singleBookPage.SingleBookActivity;
import com.example.bookstoreapplication.utils.DataHolder;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    RecyclerView allHomeBookRV, categoryRV;
    ProgressBar loadingProgress;
    SliderView homeImageSlider;
    TextView showAllTV;
    LinearLayout searchLL;
    BottomNavigationView bottomNavigationView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void setBottomNavigationView(BottomNavigationView bottomNavigationView) {
        this.bottomNavigationView = bottomNavigationView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        allHomeBookRV = view.findViewById(R.id.allHomeBookRV);
        categoryRV = view.findViewById(R.id.categoryRV);
        loadingProgress = view.findViewById(R.id.loadingProgress);
        homeImageSlider = view.findViewById(R.id.homeImageSlider);
        showAllTV = view.findViewById(R.id.showAllTV);
        searchLL = view.findViewById(R.id.searchLL);
        serverCall();
        getCategoriesOnline();
        getSliders();
        setClickListeners();
    }

    private void setClickListeners(){
        showAllTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomNavigationView.setSelectedItemId(R.id.category);
            }
        });

        searchLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void getCategoriesOnline() {
        Call<CategoryResponse> categoryResponseCall = ApiClient.getClient().getCategories();
        categoryResponseCall.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().getError()) {
                        DataHolder.categories = response.body().getCategories();
                        showCategories(response.body().getCategories());

                    }
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {

            }
        });


    }

    private void showCategories(List<Category> categories) {
        List<Category> temp;
        if (categories.size() > 8) {
            temp = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                temp.add(categories.get(categories.size() - i - 1));
            }
        } else {
            temp = categories;
        }
        categoryRV.setHasFixedSize(true);
        categoryRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        CategoryAdapter categoryAdapter = new CategoryAdapter(temp, getContext(), true);
        categoryRV.setAdapter(categoryAdapter);

    }

    private void serverCall() {
        toggleLoading(true);
        Call<AllBookResponse> allBookResponseCall = ApiClient.getClient().getAllBooks();
        allBookResponseCall.enqueue(new Callback<AllBookResponse>() {
            @Override
            public void onResponse(Call<AllBookResponse> call, Response<AllBookResponse> response) {
                toggleLoading(false);
                setBookRecyclerView(response.body().getBooks());
            }

            @Override
            public void onFailure(Call<AllBookResponse> call, Throwable t) {
                toggleLoading(false);
                Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void getSliders(){
        Call<SliderResponse> sliderResponseCall = ApiClient.getClient().getSliders();
        sliderResponseCall.enqueue(new Callback<SliderResponse>() {
            @Override
            public void onResponse(Call<SliderResponse> call, Response<SliderResponse> response) {
                if(response.isSuccessful()) {
                    setSliders(response.body().getSliders());
                }
            }

            @Override
            public void onFailure(Call<SliderResponse> call, Throwable t) {

            }
        });
    }

    private void setSliders(List<Slider> sliders){
        SliderAdapter sliderAdapter = new SliderAdapter(sliders, getContext(), true);
        sliderAdapter.setClickLister(new SliderAdapter.OnSliderClickLister() {
            @Override
            public void onSliderClick(int position, Slider slider) {
//                Toast.makeText(getContext(), "from home ", Toast.LENGTH_SHORT).show();

                if(slider.getType() == 1){
                    Intent intent = new Intent(getContext(), SingleBookActivity.class);
                    intent.putExtra(SingleBookActivity.SINGLE_DATA_KEY, slider.getRelatedId());
                    getContext().startActivity(intent);
                } else if(slider.getType() == 2){
                    Intent cat = new Intent(getContext(), CategoryActivity.class);
                    Category category = new Category();
                    category.setId(slider.getRelatedId());
                    category.setName(slider.getDesc());
                    cat.putExtra(CategoryActivity.CATEGORY_DATA_KEY, category);
                    getContext().startActivity(cat);
                }
            }
        });

        homeImageSlider.setSliderAdapter(sliderAdapter);
        homeImageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM);
        homeImageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        homeImageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        homeImageSlider.setIndicatorUnselectedColor(Color.GRAY);
        homeImageSlider.setScrollTimeInSec(4);
        homeImageSlider.startAutoCycle();

    }


    private void setBookRecyclerView(List<Book> books){
        allHomeBookRV.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        allHomeBookRV.setLayoutManager(layoutManager);
        ShopAdapter shopAdapter = new ShopAdapter(books, getContext(), false);
        allHomeBookRV.setAdapter(shopAdapter);

    }

    void toggleLoading(boolean toggle) {
        if (toggle)
            loadingProgress.setVisibility(View.VISIBLE);
        else
            loadingProgress.setVisibility(View.GONE);
    }

}