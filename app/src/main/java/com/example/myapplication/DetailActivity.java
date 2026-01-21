package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {

    private TextView tvNameDetail, tvPhoneDetail, tvGenderDetail, tvNoteDetail;
    private Button btnReturn, btnDelete, btnEdit;
    private Contact currentContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // 获取传递的联系人
        try {
            currentContact = (Contact) getIntent().getSerializableExtra("contact");
            if (currentContact == null) {
                Toast.makeText(this, "联系人数据错误", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        } catch (Exception e) {
            Log.e("DetailActivity", "获取联系人数据失败", e);
            Toast.makeText(this, "获取联系人数据失败", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        fillContactData();
        setListeners();

        Log.d("DetailActivity", "打开联系人详情: " + currentContact.getName());
    }

    private void initViews() {
        tvNameDetail = findViewById(R.id.tvNameDetail);
        tvPhoneDetail = findViewById(R.id.tvPhoneDetail);
        tvGenderDetail = findViewById(R.id.tvGenderDetail);
        tvNoteDetail = findViewById(R.id.tvNoteDetail);
        btnReturn = findViewById(R.id.btnReturn);
        btnDelete = findViewById(R.id.btnDelete);
        btnEdit = findViewById(R.id.btnEdit);
    }

    private void fillContactData() {
        try {
            tvNameDetail.setText("姓名: " + currentContact.getName());
            tvPhoneDetail.setText("电话: " + currentContact.getPhone());
            tvGenderDetail.setText("性别: " + currentContact.getGender());

            String note = currentContact.getNote();
            if (note != null && !note.isEmpty()) {
                tvNoteDetail.setText("备注: " + note);
            } else {
                tvNoteDetail.setText("备注: 无");
            }
        } catch (Exception e) {
            Log.e("DetailActivity", "填充联系人数据失败", e);
        }
    }

    private void setListeners() {
        // 返回主页面
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 删除联系人
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent();
                    intent.putExtra("deleteId", currentContact.getId());
                    setResult(RESULT_OK, intent);
                    finish();

                    Toast.makeText(DetailActivity.this,
                            "已删除联系人: " + currentContact.getName(),
                            Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("DetailActivity", "删除联系人失败", e);
                    Toast.makeText(DetailActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 打开编辑页
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(DetailActivity.this, EditActivity.class);
                    intent.putExtra("contact", currentContact);
                    startActivityForResult(intent, 100);
                } catch (Exception e) {
                    Log.e("DetailActivity", "打开编辑页面失败", e);
                    Toast.makeText(DetailActivity.this, "打开编辑页面失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("DetailActivity", "onActivityResult: requestCode=" + requestCode +
                ", resultCode=" + resultCode + ", hasData=" + (data != null));

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            try {
                // 更新编辑后的联系人
                Contact updatedContact = (Contact) data.getSerializableExtra("updatedContact");
                if (updatedContact != null) {
                    currentContact = updatedContact;
                    fillContactData();

                    // 通知主页面更新
                    Intent intent = new Intent();
                    intent.putExtra("updatedContact", currentContact);
                    setResult(RESULT_OK, intent);

                    Toast.makeText(this, "联系人已更新", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("DetailActivity", "处理编辑结果失败", e);
                Toast.makeText(this, "更新失败", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == RESULT_CANCELED) {
            Log.d("DetailActivity", "编辑操作被取消");
        }
    }
}