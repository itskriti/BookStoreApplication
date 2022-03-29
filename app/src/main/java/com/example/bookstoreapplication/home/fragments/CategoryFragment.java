package com.example.bookstoreapplication.home.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookstoreapplication.R;
import com.example.bookstoreapplication.api.response.Category;
import com.example.bookstoreapplication.home.fragments.home.adapters.CategoryAdapter;
import com.example.bookstoreapplication.utils.DataHolder;

import java.util.List;


public class CategoryFragment extends Fragment {
    RecyclerView fullCatRV;
    SwipeRefreshLayout swipeRefresh;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fullCatRV = view.findViewById(R.id.fullCatRV);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        if(DataHolder.categories != null){
            showCategoryView(DataHolder.categories);

        }

    }

    private void showCategoryView(List<Category> categories) {
        fullCatRV.setHasFixedSize(true);
        fullCatRV.setLayoutManager(new GridLayoutManager(getContext(), 4));
        CategoryAdapter categoryAdapter = new CategoryAdapter(categories, getContext(), true, false, null);
        fullCatRV.setAdapter(categoryAdapter);

    }
}