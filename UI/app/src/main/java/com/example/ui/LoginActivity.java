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

public class LoginActivity extends AppCompatActivity {
    myDBHelper myHelper;
    SQLiteDatabase sqlDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //로그인, 구현 중
        //ID, PW, 로그인 버튼 연결
        EditText edtID = (EditText) findViewById(R.id.edtID);
        EditText edtPW = (EditText) findViewById(R.id.edtPW);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        myHelper = new myDBHelper(this);
        //로그인 버튼 이벤트 리스너너
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //입력한 ID와 PW 가져오기
                String ID = edtID.getText().toString();
                String PW = edtPW.getText().toString();
                sqlDB = myHelper.getReadableDatabase();
                Cursor cursor;

                cursor=sqlDB.rawQuery("SELECT * FROM userTable WHERE ID = '"+ID+"'" +
                        "AND PW = '"+PW+"';", null);
                //입력된 ID와 PW를 비교
                //등록된 사용자인 경우
                if (cursor.getCount() > 0) {
                    while(cursor.moveToNext()) {
                        Toast.makeText(getApplicationContext(), cursor.getString(1), Toast.LENGTH_SHORT).show();
                        //사용자의 유형에 맞는 게시판 출력
                        //화면전환시 고객의 ID와 사용자 유형이 넘어갈 필요가 있다고 봄
                        //intent.putExtra("ID",입력된 ID);
                        //intent.putExtra("type",로그인한 고객의 유형);
                        //intent 를 넘길때 ID와 유형이 넘어가도록 작성
                    }
                }else{
                    //등록된 사용자가 아니면 메시지 출력
                    Toast.makeText(getApplicationContext(), "존재하지 않음", Toast.LENGTH_SHORT).show();
                }
                sqlDB.close();
                cursor.close();



            }
        });


        //수리기사 회원가입, 구현 중
        //수리기사 회원가입 버튼 연결
        Button btnReSignup = (Button) findViewById(R.id.btnSignupRepairman);
        //버튼 클릭시 이벤트 리스너
        btnReSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent 로 수리기사 회원가입의 class 로 지정
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                //회원가입은 단방향 액티비티
                startActivity(intent);
            }
        });

        //고객 회원가입, 구현 중
        //고객 회원가입 버튼 연결
        Button btnCuSignup = (Button) findViewById(R.id.btnSignupCustomer);
        //버튼 클릭시 이벤트 리스너
        btnCuSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent 로 고객 회원가입의 class 로 지정
                Intent intent = new Intent(getApplicationContext(), SignUpActivity2.class);
                //회원가입은 단방향 액티비티
                startActivity(intent);
            }
        });
    }
    public class myDBHelper extends SQLiteOpenHelper {

        public myDBHelper(@Nullable Context context) {
            super(context, "UDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE userTable (ID CHAR(15), PW CHAR(15)," +
                    "Utype INTEGER, nickName CHAR(15))");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("DROP TABLE IF EXISTS userTable");
            onCreate(db);
        }
    }
}