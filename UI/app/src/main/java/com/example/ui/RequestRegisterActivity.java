package com.example.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RequestRegisterActivity extends AppCompatActivity {
    dbHelper dbHelper;
    String u_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_register);
        dbHelper = new dbHelper(RequestRegisterActivity.this, 1);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent = getIntent();
        u_id= intent.getExtras().getString("u_id");

        //xml 연동
        Button btnR_register = (Button)findViewById(R.id.R_register);
        EditText R_title = (EditText)findViewById(R.id.R_title);
        EditText R_contents = (EditText)findViewById(R.id.R_Contents);
        Spinner category = (Spinner)findViewById(R.id.R_category);
        Spinner symptom = (Spinner)findViewById(R.id.R_symptom);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        TextView R_id = (TextView) findViewById(R.id.R_id);


        R_id.setText("작성자:"+u_id);
        //의뢰 등록 버튼을 누른 경우
        btnR_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RequestRegisterActivity.this ,noticeBoardActivity.class);
                String title = R_title.getText().toString();
                String contents = R_contents.getText().toString();
                int category_n = category.getSelectedItemPosition();
                int symptom_n = symptom.getSelectedItemPosition();

            if(title.equals("") || contents.equals("")){
                Toast.makeText(getApplicationContext(), "제목이나 고장 증상을 넣어주세요.", Toast.LENGTH_SHORT).show();
            }else if(category_n == 0 || symptom_n == 0){
                Toast.makeText(getApplicationContext(), "카테고리나 고장증상을 선택해 주세요.", Toast.LENGTH_SHORT).show();
            }else{
                dbHelper.insertRequest(u_id, title, symptom_n, contents,category_n,0);
                Toast.makeText(getApplicationContext(), "등록되었습니다.", Toast.LENGTH_SHORT).show();
                intent.putExtra("u_id", u_id);
                intent.putExtra("type", 2);
                startActivity(intent);
                finish();
            }

            }
        });
    }
    //뒤로 가는 버튼 처리
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RequestRegisterActivity.this, noticeBoardActivity.class);
        intent.putExtra("u_id", u_id);
        intent.putExtra("type", 2);
        startActivity(intent);
        finish();
    }
}