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

public class MyPageRepairMan extends AppCompatActivity {
    String u_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_repairman);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        TextView tvNickName = findViewById(R.id.tvNickName);
        TextView tvOpenLink = findViewById(R.id.tvOpenLink);
        EditText etPw = findViewById(R.id.etPw);
        EditText etPwReconfirm = findViewById(R.id.etPwReconfirm);
        Button btnCertificate = findViewById(R.id.btnCertificate);
        Button btnUpdate = findViewById(R.id.btnUpdate);

        Intent intent = getIntent();
        u_id = intent.getExtras().getString("u_id");
        Intent intent1 = new Intent(MyPageRepairMan.this, SumbitCertificate.class);

        dbHelper helper = new dbHelper(this, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM userTable WHERE id = '"+ u_id +"'",null);
        Cursor cursor1 = db.rawQuery("SELECT openlink FROM repairManTable WHERE id = '" + u_id +"'", null);

        while (cursor.moveToNext()) {
            tvNickName.setText(cursor.getString(0));
            String pw = cursor.getString(1);
            String nickname = cursor.getString(3);
            intent1.putExtra("u_id", u_id);
            intent1.putExtra("pw", pw);
            intent1.putExtra("nickname", nickname);
        }

        while (cursor1.moveToNext()) {
            tvOpenLink.setText(cursor1.getString(0));
            String openlink = cursor1.getString(0);
            intent1.putExtra("openlink", openlink);
        }

        db.close();
        cursor.close();
        cursor1.close();

        btnCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPageRepairMan.this, SumbitCertificate.class);
                intent.putExtra("u_id", u_id);
                intent.putExtra("state", "MyPage");
                startActivity(intent);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Pw = etPw.getText().toString();
                String PwReconfirm = etPwReconfirm.getText().toString();

                if(Pw.equals("") || PwReconfirm.equals("")) {
                    Toast.makeText(MyPageRepairMan.this,"비밀번호 입력칸을 채워주세요.", Toast.LENGTH_LONG).show();
                } else {
                    if(Pw.equals(PwReconfirm)) {
                        // 게시판에서 마이페이지로 넘어올 때 id 값을 intent로 받아와야 하는데 아직 미완성
                        boolean isUpdated = helper.repairManPwUpdate(u_id, etPwReconfirm.getText().toString());
                        if(isUpdated == true) {
                            etPw.setText("");
                            etPwReconfirm.setText("");
                            Toast.makeText(MyPageRepairMan.this,"성공", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MyPageRepairMan.this, noticeBoardActivity.class);
                            intent.putExtra("u_id", u_id);
                            intent.putExtra("type", 1);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MyPageRepairMan.this,"실패", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(MyPageRepairMan.this,"비밀번호가 틀립니다.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MyPageRepairMan.this, noticeBoardActivity.class);
        intent.putExtra("u_id", u_id);
        intent.putExtra("type", 1);
        startActivity(intent);
        finish();
    }
}
