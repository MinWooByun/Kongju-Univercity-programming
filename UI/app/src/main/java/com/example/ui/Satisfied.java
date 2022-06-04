package com.example.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class Satisfied extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.satisfied);

        RatingBar ratingBarState = findViewById(R.id.ratingBarState);
        RatingBar ratingBarKindness = findViewById(R.id.ratingBarKindness);
        RatingBar ratingBarTerm = findViewById(R.id.ratingBarTerm);
        EditText etPrice = findViewById(R.id.etPrice);
        Button btnSubmit = findViewById(R.id.btnSubmit);

        dbHelper helper = new dbHelper(this, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT repairNumber FROM repairManTable WHERE id = 'KWH2'",null);

        db.close();
        cursor.close();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int state = Math.round(ratingBarState.getRating());
                int kindness = Math.round(ratingBarKindness.getRating());
                int term = Math.round(ratingBarTerm.getRating());
                int price = 0;
                String price1 = etPrice.getText().toString().trim();

                if(price1.equals("")) {
                    price = 0;
                } else {
                    price = Integer.parseInt(price1);
                }

                if(state == 0.0 || kindness == 0.0 || term == 0.0 || price == 0) {
                    Toast.makeText(Satisfied.this, "평가를 완료해주세요.", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = getIntent();
                    String u_id = intent.getExtras().getString("u_id");
                    long result = helper.satisfiedUpdate(u_id, state, kindness, term, price);
                    if(result == -1) {
                        Toast.makeText(Satisfied.this, "오류발생", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Satisfied.this,"리뷰작성 완료", Toast.LENGTH_LONG).show();
                    }
                }

                Intent intent = new Intent(Satisfied.this, test.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
