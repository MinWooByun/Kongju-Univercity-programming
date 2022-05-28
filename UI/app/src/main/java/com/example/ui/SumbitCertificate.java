package com.example.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SumbitCertificate extends Activity {
    Button btnSelect, btnSubmit;
    ImageView imgView;
    TextView tvFileName;
    byte[] blobByte;
    myDBHelper myHelper;
    SQLiteDatabase sqlDB;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.certificate);

        btnSelect = (Button) findViewById(R.id.btnSelectCertification);
        btnSubmit = (Button) findViewById(R.id.btnCertificateSuggestion);
        imgView = (ImageView) findViewById(R.id.imageView);
        tvFileName = (TextView) findViewById(R.id.tvCertificationName);
        myHelper = new myDBHelper(this);
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
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //1. 이미지뷰의 사진을 bitmap으로 가져온다
                BitmapDrawable drawable = (BitmapDrawable) imgView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                String id = "dndud";
                //2. 가져온 이미지를 blob으로 저장하기 위해 바이트파일로 변환
                //이미지를 불러올때 같이 수행
                //String image = bitmapToByteArray(bitmap);
                //3. 바이트 파일로 변환된 이미지를 db에 저장
                sqlDB = myHelper.getWritableDatabase();
                String sql = "INSERT INTO imgTable VALUES('"+id+"', ?)";
                SQLiteStatement insertStml = sqlDB.compileStatement(sql);
                insertStml.clearBindings();
                insertStml.bindBlob(1,blobByte);
                insertStml.execute();
            }
        });
    }

    //겔러리에서 증명서를 선택하면 사진과 이름을 보여줌
    //완료
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ByteArrayBuffer baf = new ByteArrayBuffer(50);
        if (requestCode != 1 || resultCode != RESULT_OK) {
            return;
        }
        try {
            Uri selectedfile = data.getData();//선택한 사진
            String filename = getFileNameFromUri(selectedfile);//이름
            InputStream in = getContentResolver().openInputStream(selectedfile);
            Bitmap image = BitmapFactory.decodeStream(in);
            imgView.setImageBitmap(image);
            tvFileName.setText(filename);

            //이미지를 byte 형태로 변환
            BufferedInputStream bis = new BufferedInputStream(in);
            int current = 0;
            while((current=bis.read())!=-1){
                baf.append((byte)current);
            }
            blobByte = baf.toByteArray();
            in.close();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
    //DB
    public class myDBHelper extends SQLiteOpenHelper {
        public myDBHelper(@Nullable Context context) {
            super(context, "HL_DB.db", null, 1);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            /*db.execSQL("CREATE TABLE userTable (id varchar(20), pw varchar(30)," +
                    "type int, nickName varchar(1000))");*/
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            /*db.execSQL("DROP TABLE IF EXISTS userTable");
            onCreate(db);*/
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


    //참고 : https://daldalhanstory.tistory.com/199
    //비트맵을 바이너리 바이트배열로 바꿔주는 메서드
    /*public String bitmapToByteArray(Bitmap bitmap){
        String image = "";
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] byteArray = stream.toByteArray();
        image = byteArrayToBinaryString(byteArray);
        return image;
    }
    //바이너리 바이트 배열을 스트링으로 바꿔주는 메서드
    public static String byteArrayToBinaryString(byte[] b){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i<b.length;i++){
            sb.append(byteToBinaryString(b[i]));
        }
        return sb.toString();
    }
    //바이너리 바이트를 스트링으로 바꾸어주는 메서드*/
}
