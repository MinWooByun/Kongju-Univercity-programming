package com.example.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class SignUpActivity2 extends Activity {
    //고객 회원가입
    dbHelper myHelper;
    SQLiteDatabase sqlDB;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_layout2);

        EditText edtID, edtPW, edtrePW,edtNickname;
        Button btnSignup, btnOverlap;

        edtID = (EditText) findViewById(R.id.edtID_Signup2);
        edtPW = (EditText) findViewById(R.id.edtPW_Signup2);
        edtrePW = (EditText) findViewById(R.id.edtrePW_Signup2);
        edtNickname = (EditText) findViewById(R.id.edtNickname_Signup2);
        btnSignup = (Button) findViewById(R.id.btnSignup2);
        btnOverlap = (Button) findViewById(R.id.btnOverlap2);
        myHelper = new dbHelper(this,1);
        //회원가입 버튼
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] check = {0,0,0};
                //중복체크 버튼 활성/비활성 여부
                if(btnOverlap.isEnabled()) {check[0]=0;
                }else {check[0]=1;
                }
                //PW 일치 여부
                if(edtPW.length()>0&&edtPW.getText().toString().equals(edtrePW.getText().toString())){
                    check[1]=1;
                }else{check[1]=0;
                }
                //닉네임 입력 여부
                if(edtNickname.length()>=1){
                    check[2]=1;
                }else{check[2]=0;
                }
                Toast.makeText(getApplicationContext(),
                        check[0]+","+check[1]+","+check[2],
                        Toast.LENGTH_SHORT).show();
                //조건을 모두 만족하면 회원 등록
                if(check[0]==1&&check[1]==1&&check[2]==1){
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
                }else{
                    //조건을 하나라도 만족하지 못하면 메시지 출력
                    Toast.makeText(getApplicationContext(),
                            "올바르게 작성했는지 다시 확인해 주세요",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        //중복체크 버튼
        //완료
        btnOverlap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int count=0;
                String ID = edtID.getText().toString();
                //특수문자 및 공백 여부 체크
                for(int i=0;i<ID.length();i++){
                    if (String.valueOf(ID.charAt(i)).matches("[^a-z|A-Z|0-9]")) {
                        count++;
                    }
                }
                //특수문자 및 공백이 포함된 경우
                if(count>=1||ID.length()==0){
                    Toast.makeText(getApplicationContext(), "사용할수 없는 문자나 공백이 포함되었습니다", Toast.LENGTH_SHORT).show();
                }else{
                    //포함되지 않은 경우
                    //입력한 ID와 DB에 해당 ID가 존재하는지 비교
                    sqlDB = myHelper.getReadableDatabase();
                    Cursor cursor;
                    cursor=sqlDB.rawQuery("SELECT * FROM userTable WHERE id = '"+ID+"';", null);
                    //동일한 ID가 존재하면 중복된 ID라 메시지 출력
                    if (cursor.getCount() > 0) {
                        while(cursor.moveToNext()) {
                            Toast.makeText(getApplicationContext(), "중복된 ID", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        //동일한 ID가 없으면 버튼을 비활성화 상태로 만들고 사용가능하다는 메시지 출력
                        btnOverlap.setEnabled(false);
                        Toast.makeText(getApplicationContext(), "사용 가능한 ID 입니다", Toast.LENGTH_SHORT).show();
                    }
                    sqlDB.close();
                    cursor.close();
                }
            }
        });
        //중복체크후 ID 변경시 중복체크 버튼 활성화
        //완료
        edtID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //ID 입력칸에 변화가 일어나면 발생
                btnOverlap.setEnabled(true);
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }
}