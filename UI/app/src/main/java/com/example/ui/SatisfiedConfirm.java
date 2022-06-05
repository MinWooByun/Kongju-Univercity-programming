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

        RatingBar ratingBarState = findViewById(R.id.ratingBarState);
        RatingBar ratingBarKindness = findViewById(R.id.ratingBarKindness);
        RatingBar ratingBarTerm = findViewById(R.id.ratingBarTerm);
        TextView tvPrice = findViewById(R.id.tvPrice);
        Button btnClose = findViewById(R.id.btnClose);

        Intent intent = getIntent();
        int number = intent.getExtras().getInt("number");

        dbHelper helper = new dbHelper(this, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT S_State, S_Kindness, S_Term, price FROM satisficationTable WHERE p_num = '"+ number +"'",null);

        while (cursor.moveToNext()) {
            ratingBarState.setRating(cursor.getInt(0));
            ratingBarKindness.setRating(cursor.getInt(1));
            ratingBarTerm.setRating(cursor.getInt(2));
            tvPrice.setText(String.valueOf(cursor.getInt(3)));
        }

        db.close();
        cursor.close();

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
