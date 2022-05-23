package com.example.bookstoreapplication.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.bookstoreapplication.MainActivity;
import com.example.bookstoreapplication.R;
import com.example.bookstoreapplication.api.ApiClient;
import com.example.bookstoreapplication.api.response.ProfileResponse;
import com.example.bookstoreapplication.api.response.RegisterResponse;
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

public class UserProfileActivity extends AppCompatActivity {
    private static final int PICK_PICTURE = 1;
    private static final int TAKE_PICTURE = 2;
    String currentPhotoPath;
    TextView nameTV, emailTV;
    ImageView backIV, changeProfileIV, selectedIV;
    LinearLayout saveLL, imageLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        nameTV = findViewById(R.id.nameTV);
        emailTV = findViewById(R.id.emailTV);
        saveLL = findViewById(R.id.saveLL);
        backIV = findViewById(R.id.backIV);
        changeProfileIV = findViewById(R.id.changeProfileIV);

        nameTV.setText(SharedPrefUtils.getString(UserProfileActivity.this, getString(R.string.name_key)));
        emailTV.setText(SharedPrefUtils.getString(UserProfileActivity.this, getString(R.string.email_key)));

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        changeProfileIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddProfileView();
            }
        });

        saveLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    String key = SharedPrefUtils.getString(UserProfileActivity.this, getString(R.string.api_key));
                    Call<ProfileResponse>  profileResponseCall = ApiClient.getClient().updateProfile(key, nameTV.getText().toString(), emailTV.getText().toString());
                    profileResponseCall.enqueue(new Callback<ProfileResponse>() {
                        @Override
                        public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                            if(response.isSuccessful()){
                                if(!response.body().getError()){
                                    SharedPrefUtils.setString(UserProfileActivity.this, getString(R.string.name_key), nameTV.getText().toString());
                                    SharedPrefUtils.setString(UserProfileActivity.this, getString(R.string.email_id), emailTV.getText().toString());
                                    Toast.makeText(UserProfileActivity.this, "Profi;e Updated Successfully", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ProfileResponse> call, Throwable t) {

                        }
                    });

                }

            }
        });

    }

    private void openAddProfileView(){
        LayoutInflater factory = LayoutInflater.from(UserProfileActivity.this);
        View DialogView = factory.inflate(R.layout.custom_dialog_add_category, null);
        Dialog main_dialog = new Dialog(UserProfileActivity.this, R.style.Base_Theme_AppCompat_Dialog);
        main_dialog.setContentView(DialogView);
        main_dialog.show();
        Button upload = (Button) main_dialog.findViewById(R.id.upload);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPhotoPath != null) {
                    uploadProfile(new File(currentPhotoPath), main_dialog);
                } else {
                    Toast.makeText(UserProfileActivity.this, "Please check image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        LinearLayout cameraLL = (LinearLayout) main_dialog.findViewById(R.id.cameraLL);
        LinearLayout galleryLL = (LinearLayout) main_dialog.findViewById(R.id.galleryLL);
        selectedIV = (ImageView) main_dialog.findViewById(R.id.selectedIV);
        imageLayout = (LinearLayout) main_dialog.findViewById(R.id.imageLayout);

        cameraLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = null;
                try {
                    file = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (PermissionUtils.isCameraPermissionGranted(UserProfileActivity.this, "", 1)) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (file != null) {
                        Uri photoURI = FileProvider.getUriForFile(UserProfileActivity.this,
                                "com.example.android.fileprovider",
                                file);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(intent, TAKE_PICTURE);
                    }

                }

            }
        });
        galleryLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionUtils.isStoragePermissionGranted(UserProfileActivity.this, "", PICK_PICTURE)) {
                    Intent chooseFile = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(chooseFile, PICK_PICTURE);
                }
            }
        });
        main_dialog.show();
    }


    public boolean validate() {
        if(emailTV.getText().toString().isEmpty() || nameTV.getText().toString().isEmpty()){
            Toast.makeText(UserProfileActivity.this, "None of the above fields can be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Patterns.EMAIL_ADDRESS.matcher(emailTV.getText().toString()).matches()){
            return true;
        } else{
            Toast.makeText(this, "Enter valid email address !!", Toast.LENGTH_SHORT).show();
        } return false;

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

    private void uploadProfile(File file, Dialog dialog){
        ProgressDialog progressDialog = ProgressDialog.show(UserProfileActivity.this, "",
                "Uploading. Please wait...", false);
        String key = SharedPrefUtils.getString(UserProfileActivity.this, getString(R.string.api_key));
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        Call<RegisterResponse> responseCall = ApiClient.getClient().uploadProfile(key, filePart);
        responseCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    dialog.dismiss();
                    Toast.makeText(UserProfileActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                } else {
                    progressDialog.dismiss();

                    Toast.makeText(UserProfileActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(UserProfileActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile( imageFileName,  /* prefix */".jpg", /* suffix */storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}




