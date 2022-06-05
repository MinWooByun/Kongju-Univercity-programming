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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_authority);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Button btnApprove = findViewById(R.id.btnApprove);
        Button btnRefusal = findViewById(R.id.btnRefusal);

        Intent intent = getIntent();
        String id = intent.getExtras().getString("id");
        String u_id = intent.getExtras().getString("u_id");

        //
        imgView = findViewById(R.id.imageView);
        printImg(id);
        //

        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        btnRefusal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
}
