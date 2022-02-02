package com.example.bookstoreapplication.UserAccount.fragments;

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

import com.example.bookstoreapplication.R;
import com.example.bookstoreapplication.api.ApiClient;
import com.example.bookstoreapplication.api.response.RegisterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterFragment extends Fragment {
    EditText emailET, passwordET, confirmPasswordET, nameET;
    LinearLayout registerLL;
    ProgressBar circularProgress;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailET = view.findViewById(R.id.emailET);
        passwordET = view.findViewById(R.id.passwordET);
        nameET = view.findViewById(R.id.nameET);
        confirmPasswordET = view.findViewById(R.id.confirmPasswordET);
        circularProgress = view.findViewById(R.id.circularProgress);
        registerLL = view.findViewById(R.id.registerLL);
        registerLL.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(validate()){
                    toggleLoading(true);
                    Call<RegisterResponse> registerCall = ApiClient.getClient().register(nameET.getText().toString(), emailET.getText().toString(), passwordET.getText().toString());
                    registerCall.enqueue(new Callback<RegisterResponse>() {
                        @Override
                        public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                            RegisterResponse registerResponse = response.body();
                            toggleLoading(false);
                            if(response.isSuccessful()){
                                Toast.makeText(getActivity(), registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                if(!registerResponse.getError()){
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.formContainerFL, new LoginFragment()).commit();

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<RegisterResponse> call, Throwable t) {
                            toggleLoading(false);
                            Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

    }

    void toggleLoading(Boolean toogle){
        if (toogle)
            circularProgress.setVisibility(View.VISIBLE);
        else
            circularProgress.setVisibility(View.GONE);
    }

    public boolean validate(){
        boolean validate = true;
        if (emailET.getText().toString().isEmpty()
                || passwordET.getText().toString().isEmpty()
                || confirmPasswordET.getText().toString().isEmpty()
                || nameET.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "None of the above fields can be empty", Toast.LENGTH_SHORT).show();
            validate = false;
        } else if (!passwordET.getText().toString().equals(confirmPasswordET.getText().toString())) {
            confirmPasswordET.setError("Passoerd doesnot match please check!!");
            validate = false;

        }

        return validate;
    }
}