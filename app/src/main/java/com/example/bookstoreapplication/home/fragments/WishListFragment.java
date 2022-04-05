package com.example.bookstoreapplication.home.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.bookstoreapplication.R;
import com.example.bookstoreapplication.api.ApiClient;
import com.example.bookstoreapplication.api.response.AllBookResponse;
import com.example.bookstoreapplication.api.response.Book;
import com.example.bookstoreapplication.api.response.RegisterResponse;
import com.example.bookstoreapplication.home.fragments.home.adapters.ShopAdapter;
import com.example.bookstoreapplication.home.fragments.home.adapters.WishlistAdapter;
import com.example.bookstoreapplication.utils.SharedPrefUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WishListFragment extends Fragment {
    RecyclerView allBookRV;
    List <Book> books;
    SwipeRefreshLayout swipeRefresh;
    LinearLayout addToCartLL;
    AllBookResponse allBookResponse;
    ShopAdapter shopAdapter;
    ImageView emptyWishlistIV;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wish_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        allBookRV = view.findViewById((R.id.allBooksRV));
        addToCartLL = view.findViewById(R.id.addToCartLL);
        emptyWishlistIV = view.findViewById(R.id.emptyWishlistIV);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        addToCartLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allBookResponse != null && allBookResponse.getBooks().size() > 0) {
                    Intent intent = new Intent(getContext(), CartFragment.class);
//                    intent.putExtra(CheckOutActivity.CHECK_OUT_PRODUCTS, allBookResponse);
                    getContext().startActivity(intent);
                }
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(true);
                getWishListItems();
            }
        });

        getWishListItems();

    }

    public void getWishListItems(){
        String key = SharedPrefUtils.getString(getActivity(), getString(R.string.api_key));
        Call<AllBookResponse> cartItemsCall = ApiClient.getClient().getMyWishList(key);
        cartItemsCall.enqueue(new Callback<AllBookResponse>() {
            @Override
            public void onResponse(Call<AllBookResponse> call, Response<AllBookResponse> response) {
                swipeRefresh.setRefreshing(false);
                if(response.isSuccessful()){
                    if (!response.body().getError()) {
                        if (response.body().getBooks().size() == 0) {
                            showEmptyLayout();
                        } else {
                            emptyWishlistIV.setVisibility(View.GONE);
                            allBookResponse = response.body();
                            books = response.body().getBooks();
                            loadWishList();
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<AllBookResponse> call, Throwable t) {
                swipeRefresh.setRefreshing(false);

            }
        });
    }

    private void showEmptyLayout(){
        emptyWishlistIV.setVisibility(View.VISIBLE);

    }

    private void loadWishList() {
        allBookRV.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        allBookRV.setLayoutManager(layoutManager);
        WishlistAdapter wishlistAdapter = new WishlistAdapter(books, getContext());
        wishlistAdapter.setWishCartItemClick(new WishlistAdapter.WishlistCartItemClick() {
            @Override
            public void onRemoveCart(int position) {
                String key = SharedPrefUtils.getString(getActivity(), "apk");
                Call<RegisterResponse> removeCartCall = ApiClient.getClient().deleteFromCart(key, books.get(position).getCartID());
                removeCartCall.enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                        if (response.isSuccessful()) {
                            if (!response.body().getError()) {
                                books.remove(books.get(position));
                                wishlistAdapter.notifyItemChanged(position);
                                Toast.makeText(getContext(), "Cart Item successfully deleted", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    }
                });
            }
        });
        allBookRV.setAdapter(wishlistAdapter);
    }





    @Override
    public void onResume() {
        super.onResume();
        getWishListItems();
    }
}