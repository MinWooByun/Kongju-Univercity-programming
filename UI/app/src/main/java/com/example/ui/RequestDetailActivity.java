package com.example.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RequestDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_detail);

        Intent intent = getIntent();;
        String u_id= intent.getExtras().getString("u_id");
        int type = intent.getExtras().getInt("type");

        Button btnProposal = (Button) findViewById(R.id.btnProposal);

        if(type!=1)
            btnProposal.setVisibility(View.GONE);

        btnProposal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RequestDetailActivity.this ,Proposal_Suggest.class);
                startActivity(intent);
                intent.putExtra("uId", u_id);
            }
        });
    }
}