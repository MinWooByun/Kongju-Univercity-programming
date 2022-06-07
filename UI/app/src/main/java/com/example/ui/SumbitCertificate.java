package com.example.ui;
//증명서를 제출하는 클래스
//회원가입과 로그인시 넘어옴
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SumbitCertificate extends AppCompatActivity {
    Button btnSelect, btnSubmit;
    ImageView imgView;
    TextView tvFileName;
    byte[] blobByte = "".getBytes();
    dbHelper myHelper;
    SQLiteDatabase sqlDB;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.certificate);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //회원가입 양식으로부터 받아오는 정보
        Intent intent = getIntent();
        String re_id= intent.getExtras().getString("u_id");
        String re_pw= intent.getExtras().getString("pw","");
        String re_link= intent.getExtras().getString("link","");
        String state = intent.getExtras().getString("state");

        btnSelect = (Button) findViewById(R.id.btnSelectCertification);
        btnSubmit = (Button) findViewById(R.id.btnCertificateSuggestion);
        imgView = (ImageView) findViewById(R.id.imageView);
        tvFileName = (TextView) findViewById(R.id.tvCertificationName);
        myHelper = new dbHelper(SumbitCertificate.this,1);
        //증명서 선택버튼
        //완료
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //버튼을 누르면 갤러리가 열림
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });
        //증명서 제출버튼
        //완료
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //1. 이미지뷰의 사진을 bitmap으로 가져온다
                BitmapDrawable drawable = (BitmapDrawable) imgView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                //2. 가져온 이미지를 blob으로 저장하기 위해 바이트파일로 변환
                //이미지를 불러올때 같이 수행
                //3. 바이트 파일로 변환된 이미지를 db에 저장
                //4. 직전 화면에서 받아온 데이터를 db에 저장
                //5. 마이페이지에서 넘어온 경우는 이미지만 저장
                sqlDB = myHelper.getWritableDatabase();
                Object[] args;
                //회원가입
                if(state.compareTo("SignUp")==0){
                    try{
                        String sql = "INSERT INTO imgTable(id, imgBLOB) VALUES(?, ?)";
                        args= new Object[]{re_id, blobByte};
                        sqlDB.execSQL(sql, args);//이미지테이블
                        sqlDB.execSQL("INSERT INTO repairManTable(id, isproof, openlink) VALUES ('"//수리기사테이블
                                +re_id+"',"
                                +0+",'"
                                +re_link+"');");
                        sqlDB.execSQL("INSERT INTO userTable VALUES ('"//전체회원테이블
                                +re_id+"','"
                                +re_pw+"',"
                                +1+");");
                    }catch (SQLException ex){
                        Toast.makeText(getApplicationContext(), "오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                    }//마이페이지
                }else if(state.compareTo("MyPage")==0){
                    try{
                        String sql = "INSERT INTO imgTable(id, imgBLOB) VALUES(?, ?)";
                        args= new Object[]{re_id, blobByte};
                        sqlDB.execSQL(sql, args);//이미지테이블
                    }catch (SQLException ex){
                        Toast.makeText(getApplicationContext(), "오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                    }
                }
                sqlDB.close();
                //회원가입 시 처음 제출은 로그인으로
                //증명서 재제출은 마이페이지로 넘어감
                Intent outIntent = new Intent();
                setResult(RESULT_OK,outIntent);
                finish();
            }
        });
    }

    @Override
    //갤러리에서 이미지 선택후 이름과 이미지 출력
    //완료
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 1 || resultCode != RESULT_OK) {
            return;
        }
        try {
            Uri selectedfile = data.getData();//선택한 사진의 Uri
            String filename = getFileNameFromUri(selectedfile);//사진의 이름
            InputStream in = getContentResolver().openInputStream(selectedfile);
            Bitmap image = BitmapFactory.decodeStream(in);//사진을 비트맵으로 변환
            //이름과 비트맵 이미지 출력
            imgView.setImageBitmap(image);
            tvFileName.setText(filename);
            //비트맵을 바이트형태로 변환
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 30, os);
            blobByte = os.toByteArray();
            in.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    //파일의 이름 가져오기
    //완료
    @SuppressLint("Range")
    private String getFileNameFromUri(Uri uri) {
        String fileName = "";

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

        }
        cursor.close();
        return fileName;
    }
}