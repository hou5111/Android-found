package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private CircleTextAvatar avatar;
    private TextView tvPhoneNumber;
    private TextView tvAddress;
    private TextView tvVersion;

    private SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);

        avatar = view.findViewById(R.id.avatar);
        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvVersion = view.findViewById(R.id.tvVersion);

        preferences = getActivity().getSharedPreferences("user_profile", Context.MODE_PRIVATE);

        setupViews(view);
        loadUserInfo();

        return view;
    }

    private void setupViews(View view) {
        // 设置头像
        avatar.setName("我");

        // 编辑电话号码
        ImageButton btnEditPhone = view.findViewById(R.id.btnEditPhone);
        btnEditPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhoneEditDialog();
            }
        });

        // 编辑地址
        ImageButton btnEditAddress = view.findViewById(R.id.btnEditAddress);
        btnEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddressEditDialog();
            }
        });

        // 关于我们
        View layoutAbout = view.findViewById(R.id.layoutAbout);
        layoutAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "通讯录 v1.0\n您的个人通讯录应用", Toast.LENGTH_LONG).show();
            }
        });

        // 设置版本号
        tvVersion.setText("v1.0.0");
    }

    private void loadUserInfo() {
        // 从SharedPreferences加载用户信息
        String phone = preferences.getString("phone", "13888888888");
        String address = preferences.getString("address", "北京市海淀区");

        tvPhoneNumber.setText(phone);
        tvAddress.setText(address);
    }

    private void saveUserInfo(String phone, String address) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("phone", phone);
        editor.putString("address", address);
        editor.apply();
    }

    private void showPhoneEditDialog() {
        // 简单的直接编辑（实际项目中可以用Dialog）
        Toast.makeText(getContext(), "点击编辑电话号码", Toast.LENGTH_SHORT).show();
        // 这里可以弹出一个编辑对话框
        // 为简化，我们直接修改
        String newPhone = "139" + (int)(Math.random() * 100000000);
        tvPhoneNumber.setText(newPhone);
        saveUserInfo(newPhone, preferences.getString("address", ""));
        Toast.makeText(getContext(), "电话号码已更新", Toast.LENGTH_SHORT).show();
    }

    private void showAddressEditDialog() {
        Toast.makeText(getContext(), "点击编辑地址", Toast.LENGTH_SHORT).show();
        String newAddress = "上海市浦东新区";
        tvAddress.setText(newAddress);
        saveUserInfo(preferences.getString("phone", ""), newAddress);
        Toast.makeText(getContext(), "地址已更新", Toast.LENGTH_SHORT).show();
    }
}