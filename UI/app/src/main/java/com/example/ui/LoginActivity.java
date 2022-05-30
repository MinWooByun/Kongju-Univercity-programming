package com.example.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
//로그인
//완료
public class LoginActivity extends AppCompatActivity {
    dbHelper myHelper;
    SQLiteDatabase sqlDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //ID, PW, 로그인 버튼 연결
        EditText edtID = (EditText) findViewById(R.id.edtID);
        EditText edtPW = (EditText) findViewById(R.id.edtPW);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        myHelper = new dbHelper(this, 1);
        //로그인 버튼 이벤트 리스너너
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //입력한 ID와 PW 가져오기
                String ID = edtID.getText().toString();
                String PW = edtPW.getText().toString();
                sqlDB = myHelper.getReadableDatabase();
                Cursor cursor;
                //입력된 ID와 PW를 userTable에서 검색
                cursor = sqlDB.rawQuery("SELECT * FROM userTable WHERE id = '" + ID + "'" +
                        "AND pw = '" + PW + "';", null);
                //등록된 사용자인 경우, 중복된 ID가 없으므로 카운트가 1일것
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        Intent intent = new Intent(getApplicationContext(), noticeBoardActivity.class);
                        //사용자의 ID와 type을 넘기고
                        intent.putExtra("u_id", cursor.getString(0));
                        intent.putExtra("type", cursor.getInt(2));
                        //type에 맞는 사용자의 적절한 게시판 화면을 출력
                        startActivity(intent);
                    }
                } else {
                    //등록된 사용자가 아니면 메시지 출력
                    Toast.makeText(getApplicationContext(), "존재하지 않음", Toast.LENGTH_SHORT).show();
                }
                sqlDB.close();
                cursor.close();
            }
        });

        //수리기사 회원가입, 완료
        //수리기사 회원가입 버튼 연결
        Button btnReSignup = (Button) findViewById(R.id.btnSignupRepairman);
        //버튼 클릭시 이벤트 리스너
        btnReSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent 로 수리기사 회원가입의 class 로 지정
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        //고객 회원가입, 완료
        //고객 회원가입 버튼 연결
        Button btnCuSignup = (Button) findViewById(R.id.btnSignupCustomer);
        //버튼 클릭시 이벤트 리스너
        btnCuSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent 로 고객 회원가입의 class 로 지정
                Intent intent = new Intent(getApplicationContext(), SignUpActivity2.class);
                startActivity(intent);
            }
        });
    }
}