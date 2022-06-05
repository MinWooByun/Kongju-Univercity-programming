package com.example.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
        int tag = intent.getExtras().getInt("tag");
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
        Button btnSatisfication = (Button) findViewById(R.id.btnSatisfication);
        Button btnSatisfication_check =  (Button) findViewById(R.id.btnSatisficationcheck);

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
        else
            btnReport_Update.setVisibility(View.GONE);

        //신고_갱신 버튼은 자신의 아이디가 아니거나 관리자는 보이면 안 된다.
        if(!array[0].equals(u_id) || type== 0)
            btnReport_Update.setVisibility(View.GONE);

        //수리기사이며, 인증을 받았을 때만 견적 제시가 보임.
        if(!(type==1 && dbHelper.getIsproof(u_id)== 1 && tag == 0 ))
            btnProposal.setVisibility(View.GONE);


        //자신의 글이 아니면 수정 버튼이 보여지면 안 된다.
        if(!array[0].equals(u_id))
            btnFix.setVisibility(View.GONE);

        //관리자와 자신을 제외하고는 삭제 버튼이 보여지면 안 된다.
        if(!(type==0 || array[0].equals(u_id)))
            btnDelete.setVisibility(View.GONE);

        //만족도 평가 버튼은 자신이 아니고, 수리중인 상태가 아니면 보여지면 안 된다.
        if(tag!=1 || !(array[0].equals(u_id)))
            btnSatisfication.setVisibility(View.GONE);

        //태그가 완료중이 아니면 만족도 평가 확인 버튼이 보여지면 안 된다.
        if(tag!=2)
            btnSatisfication_check.setVisibility(View.GONE);

        //견적제시 버튼
        btnProposal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dbHelper.getSuggestionCount(u_id,type)>=1){
                    Toast.makeText(getApplicationContext(),
                            "이미 견적을 제시하였습니다.",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(RequestDetailActivity.this ,Proposal_Suggest.class);
                    intent.putExtra("r_id", u_id);
                    intent.putExtra("p_num", number);
                    intent.putExtra("type", type);
                    startActivity(intent);
                }
            }
        });

        //기사가 이미 신고한 글이면 신고버튼 비활성
        //신고한적이 없으면 신고버튼 활성(디폴트)
        if(dbHelper.checkReport(u_id, number)){
            //신고이력 존재
            btnReport_Update.setEnabled(false);
        }

        //신고 or 갱신 버튼
        btnReport_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //갱신
               if(type ==2){
                   String getdata =  dbHelper.getRequest(number);
                   String[] array = getdata.split("##,#");
                   dbHelper.deleteRequest(number);
                   dbHelper.insertRequest(array[0],array[1],Integer.parseInt(array[2]),array[3],Integer.parseInt(array[4]),Integer.parseInt(array[5]));
                   Toast.makeText(getApplicationContext(),
                           "갱신되었습니다.",
                           Toast.LENGTH_SHORT).show();
               }
               //신고
               if(type == 1){
                   if(dbHelper.reportRequest(u_id, number)){
                       Toast.makeText(getApplicationContext(),
                               "신고 완료",
                               Toast.LENGTH_SHORT).show();
                       btnReport_Update.setEnabled(false);
                   }else{
                       Toast.makeText(getApplicationContext(),
                               "신고가 누적되어 게시글 삭제",
                               Toast.LENGTH_SHORT).show();
                       Intent intent = getIntent();
                       String u_id = intent.getExtras().getString("u_id");
                       int type = intent.getExtras().getInt("type");
                       intent = new Intent(RequestDetailActivity.this, noticeBoardActivity.class);
                       intent.putExtra("u_id", u_id);
                       intent.putExtra("type", type);
                       startActivity(intent);
                       finish();
                   }
               }
            }
        });

        //수정 버튼
        btnFix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RequestDetailActivity.this ,RequestFixActivity.class);
                intent.putExtra("u_id", u_id);
                intent.putExtra("number", number);
                startActivity(intent);
            }
        });

        //삭제 버튼
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RequestDetailActivity.this ,noticeBoardActivity.class);
                intent.putExtra("u_id", u_id);
                intent.putExtra("type", type);
                dbHelper.deleteRequest(number);
                startActivity(intent);
                finish();
            }
        });

        //만족도 평가 버튼
        btnSatisfication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RequestDetailActivity.this ,Satisfied.class);
                intent.putExtra("u_id", u_id);
                intent.putExtra("number", number);
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });

        //만족도 평가 확인 버튼
        btnSatisfication_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RequestDetailActivity.this ,SatisfiedConfirm.class);
                intent.putExtra("number", number);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = getIntent();
        String u_id = intent.getExtras().getString("u_id");
        int type = intent.getExtras().getInt("type");
        intent = new Intent(RequestDetailActivity.this, noticeBoardActivity.class);
        intent.putExtra("u_id", u_id);
        intent.putExtra("type", type);
        startActivity(intent);
        finish();
    }
}
