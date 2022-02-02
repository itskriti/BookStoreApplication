package com.example.bookstoreapplication.api;

import com.example.bookstoreapplication.api.response.AddressResponse;
import com.example.bookstoreapplication.api.response.AllBookResponse;
import com.example.bookstoreapplication.api.response.Category;
import com.example.bookstoreapplication.api.response.CategoryResponse;
import com.example.bookstoreapplication.api.response.LoginResponse;
import com.example.bookstoreapplication.api.response.OrderHistoryResponse;
import com.example.bookstoreapplication.api.response.RegisterResponse;
import com.example.bookstoreapplication.api.response.SingleBookResponse;
import com.example.bookstoreapplication.api.response.Slider;
import com.example.bookstoreapplication.api.response.SliderResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @FormUrlEncoded
    @POST("/api/v1/login")
    Call<LoginResponse> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("/api/v1/register")
    Call<RegisterResponse> register(@Field("name") String name, @Field("email") String email, @Field("password") String password);


    @FormUrlEncoded
    @POST("/api/v1/cart")
    Call<RegisterResponse> addToCart(@Header("Apikey") String apikey, @Field("b_id") int p, @Field("quantity") int q);

    @FormUrlEncoded
    @POST("/api/v1/order")
    Call<RegisterResponse> order(@Header("Apikey") String apikey,
                                 @Field("p_type") int p_type,
                                 @Field("address_id") int address_id,
                                 @Field("payment_refrence") String paymentRefrence);


    @GET("/api/v1/order")
    Call<OrderHistoryResponse> orderHistory(@Header("Apikey") String apikey
    );

    @GET("api/v1/get-all-books")
    Call<AllBookResponse> getAllBooks();

    @GET("api/v1/get-categories")
    Call<CategoryResponse> getCategories();

    @GET("api/v1/slider")
    Call<SliderResponse> getSliders();

    @GET("/api/v1/get-books-by-category")
    Call<AllBookResponse> getBooksByCategory(@Query("c_id") int c_id);



    @GET("/api/v1/cart")
    Call<AllBookResponse> getMyCart(@Header("Apikey") String apikey);

    @DELETE("/api/v1/cart")
    Call<RegisterResponse> deleteFromCart(@Header("Apikey") String apikey, @Query("c_id") int cartID);


    @GET("/api/v1/address")
    Call<AddressResponse> getMyAddresses(@Header("Apikey") String apikey);

}
