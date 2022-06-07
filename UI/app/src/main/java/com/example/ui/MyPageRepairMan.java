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
    int isproof, r_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_repairman);

        // 타이틀바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        TextView tvId = findViewById(R.id.tvId);
        EditText etPw = findViewById(R.id.etPw);
        EditText etPwReconfirm = findViewById(R.id.etPwReconfirm);
        EditText etOpenLink = findViewById(R.id.etOpenLink);
        Button btnCertificate = findViewById(R.id.btnCertificate);
        Button btnUpdate = findViewById(R.id.btnUpdate);

        Intent intent = getIntent();
        u_id = intent.getExtras().getString("u_id");
        Intent intent1 = new Intent(MyPageRepairMan.this, SumbitCertificate.class);

        // db선언 및 필요한 정보 조회
        dbHelper helper = new dbHelper(this, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM userTable WHERE id = '"+ u_id +"'",null);
        Cursor cursor1 = db.rawQuery("SELECT isproof, openlink FROM repairManTable WHERE id = '" + u_id +"'", null);
        Cursor cursor2 = db.rawQuery("SELECT id FROM imgTable WHERE id = '" + u_id +"'", null);

        while (cursor.moveToNext()) {
            tvId.setText(cursor.getString(0));
            String pw = cursor.getString(1);
            intent1.putExtra("u_id", u_id);
            intent1.putExtra("pw", pw);
        }

        while (cursor1.moveToNext()) {
            isproof = cursor1.getInt(0);
            etOpenLink.setText(cursor1.getString(1));
            String openlink = cursor1.getString(1);
            intent1.putExtra("openlink", openlink);
        }

        while (cursor2.moveToNext()) {
            r_id = cursor2.getCount();
        }

        db.close();
        cursor.close();
        cursor1.close();

        // 증명서 제출 버튼 처리
        btnCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 증명서 인증 여부를 확인하는 조건문이다.
                if(isproof == 0) {
                    // 이미지 테이블에 증명서가 등록되어 있는지 확인하는 조건문이다.
                    if(r_id == 0) {
                        Intent intent = new Intent(MyPageRepairMan.this, SumbitCertificate.class);
                        intent.putExtra("u_id", u_id);
                        intent.putExtra("state", "MyPage");
                        startActivity(intent);
                    } else {
                        Toast.makeText(MyPageRepairMan.this, "증명서 심사가 진행중입니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MyPageRepairMan.this, "증명서 인증이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 저장 버튼 처리
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Pw = etPw.getText().toString();
                String PwReconfirm = etPwReconfirm.getText().toString();
                String OpenLink = etOpenLink.getText().toString();

                // EditText에 비밀번호와 오픈링크를 다 입력했는지 유효성을 체크하는 조건문이다.
                if(Pw.equals("") || PwReconfirm.equals("") || OpenLink.equals("")) {
                    Toast.makeText(MyPageRepairMan.this,"비밀번호 및 오픈링크 입력칸을 채워주세요.", Toast.LENGTH_LONG).show();
                } else {
                    // 비밀번호 EditText와 비밀번호를 재입력한 EditText가 동일한지 유효성을 체크하는 조건문이다.
                    if(Pw.equals(PwReconfirm)) {
                        // db의 성공 여부를 isUpdate에 넣어주고 토스트 메시지를 띄워 보여준다.
                        boolean isUpdated = helper.repairManUpdate(u_id, PwReconfirm, OpenLink);
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

    // 안드로이드에 기본으로 내장되어 있는 뒤로가기 버튼에 대한 이벤트 처리
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MyPageRepairMan.this, noticeBoardActivity.class);
        intent.putExtra("u_id", u_id);
        intent.putExtra("type", 1);
        startActivity(intent);
        finish();
    }
}
