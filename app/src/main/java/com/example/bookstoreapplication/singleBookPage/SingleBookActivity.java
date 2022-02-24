package com.example.bookstoreapplication.singleBookPage;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstoreapplication.R;
import com.example.bookstoreapplication.api.ApiClient;
import com.example.bookstoreapplication.api.response.Book;
import com.example.bookstoreapplication.api.response.RegisterResponse;
import com.example.bookstoreapplication.api.response.SingleBookResponse;
import com.example.bookstoreapplication.api.response.Slider;
import com.example.bookstoreapplication.home.fragments.home.adapters.SliderAdapter;
import com.example.bookstoreapplication.utils.SharedPrefUtils;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleBookActivity extends AppCompatActivity {
    public static String key = "pkey";
    public static String SINGLE_DATA_KEY = "sds";
    Book book;
    ImageView backIV, plusIV, minusIV, wishListIV;
    SliderView imageSlider;
    TextView name, price, desc, quantityTV, oldPrice;
    ProgressBar addingCartPR;
    LinearLayout addToCartLL;
    int quantity = 1;
    boolean isAdding = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_book);
        imageSlider = findViewById(R.id.imageSlider);
        name = findViewById(R.id.bookNameTV);
        price = findViewById(R.id.bookPriceTV);
        oldPrice = findViewById(R.id.bookOldPriceTV);
        desc = findViewById(R.id.decTV);
        addToCartLL = findViewById(R.id.addToCartLL);
        addingCartPR = findViewById(R.id.addingCartPR);
        backIV = findViewById(R.id.backIV);
        plusIV = findViewById(R.id.plusIV);
        minusIV = findViewById(R.id.minusIV);
        wishListIV = findViewById(R.id.wishlistIV);
        quantityTV = findViewById(R.id.quantityTV);
        setOnClickListeners();
        if (getIntent().getSerializableExtra(key) != null) {
            book = (Book) getIntent().getSerializableExtra(key);
            setBook(book);
        } else if(getIntent().getSerializableExtra(SINGLE_DATA_KEY) != null)
            getBookOnline(getIntent().getIntExtra(SINGLE_DATA_KEY,  1));
        setOnClickListeners();

    }

    private void getBookOnline(int intExtra){
        Call<SingleBookResponse> bookResponseCall = ApiClient.getClient().getBookById(intExtra);
        bookResponseCall.enqueue(new Callback<SingleBookResponse>() {
            @Override
            public void onResponse(Call<SingleBookResponse> call, Response<SingleBookResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().getError()) {
                        book = response.body().getBook();
                        setBook(book);
                    }

                }
            }

            @Override
            public void onFailure(Call<SingleBookResponse> call, Throwable t) {

            }
        });
    }

    private void setBook(Book book) {
        setSliders(book.getImages());
        name.setText(book.getName());
        if (book.getDiscountPrice() == 0 || book.getDiscountPrice() == null) {
            price.setText("Rs. " + book.getPrice());
            oldPrice.setVisibility(View.INVISIBLE);
        } else {
            price.setText("Rs. " + book.getDiscountPrice());
            oldPrice.setText("Rs. " + book.getPrice());
        }
        desc.setText(book.getDescription());
    }

    private void setSliders(List<String> images) {
        List<Slider> sliders = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            Slider slider = new Slider();
            slider.setImage(images.get(i));
            sliders.add(slider);
        }

        SliderAdapter sliderAdapter = new SliderAdapter(sliders, this, false);
        sliderAdapter.setClickLister(new SliderAdapter.OnSliderClickLister() {
            @Override
            public void onSliderClick(int position, Slider slider) {

            }
        });


        imageSlider.setSliderAdapter(sliderAdapter);
        imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        imageSlider.setIndicatorUnselectedColor(Color.GRAY);


    }

    private void setOnClickListeners() {
        backIV.setOnClickListener(v -> finish());
        plusIV.setOnClickListener(v -> {

            if (quantity > 9)
                Toast.makeText(this, "You can only buy 10 items at once", Toast.LENGTH_SHORT).show();
            else
                quantity++;

            setQuantity();
        });
        minusIV.setOnClickListener(v -> {

            if (quantity < 2)
                Toast.makeText(this, "Quantity should be atleast 1", Toast.LENGTH_SHORT).show();
            else
                quantity--;
            setQuantity();
        });
        wishListIV.setOnClickListener(view -> {
            if (!isAdding){
                isAdding = true;
                addingToggle(true);
                String key = SharedPrefUtils.getString(this, "apk");

                Call<RegisterResponse> wishCall = ApiClient.getClient().addToCart(key, book.getId(), quantity);
                
            }
        });
        addToCartLL.setOnClickListener(v -> {

            if (!isAdding) {
                isAdding = true;
                addingToggle(true);
                String key = SharedPrefUtils.getString(this, getString(R.string.api_key));


                Call<RegisterResponse> cartCall = ApiClient.getClient().addToCart(key, book.getId(), quantity);
                cartCall.enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        addingToggle(false);
                        isAdding = false;

                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        addingToggle(false);
                        isAdding = false;
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "Adding Already!!", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void addingToggle(boolean b) {
        if (b)
            addingCartPR.setVisibility(View.VISIBLE);
        else {
            addingCartPR.setVisibility(View.GONE);
        }
    }

    private void setQuantity() {
        quantityTV.setText(quantity + "");
    }


}