package com.example.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class SignUpActivity2 extends Activity {
    //고객 회원가입
    myDBHelper myHelper;
    SQLiteDatabase sqlDB;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_layout2);

        EditText edtID, edtPW,edtNickname;
        Button btnSignup, btnOverlap;

        edtID = (EditText) findViewById(R.id.edtID_Signup2);
        edtPW = (EditText) findViewById(R.id.edtPW_Signup2);
        edtNickname = (EditText) findViewById(R.id.edtNickname_Signup2);
        btnSignup = (Button) findViewById(R.id.btnSignup2);
        btnOverlap = (Button) findViewById(R.id.btnOverlap);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("INSERT INTO userTable VALUES ('"
                        +edtID.getText().toString()+"','"
                        +edtPW.getText().toString()+"',"
                        +1+",'"
                        +edtNickname.getText().toString()+"');");
                sqlDB.close();
                Toast.makeText(getApplicationContext(),
                        edtNickname.getText().toString()+"님 가입이 완료되었습니다.",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        myHelper = new myDBHelper(this);
        btnOverlap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*sqlDB = myHelper.getWritableDatabase();
                myHelper.onUpgrade(sqlDB, 1, 2); // 인수는 아무거나 입력하면 됨.
                sqlDB.close();*/
                Toast.makeText(getApplicationContext(),
                        "클릭",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    //DB
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
