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
    String u_id;
    int type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_customer);

        // 타이틀바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        TextView tvId = findViewById(R.id.tvId);
        EditText etPw = findViewById(R.id.etPw);
        EditText etPwReconfirm = findViewById(R.id.etPwReconfirm);
        Button btnUpdate = findViewById(R.id.btnUpdate);

        Intent intent = getIntent();
        u_id = intent.getExtras().getString("u_id");
        type = intent.getExtras().getInt("type");

        // db선언 및 id 조회
        dbHelper helper = new dbHelper(this, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM userTable WHERE id = '"+ u_id +"'",null);

        while (cursor.moveToNext()) {
            // 마이페이지 레이아웃에서 자신의 아이디를 확인하기 위해 텍스트뷰의 setText() 함수를 이용하여 아이디를 보여준다.
            tvId.setText(cursor.getString(0));
        }

        db.close();
        cursor.close();

        // 저장 버튼 처리
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Pw = etPw.getText().toString();
                String PwReconfirm = etPwReconfirm.getText().toString();

                // EditText에 비밀번호를 다 입력했는지 유효성을 체크하는 조건문이다.
                if(Pw.equals("") || PwReconfirm.equals("")) {
                    Toast.makeText(MyPageUser.this,"비밀번호 입력칸을 채워주세요.", Toast.LENGTH_LONG).show();
                } else {
                    if(Pw.equals(PwReconfirm)) {
                        // db의 성공 여부를 isUpdate에 넣어주고 토스트 메시지를 띄워 보여준다.
                        boolean isUpdated = helper.userUpdate(u_id, etPwReconfirm.getText().toString());
                        if(isUpdated == true) {
                            etPw.setText("");
                            etPwReconfirm.setText("");
                            Toast.makeText(MyPageUser.this,"성공", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MyPageUser.this, noticeBoardActivity.class);
                            intent.putExtra("u_id", u_id);
                            intent.putExtra("type", type);
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

    // 안드로이드에 기본으로 내장되어 있는 뒤로가기 버튼에 대한 이벤트 처리
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MyPageUser.this, noticeBoardActivity.class);
        intent.putExtra("u_id", u_id);
        intent.putExtra("type", type);
        startActivity(intent);
        finish();
    }
}
