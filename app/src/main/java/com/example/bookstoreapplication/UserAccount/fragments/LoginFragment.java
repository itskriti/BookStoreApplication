package com.example.bookstoreapplication.UserAccount.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bookstoreapplication.MainActivity;
import com.example.bookstoreapplication.R;
import com.example.bookstoreapplication.api.ApiClient;
import com.example.bookstoreapplication.api.response.LoginResponse;
import com.example.bookstoreapplication.utils.SharedPrefUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment implements View.OnClickListener{
    EditText emailEt, passwordET;
    LinearLayout loginBtn;
    ProgressBar circularProgress;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailEt = view.findViewById(R.id.emailET);
        passwordET = view.findViewById(R.id.passwordET);
        loginBtn = view.findViewById(R.id.loginLL);
        circularProgress = view.findViewById(R.id.circularProgress);
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == loginBtn){
            String email, password;
            email = emailEt.getText().toString();
            password = passwordET.getText().toString();
            if(email.isEmpty() && password.isEmpty())
                Toast.makeText(getContext(), "Email or password is Empty!", Toast.LENGTH_LONG).show();
            else{
                toggleLoading(true);
                System.out.println("22222222222222 Email is: " + email);
                System.out.println("22222222222222 Password is: " + password);

                Call<LoginResponse> loginResponseCall = ApiClient.getClient().login(email, password);
                loginResponseCall.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        toggleLoading(false);
                        LoginResponse loginResponse = response.body();
                        if(response.isSuccessful()){
                            if(loginResponse.getError()){
                                System.out.println("222222221222222222222 my error  is: "+ loginResponse.getError());
                                Toast.makeText(getActivity(), "Incorrect password or incorrect email", Toast.LENGTH_SHORT).show();



                            }else {
                               Toast.makeText(getActivity(), "Welcome", Toast.LENGTH_LONG).show();
                                System.out.println("222222221222222222222 my api key is: "+ loginResponse.getApiKey());
                                System.out.println("22222222222222 email   is: " + loginResponse.getEmail());
                                System.out.println("22222222222222 created at   is: " + loginResponse.getCreatedAt());
                                SharedPrefUtils.setBoolean(getActivity(), getString(R.string.isLogged), true);
                                SharedPrefUtils.setString(getActivity(), getString(R.string.name_key), loginResponse.getName());
                                SharedPrefUtils.setString(getActivity(), getString(R.string.email_id), loginResponse.getEmail());
                                SharedPrefUtils.setString(getActivity(), getString(R.string.created_key), loginResponse.getCreatedAt());
                                SharedPrefUtils.setString(getActivity(), getString(R.string.api_key), loginResponse.getApiKey());
                                getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                                getActivity().finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        toggleLoading(false);
                        Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }



        }

    }

    public boolean validate(){
        boolean validate = true;
        if (emailEt.getText().toString().isEmpty()
                || passwordET.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "None of the above fields can be empty", Toast.LENGTH_SHORT).show();
            validate = false;
        }

        return validate;
    }

    void toggleLoading(Boolean toogle){
        if (toogle)
            circularProgress.setVisibility(View.VISIBLE);
        else
            circularProgress.setVisibility(View.GONE);
    }
}