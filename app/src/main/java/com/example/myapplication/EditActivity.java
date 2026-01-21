package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class EditActivity extends AppCompatActivity {

    private EditText etName, etPhone, etNote;
    private RadioGroup rgGender;
    private RadioButton rbMale, rbFemale;
    private Button btnSave;
    private Contact editContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // 判断是"编辑"还是"新建"
        editContact = (Contact) getIntent().getSerializableExtra("contact");

        initViews();
        if (editContact != null) {
            // 编辑模式：填充已有数据
            fillEditData();
        } else {
            // 新建模式：设置默认性别为男
            rbMale.setChecked(true);
        }
        setSaveListener();
    }

    private void initViews() {
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etNote = findViewById(R.id.etNote);
        rgGender = findViewById(R.id.rgGender);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        btnSave = findViewById(R.id.btnSave);
    }

    // 填充已有联系人数据（编辑模式）
    private void fillEditData() {
        if (editContact == null) return;

        etName.setText(editContact.getName());
        etPhone.setText(editContact.getPhone());
        etNote.setText(editContact.getNote());

        if ("男".equals(editContact.getGender())) {
            rbMale.setChecked(true);
        } else {
            rbFemale.setChecked(true);
        }
    }

    // 保存按钮监听
    private void setSaveListener() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取输入的数据
                String name = etName.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String gender = rbMale.isChecked() ? "男" : "女";
                String note = etNote.getText().toString().trim();

                // 输入验证
                if (name.isEmpty()) {
                    etName.setError("请输入姓名");
                    etName.requestFocus();
                    return;
                }

                if (phone.isEmpty()) {
                    etPhone.setError("请输入电话");
                    etPhone.requestFocus();
                    return;
                }

                Contact resultContact;
                if (editContact != null) {
                    // 编辑模式：更新已有对象
                    editContact.setName(name);
                    editContact.setPhone(phone);
                    editContact.setGender(gender);
                    editContact.setNote(note);
                    resultContact = editContact;
                } else {
                    // 新建模式：创建新对象
                    // 注意：这里ID暂时设为-1，由MainActivity处理
                    resultContact = new Contact(-1, name, phone, gender, note);
                }

                // 返回结果
                Intent intent = new Intent();
                if (editContact != null) {
                    intent.putExtra("updatedContact", resultContact);
                } else {
                    intent.putExtra("newContact", resultContact);
                }
                setResult(RESULT_OK, intent);
                finish(); // 关闭当前Activity
            }
        });
    }

    // 处理返回键
    @Override
    public void onBackPressed() {
        // 如果用户按返回键，返回取消结果
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}