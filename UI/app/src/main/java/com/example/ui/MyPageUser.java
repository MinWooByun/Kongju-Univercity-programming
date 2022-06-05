package com.example.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class MyPageUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_customer);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        TextView tvNickName = findViewById(R.id.tvNickName);
        EditText etPw = findViewById(R.id.etPw);
        EditText etPwReconfirm = findViewById(R.id.etPwReconfirm);
        Button btnUpdate = findViewById(R.id.btnUpdate);

        Intent intent = getIntent();
        String u_id = intent.getExtras().getString("u_id");

        dbHelper helper = new dbHelper(this, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nickname FROM userTable WHERE id = '"+ u_id +"'",null);

        while (cursor.moveToNext()) {
            tvNickName.setText(cursor.getString(0));
        }

        db.close();
        cursor.close();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Pw = etPw.getText().toString();
                String PwReconfirm = etPwReconfirm.getText().toString();

                if(Pw.equals("") || PwReconfirm.equals("")) {
                    Toast.makeText(MyPageUser.this,"비밀번호 입력칸을 채워주세요.", Toast.LENGTH_LONG).show();
                } else {
                    if(Pw.equals(PwReconfirm)) {
                        boolean isUpdated = helper.userUpdate(u_id, etPwReconfirm.getText().toString());
                        if(isUpdated == true) {
                            etPw.setText("");
                            etPwReconfirm.setText("");
                            Toast.makeText(MyPageUser.this,"성공", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MyPageUser.this, noticeBoardActivity.class);
                            intent.putExtra("u_id", u_id);
                            intent.putExtra("type", 2);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MyPageUser.this,"실패", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(MyPageUser.this,"비밀번호가 틀립니다.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
