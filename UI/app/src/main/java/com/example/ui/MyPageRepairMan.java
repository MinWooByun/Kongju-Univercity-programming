package com.example.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class MyPageRepairMan extends AppCompatActivity {

    MyDatabaseHelper myDb;
    TextView tvNickname;
    EditText etPw, etId, etPassword, etIsproof, etOpenlink;
    Button btnUpdate, btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_repairman);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        tvNickname = findViewById(R.id.tvNickname);
        etPw = findViewById(R.id.etPw);
        etId = findViewById(R.id.etId);
        etPassword = findViewById(R.id.etPassword);
        etIsproof = findViewById(R.id.etIsproof);
        etOpenlink = findViewById(R.id.etOpenlink);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnAdd = findViewById(R.id.btnAdd);

        myDb = new MyDatabaseHelper(MyPageRepairMan.this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.repairManInsertData(etId.getText().toString() , etPassword.getText().toString(), etIsproof.getText().toString(), etOpenlink.getText().toString());
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.repairManUpdateData(etPw.getText().toString(), tvNickname.getText().toString());
                updateData();
            }
        });
    }

    public void updateData() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isUpdated = myDb.repairManUpdateData(etPw.getText().toString(), tvNickname.getText().toString());
                if(isUpdated == true) {
                    Toast.makeText(MyPageRepairMan.this,"성공", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MyPageRepairMan.this,"실패", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
