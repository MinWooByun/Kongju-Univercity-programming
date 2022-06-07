package com.example.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class AdminAuthority extends AppCompatActivity {
    dbHelper helper = new dbHelper(this, 1);
    ImageView imgView;
    String u_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_authority);

        // 타이틀바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Button btnApprove = findViewById(R.id.btnApprove);
        Button btnRefusal = findViewById(R.id.btnRefusal);

        Intent intent = getIntent();
        String id = intent.getExtras().getString("id");
        u_id = intent.getExtras().getString("u_id");

        imgView = findViewById(R.id.imageView);
        // 증명서 사진을 출력하는 함수이다.
        printImg(id);


        // 권한 승인 버튼 처리
        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // db의 성공 여부를 isUpdate에 넣어주어 토스트 메시지를 띄워 보여준다.
                boolean isUpdate = helper.isProofApprove(id);
                if(isUpdate == true) {
                    Toast.makeText(AdminAuthority.this,"성공", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AdminAuthority.this,"실패", Toast.LENGTH_LONG).show();
                }
                Intent intent = new Intent(AdminAuthority.this, AdminList.class);
                intent.putExtra("u_id", u_id);
                startActivity(intent);
                finish();
            }
        });

        // 권한 거부 버튼 처리
        btnRefusal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // db의 성공 여부를 isUpdate에 넣어주고 토스트 메시지를 띄워 보여준다.
                boolean isUpdate = helper.isProofRefusal(id);
                if(isUpdate == true) {
                    helper.imgDelete(id);
                    Toast.makeText(AdminAuthority.this,"성공", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AdminAuthority.this,"실패", Toast.LENGTH_LONG).show();
                }
                Intent intent = new Intent(AdminAuthority.this, AdminList.class);
                intent.putExtra("u_id", u_id);
                startActivity(intent);
                finish();
            }
        });
    }

    // 증명서 사진 출력 처리
    public void printImg(String id){
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.rawQuery("SELECT * FROM imgTable WHERE id = " +
                "'" + id + "';", null);
        while(cursor.moveToNext()){
            byte[] image = cursor.getBlob(1);
            Bitmap bm = BitmapFactory.decodeByteArray(image,0,image.length);
            imgView.setImageBitmap(bm);
        }
        cursor.close();
        sqlDB.close();
    }

    // 안드로이드에 기본으로 내장되어 있는 뒤로가기 버튼에 대한 이벤트 처리
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AdminAuthority.this, AdminList.class);
        intent.putExtra("u_id", u_id);
        startActivity(intent);
        finish();
    }
}
