package com.example.bookstoreapplication.api;

import com.example.bookstoreapplication.api.response.AddressResponse;
import com.example.bookstoreapplication.api.response.AllBookResponse;
import com.example.bookstoreapplication.api.response.Category;
import com.example.bookstoreapplication.api.response.CategoryResponse;
import com.example.bookstoreapplication.api.response.DashResponse;
import com.example.bookstoreapplication.api.response.LoginResponse;
import com.example.bookstoreapplication.api.response.OrderHistoryResponse;
import com.example.bookstoreapplication.api.response.RegisterResponse;
import com.example.bookstoreapplication.api.response.SingleBookResponse;
import com.example.bookstoreapplication.api.response.Slider;
import com.example.bookstoreapplication.api.response.SliderResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
    Call<RegisterResponse> addToCart(@Header("api_key") String apikey, @Field("b_id") int p, @Field("quantity") int q);

    @FormUrlEncoded
    @POST("/api/v1/wishlist")
    Call<AllBookResponse> addToWishlist(@Header("api_key") String apikey, @Field("b_id") int p, @Field("quantity") int q);

    @FormUrlEncoded
    @POST("/api/v1/order")
    Call<RegisterResponse> order(@Header("api_key") String apikey,
                                 @Field("p_type") int p_type,
                                 @Field("address_id") int address_id,
                                 @Field("payment_refrence") String paymentRefrence);


    @GET("/api/v1/order")
    Call<OrderHistoryResponse> orderHistory(@Header("api_key") String apikey
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
    Call<AllBookResponse> getMyCart(@Header("api_key") String apikey);

    @GET("/api/v1/wishlist")
    Call<AllBookResponse> getMyWishList(@Header("api_key") String apikey);

    @DELETE("/api/v1/cart")
    Call<RegisterResponse> deleteFromCart(@Header("api_key") String apikey, @Query("c_id") int cartID);

    @DELETE("/api/v1/cart")
    Call<RegisterResponse> deleteFromWishlist(@Header("api_key") String apikey, @Query("w_id") int wishlistID);

    @GET("/api/v1/get-all-products")
    Call<SingleBookResponse> getBookById(@Query("id") int c_id);

    @GET("/api/v1/address")
    Call<AddressResponse> getMyAddresses(@Header("api_key") String apikey);

    @FormUrlEncoded
    @POST("/api/v1/address")
    Call<AddressResponse> addAddress(
            @Header("api_key") String apikey,
            @Field("city") String city,
            @Field("street") String street,
            @Field("province") String province,
            @Field("description") String description);

    @Multipart
    @POST("/api/v1/upload-category")
    Call<RegisterResponse> uploadCategory(
            @Header("api_key") String apikey,
            @Part MultipartBody.Part file,
            @Part("name") RequestBody name

    );

    @Multipart
    @POST("/api/v1/upload-product")
    Call<RegisterResponse> uploadBook(
            @Header("api_key") String apikey,
            @Part MultipartBody.Part[] files,
            @Part("name") RequestBody name,
            @Part("price") RequestBody price,
            @Part("description") RequestBody description,
            @Part("quantity") RequestBody quantity,
            @Part("discount_price") RequestBody discount_price,
            @Part("categories") RequestBody categories,
            @Part("author_name") RequestBody author_name

    );

    @GET("/api/v1/dash")
    Call<DashResponse> getDash(@Header("api_key") String apikey);

    @DELETE("/api/v1/category")
    Call<RegisterResponse> deleteCategory(@Header("api_key") String apikey, @Query("c_id") int id);

}
