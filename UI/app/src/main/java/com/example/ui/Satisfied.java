package com.example.ui;

import android.app.Activity;
import android.content.Intent;
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

        Intent intent = getIntent();
        String u_id = intent.getExtras().getString("u_id");
        int type = intent.getExtras().getInt("type");
        int number = intent.getExtras().getInt("number");

        dbHelper helper = new dbHelper(this, 1);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int state = Math.round(ratingBarState.getRating());
                int kindness = Math.round(ratingBarKindness.getRating());
                int term = Math.round(ratingBarTerm.getRating());
                String price1 = etPrice.getText().toString().trim();
                int price = 0;

                if(price1.equals("")) {
                    price = 0;
                } else {
                    price = Integer.parseInt(price1);
                }

                // RatingBar의 별점들을 최소 1점이라도 선택하도록 유도하는 조건문이다.
                if(state == 0.0 || kindness == 0.0 || term == 0.0 || price == 0) {
                    Toast.makeText(Satisfied.this, "평가를 완료해주세요.", Toast.LENGTH_LONG).show();
                } else {
                    // db의 성공 여부를 result에 넣어주고 토스트 메시지를 띄워 보여준다.
                    long result = helper.satisfiedUpdate(u_id, number, state, kindness, term, price);
                    if(result == -1) {
                        Toast.makeText(Satisfied.this, "오류발생", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Satisfied.this,"리뷰작성 완료", Toast.LENGTH_LONG).show();
                    }
                }

                Intent intent = new Intent(Satisfied.this, RequestDetailActivity.class);
                intent.putExtra("u_id", u_id);
                intent.putExtra("type", type);
                intent.putExtra("number", number);
                intent.putExtra("tag", 2);
                startActivity(intent);
                finish();
            }
        });
    }
}
