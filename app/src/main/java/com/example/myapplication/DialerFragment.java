package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DialerFragment extends Fragment {

    private TextView tvDialNumber;
    private StringBuilder currentNumber = new StringBuilder();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dialer, container, false);

        tvDialNumber = view.findViewById(R.id.tvDialNumber);

        setupNumberButtons(view);
        setupActionButtons(view);

        return view;
    }

    private void setupNumberButtons(View view) {
        // 数字按钮 0-9
        int[] numberButtonIds = {
                R.id.btnNumber1, R.id.btnNumber2, R.id.btnNumber3,
                R.id.btnNumber4, R.id.btnNumber5, R.id.btnNumber6,
                R.id.btnNumber7, R.id.btnNumber8, R.id.btnNumber9,
                R.id.btnStar, R.id.btnNumber0, R.id.btnPound
        };

        for (int id : numberButtonIds) {
            Button btn = view.findViewById(id);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button button = (Button) v;
                    appendNumber(button.getText().toString());
                }
            });
        }
    }

    private void setupActionButtons(View view) {
        // 删除按钮
        ImageButton btnDelete = view.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLastDigit();
            }
        });

        // 长按删除全部
        btnDelete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                clearNumber();
                return true;
            }
        });

        // 拨打按钮
        ImageButton btnCall = view.findViewById(R.id.btnCall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall();
            }
        });

        // 添加联系人按钮
        ImageButton btnAddContact = view.findViewById(R.id.btnAddContact);
        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToContacts();
            }
        });
    }

    private void appendNumber(String digit) {
        currentNumber.append(digit);
        updateDisplay();
    }

    private void deleteLastDigit() {
        if (currentNumber.length() > 0) {
            currentNumber.deleteCharAt(currentNumber.length() - 1);
            updateDisplay();
        }
    }

    private void clearNumber() {
        currentNumber.setLength(0);
        updateDisplay();
    }

    private void updateDisplay() {
        String number = currentNumber.toString();
        tvDialNumber.setText(number);
    }

    private void makeCall() {
        if (currentNumber.length() == 0) {
            Toast.makeText(getContext(), "请输入号码", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String phoneNumber = currentNumber.toString();
            Intent intent = new Intent(Intent.ACTION_DIAL); // 使用ACTION_DIAL而不是ACTION_CALL，不需要拨号权限
            intent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getContext(), "拨号失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void addToContacts() {
        if (currentNumber.length() == 0) {
            Toast.makeText(getContext(), "请输入号码", Toast.LENGTH_SHORT).show();
            return;
        }

        // 跳转到添加联系人页面，预填号码
        Intent intent = new Intent(getActivity(), EditActivity.class);
        intent.putExtra("prefilledPhone", currentNumber.toString());
        startActivity(intent);
    }
}