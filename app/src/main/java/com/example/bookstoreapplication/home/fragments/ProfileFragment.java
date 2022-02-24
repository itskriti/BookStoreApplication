package com.example.bookstoreapplication.home.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bookstoreapplication.R;
import com.example.bookstoreapplication.UserAccount.UserAccountActivity;
import com.example.bookstoreapplication.admin.AdminActivity;
import com.example.bookstoreapplication.utils.SharedPrefUtils;


public class ProfileFragment extends Fragment {
    LinearLayout logoutButton;
    TextView editProfileTV, myBooksTV, orderHistoryTV, settingTV, customerTV, adminAreaTV;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editProfileTV = view.findViewById(R.id.editProfileTV);
        myBooksTV = view.findViewById(R.id.myBooksTV);
        orderHistoryTV = view.findViewById(R.id.orderHistoryTV);
        settingTV = view.findViewById(R.id.settingTV);
        customerTV = view.findViewById(R.id.customerTV);
        logoutButton = view.findViewById(R.id.logout);
        adminAreaTV = view.findViewById(R.id.adminAreaTV);
        checkAdmin();
        setClickListeners();

    }

    private void checkAdmin(){
        boolean is_staff = SharedPrefUtils.getBool(getActivity(),getString(R.string.staff_key), false);
        if(is_staff)
            adminAreaTV.setVisibility(View.VISIBLE);
    }

    private void setClickListeners(){
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefUtils.setBoolean(getActivity(), getString(R.string.isLogged), false);
                Intent intent = new Intent(getContext(), UserAccountActivity.class);
                startActivity(intent);
            }
        });

        editProfileTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), )
            }
        });

        myBooksTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        orderHistoryTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        settingTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        customerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        adminAreaTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(), AdminActivity.class);
                startActivity(intent);
            }
        });

    }

}