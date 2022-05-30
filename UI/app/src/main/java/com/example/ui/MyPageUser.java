package com.example.ui;

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

        dbHelper helper = new dbHelper(this, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        // 아이디 값을 아직 전 페이지에서 못 받아와서 임시방편임
        Cursor cursor = db.rawQuery("SELECT nickname FROM userTable WHERE id = 'KWH3' AND type = 0",null);

        while (cursor.moveToNext()) {
            tvNickName.setText(cursor.getString(0));
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Pw = etPw.getText().toString();
                String PwReconfirm = etPwReconfirm.getText().toString();

                if(Pw.equals("") || PwReconfirm.equals("")) {
                    Toast.makeText(MyPageUser.this,"비밀번호 입력칸을 채워주세요.", Toast.LENGTH_LONG).show();
                } else {
                    if(Pw.equals(PwReconfirm)) {
                        // 게시판에서 마이페이지로 넘어올 때 id 값을 intent로 받아와야 하는데 아직 미완성
                        boolean isUpdated = helper.userPwUpdate("KWH3", etPwReconfirm.getText().toString());
                        if(isUpdated == true) {
                            etPw.setText("");
                            etPwReconfirm.setText("");
                            Toast.makeText(MyPageUser.this,"성공", Toast.LENGTH_LONG).show();
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
