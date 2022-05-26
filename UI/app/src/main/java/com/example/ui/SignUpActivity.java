package com.example.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class SignUpActivity extends Activity {
    //수리기사 회원가입
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_layout);


        Button btnSubmit = (Button) findViewById(R.id.btnSignup1);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SumbitCertificate.class);
                intent.putExtra("state",1);
                startActivityForResult(intent, 0);
            }
        });
    }
}
