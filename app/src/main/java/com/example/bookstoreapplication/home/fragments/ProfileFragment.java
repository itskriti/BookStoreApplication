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
import android.widget.Toast;

import com.example.bookstoreapplication.R;
import com.example.bookstoreapplication.UserAccount.UserAccountActivity;
import com.example.bookstoreapplication.admin.AdminActivity;
import com.example.bookstoreapplication.orders.orderDetails.OrderHistoryActivity;
import com.example.bookstoreapplication.profile.ChangePasswordActivity;
import com.example.bookstoreapplication.profile.CustomerServiceActivity;
import com.example.bookstoreapplication.profile.UserProfileActivity;
import com.example.bookstoreapplication.utils.SharedPrefUtils;


public class ProfileFragment extends Fragment {
    LinearLayout logoutButton;
    TextView  settingTV, customerTV, adminAreaTV;
    LinearLayout ordersLL, editProfileLL, customerLL, settingLL;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editProfileLL = view.findViewById(R.id.editProfileLL);

        ordersLL = view.findViewById(R.id.ordersHistoryLL);
//        orderHistoryTV = view.findViewById(R.id.orderHistoryTV);
        settingLL = view.findViewById(R.id.settingLL);
        customerLL = view.findViewById(R.id.customerLL);
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
//                Toast.makeText(this, "You are logged out from this app", Toast.LENGTH_SHORT).show();
            }
        });

        editProfileLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent intent = new Intent(getContext(), UserProfileActivity.class);
                 startActivity(intent);
            }
        });

        ordersLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), OrderHistoryActivity.class);
                startActivity(intent);
            }
        });


//        orderHistoryTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getContext(), UserProfileActivity.class);
//                startActivity(intent);
//
//            }
//        });

        settingLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        customerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CustomerServiceActivity.class);
                startActivity(intent);

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