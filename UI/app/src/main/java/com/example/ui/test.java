package com.example.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        TextView tv = findViewById(R.id.tv);
        TextView tvState = findViewById(R.id.tvState);
        TextView tvKindness = findViewById(R.id.tvKindness);
        TextView tvTerm = findViewById(R.id.tvTerm);
        TextView tvPrice = findViewById(R.id.tvPrice);
        Button btn = findViewById(R.id.btn);

        dbHelper helper = new dbHelper(this, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM repairManTable WHERE id = 'KWH2'",null);

        int state = 0;
        int kindness = 0;
        int term = 0;
        int price = 0;
        int repairNumber = 0;

        while (cursor.moveToNext()) {
            state = cursor.getInt((3));
            kindness = cursor.getInt(4);
            term = cursor.getInt(5);
            price = cursor.getInt(6);
            repairNumber = cursor.getInt(7);
        }

        if(repairNumber == 0) {
            tv.setText("해당 수리기사는 아직 평가가 존재하지 않습니다.");
        } else {
            tvState.setText(String.valueOf(state));
            tvKindness.setText(String.valueOf(kindness));
            tvTerm.setText(String.valueOf(term));
            tvPrice.setText(String.valueOf(price));
        }

        db.close();
        cursor.close();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(test.this, Satisfied.class);
                intent.putExtra("u_id", "KWH2");
                startActivity(intent);
            }
        });
    }
}
