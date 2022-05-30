package com.example.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class RequestDetailActivity extends AppCompatActivity {
    Resources res;
    dbHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_detail);


        res=getResources();
        //intent 값 가져오기, helper 설정
        Intent intent = getIntent();
        String u_id= intent.getExtras().getString("u_id");
        int type = intent.getExtras().getInt("type");
        int number = intent.getExtras().getInt("number");
        dbHelper = new dbHelper(RequestDetailActivity.this, 1);
        String[] category = res.getStringArray(R.array.카테고리);
        String[] symptom = res.getStringArray(R.array.고장증상);

        //xml과 연동
        Button btnProposal = (Button) findViewById(R.id.btnProposal);
        Button btnReport_Update = (Button) findViewById(R.id.btnReport_Update);
        ImageButton btnFix = (ImageButton) findViewById(R.id.btn_fix);
        ImageButton btnDelete = (ImageButton) findViewById(R.id.btn_delete);
        TextView Rd_id = (TextView)findViewById(R.id.Rd_id);
        TextView Rd_title = (TextView)findViewById(R.id.Rd_title);
        TextView Rd_category = (TextView)findViewById(R.id.Rd_category);
        TextView Rd_symptom = (TextView)findViewById(R.id.Rd_symptom);
        TextView Rd_contents = (TextView)findViewById(R.id.Rd_contents);

        //가져온 db 값들 넣어주기
        String getdata = dbHelper.getRequest(number);
        String[] array = getdata.split("##,#");
        Rd_id.setText("작성자: " + array[0]);
        Rd_title.setText( array[1]);
        Rd_category.setText(category[Integer.parseInt(array[4])]);
        Rd_symptom.setText(symptom[Integer.parseInt(array[2])]);
        Rd_contents.setText(array[3]);


        //버튼 설정 (고객은 갱신, 수리기사는 신고)
        if(type == 2)
            btnReport_Update.setText("갱신");
        else if(type == 1)
            btnReport_Update.setText("신고");



        //수리기사이며, 인증을 받았을 때만 견적 제시가 보임.
        if(type!=1 && dbHelper.getIsproof(u_id)== 1)
            btnProposal.setVisibility(View.GONE);

        //견적제시 버튼
        btnProposal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RequestDetailActivity.this ,Proposal_Suggest.class);
                intent.putExtra("r_id", u_id);
                intent.putExtra("p_num", number);
                startActivity(intent);
            }
        });

        //신고 or 갱신 버튼
        btnReport_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //갱신
               if(type == 0){

               }
               //신고
               if(type == 1){

               }
            }
        });

        //수정 버튼
        btnFix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //삭제 버튼
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RequestDetailActivity.this ,noticeBoardActivity.class);
                intent.putExtra("u_id", u_id);
                intent.putExtra("type", 2);
                dbHelper.deleteRequest(number);
                startActivity(intent);
                finish();
            }
        });


    }
}