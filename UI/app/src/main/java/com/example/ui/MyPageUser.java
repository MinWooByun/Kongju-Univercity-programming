package com.example.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class MyPageUser extends AppCompatActivity {

    MyDatabaseHelper myDb;
    TextView tvNickname;
    EditText etPw, etId, etPassword, etType, etNickname;
    Button btnUpdate, btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_customer);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        tvNickname = findViewById(R.id.tvNickname);
        etPw = findViewById(R.id.etIsproof);
        etId = findViewById(R.id.etId);
        etPassword = findViewById(R.id.etPassword);
        etType = findViewById(R.id.etType);
        etNickname = findViewById(R.id.etNickname);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnAdd = findViewById(R.id.btnAdd);

        myDb = new MyDatabaseHelper(MyPageUser.this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.userInsertData(etId.getText().toString() ,etPassword.getText().toString(), etType.getText().toString(), etNickname.getText().toString());
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.userUpdateData(etPw.getText().toString(), tvNickname.getText().toString());
                updateData();
            }
        });
    }

    public void updateData() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isUpdated = myDb.userUpdateData(etPw.getText().toString(), tvNickname.getText().toString());
                if(isUpdated == true) {
                    Toast.makeText(MyPageUser.this,"성공", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MyPageUser.this,"실패", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
