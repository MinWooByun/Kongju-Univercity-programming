package com.example.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class SatisfiedConfirm extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.satisfied_confirm);

        TextView tvId = findViewById(R.id.tvId);
        RatingBar ratingBarState = findViewById(R.id.ratingBarState);
        RatingBar ratingBarKindness = findViewById(R.id.ratingBarKindness);
        RatingBar ratingBarTerm = findViewById(R.id.ratingBarTerm);
        TextView tvPrice = findViewById(R.id.tvPrice);
        Button btnClose = findViewById(R.id.btnClose);

        Intent intent = getIntent();
        int number = intent.getExtras().getInt("number");

        // db선언 및 필요한 정보 조회
        dbHelper helper = new dbHelper(this, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT r_id, S_State, S_Kindness, S_Term, price FROM satisficationTable WHERE p_num = '"+ number +"'",null);

        // db에서 불러온 만족도에 대한 정보들을 설정하고 보여해준다.
        while (cursor.moveToNext()) {
            tvId.setText(cursor.getString(0));
            ratingBarState.setRating(cursor.getInt(1));
            ratingBarKindness.setRating(cursor.getInt(2));
            ratingBarTerm.setRating(cursor.getInt(3));
            tvPrice.setText(String.valueOf(cursor.getInt(4)));
        }

        db.close();
        cursor.close();

        // 닫기 버튼 처리
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
