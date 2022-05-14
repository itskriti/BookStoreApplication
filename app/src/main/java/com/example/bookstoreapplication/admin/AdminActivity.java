package com.example.bookstoreapplication.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstoreapplication.R;
import com.example.bookstoreapplication.admin.addBook.AddBookActivity;
import com.example.bookstoreapplication.admin.addCategory.ListCategoryActivity;
import com.example.bookstoreapplication.admin.books.ListBookActivity;
import com.example.bookstoreapplication.api.ApiClient;
import com.example.bookstoreapplication.api.response.Dash;
import com.example.bookstoreapplication.api.response.DashResponse;
import com.example.bookstoreapplication.api.response.RegisterResponse;
import com.example.bookstoreapplication.orders.orderDetails.OrderDetailsActivity;
import com.example.bookstoreapplication.orders.orderDetails.OrderHistoryActivity;
import com.example.bookstoreapplication.utils.PermissionUtils;
import com.example.bookstoreapplication.utils.SharedPrefUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends AppCompatActivity {
    private static final int TAKE_PICTURE = 2;
    private static final int PICK_PICTURE = 1;
    private static final String TEMP_DIRECT = "/Book/Picture/.temp/";
    TextView pendingOrdersTV, totalOrdersTV, shippedOrdersTV, totalCategoriesTV, totalCustomersTV, totalProductsTV;
    LinearLayout addCategory, imageLayout, categoryList, productsLL, ordersLL;
    private Uri imageUri;
    String currentPhotoPath;
    ImageView selectedIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setTitle("Admin Area");
        findIds();
        getDash();
    }

    private void getDash(){
        String key = SharedPrefUtils.getString(this, getString(R.string.api_key));
        Call<DashResponse> addressResponseCall = ApiClient.getClient().getDash(key);
        addressResponseCall.enqueue(new Callback<DashResponse>() {
            @Override
            public void onResponse(Call<DashResponse> call, Response<DashResponse> response) {
                if(response.isSuccessful()){
                    setDash(response.body().getDash());
                }
            }

            @Override
            public void onFailure(Call<DashResponse> call, Throwable t) {

            }
        });
    }

    private void setDash(Dash dash){
        pendingOrdersTV.setText(dash.getPendingOrders().toString());
        totalCategoriesTV.setText(dash.getCategories().toString());
        totalCustomersTV.setText(dash.getCustomers().toString());
        totalOrdersTV.setText(dash.getProcessingOrders().toString());
        shippedOrdersTV.setText(dash.getShippedOrders().toString());
        totalProductsTV.setText(dash.getBooks().toString());
    }

    private void findIds(){
        pendingOrdersTV = findViewById(R.id.pendingOrdersTV);
        totalCategoriesTV = findViewById(R.id.totalCategoriesTV);
        totalCustomersTV = findViewById(R.id.totalCustomersTV);
        totalOrdersTV = findViewById(R.id.totalOrdersTV);
        shippedOrdersTV = findViewById(R.id.shippedOrdersTV);
        totalProductsTV = findViewById(R.id.totalProductsTV);
        addCategory = findViewById(R.id.addCategory);
        categoryList = findViewById(R.id.categoryList);
        productsLL = findViewById(R.id.productsLL);
        ordersLL = findViewById(R.id.ordersLL);
        setClickListeners();
    }

    private void setClickListeners(){
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddCategoryView();
            }
        });
        categoryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, ListCategoryActivity.class);
                startActivity(intent);
            }
        });
        productsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, ListBookActivity.class);
                startActivity(intent);
            }
        });
        ordersLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, OrderHistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openAddCategoryView(){

        LayoutInflater factory = LayoutInflater.from(this);
        View DialogView = factory.inflate(R.layout.custom_dialog_add_category, null);
        Dialog main_dialog = new Dialog(this, R.style.Base_Theme_AppCompat_Dialog);
        main_dialog.setContentView(DialogView);
        main_dialog.show();
        EditText name = (EditText) main_dialog.findViewById(R.id.catNameET);
        Button upload = (Button) main_dialog.findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!name.getText().toString().isEmpty() && currentPhotoPath != null) {
                    uploadCategory(new File(currentPhotoPath), name.getText().toString(), main_dialog);
                } else {
                    Toast.makeText(AdminActivity.this, "Please check image or Name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView cameraIV = (ImageView) main_dialog.findViewById(R.id.cameraIV);
        ImageView galleryIv = (ImageView) main_dialog.findViewById(R.id.galleryIV);
        selectedIV = (ImageView) main_dialog.findViewById(R.id.selectedIV);
        imageLayout = (LinearLayout) main_dialog.findViewById(R.id.imageLayout);
        cameraIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = null;
                try {
                    file = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (PermissionUtils.isCameraPermissionGranted(AdminActivity.this, "", 1)) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (file != null) {
                        Uri photoURI = FileProvider.getUriForFile(AdminActivity.this,
                                "com.example.android.filesprovider",
                                file);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(intent, TAKE_PICTURE);
                    }

                }
            }
        });
        galleryIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionUtils.isStoragePermissionGranted(AdminActivity.this, "", PICK_PICTURE)) {
                    Intent chooseFile = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(chooseFile, PICK_PICTURE);
                }
            }
        });
        main_dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PICTURE){
            if(requestCode == Activity.RESULT_OK){
                File file = new File(currentPhotoPath);
                Uri contentUri = Uri.fromFile(file);
                selectedIV.setImageURI(contentUri);
                setCategoryImage();
            }
        } else if(requestCode == PICK_PICTURE){
            if(requestCode == Activity.RESULT_OK){
                setCategoryImage();
                selectedIV.setImageURI(data.getData());
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor cu = getContentResolver().query(data.getData(), filePath, null, null, null);
                cu.moveToFirst();
                int columnIndex = cu.getColumnIndex(filePath[0]);
                String picturePAth = cu.getString(columnIndex);
                cu.close();
                currentPhotoPath = picturePAth;
            }
        }

    }


    public void setCategoryImage(){
        imageLayout.setVisibility(View.GONE);
        selectedIV.setVisibility(View.GONE);
    }

    private void uploadCategory(File f, String name, Dialog dialog){
        ProgressDialog progressDialog = ProgressDialog.show(this, "",
                "Uploading. Please wait...", false);
        String key = SharedPrefUtils.getString(this, getString(R.string.api_key));
        RequestBody catName = RequestBody.create(MediaType.parse("text/plain"), name);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", f.getName(), RequestBody.create(MediaType.parse("image/*"), f));
        Call<RegisterResponse> responseCall = ApiClient.getClient().uploadCategory(key, filePart, catName);
        responseCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    dialog.dismiss();
                    Toast.makeText(AdminActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();

                    Toast.makeText(AdminActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(AdminActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void addProduct(View view) {
        Intent intent = new Intent(this, AddBookActivity.class);
        startActivity(intent);
    }
}























