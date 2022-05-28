package com.example.ui;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class noticeBoardActivity extends AppCompatActivity {

    dbHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_board);

        Intent intent = getIntent();
        String u_id= intent.getExtras().getString("u_id");
        int type = intent.getExtras().getInt("type");
        Log.v("intent:" , u_id + type);
        Log.v("start", "시작");
        dbHelper = new dbHelper(noticeBoardActivity.this, 1);

        Button btnRequestRegister = (Button) findViewById(R.id.btnReqeustRegister);
        Button btnCheckPro = (Button) findViewById(R.id.check_proposal);
        Button btnAdminlist = (Button) findViewById(R.id.Admin_list);

        if(type == 2){
           btnAdminlist.setVisibility(View.GONE);
        }
        else if(type==1){
            btnRequestRegister.setVisibility(View.GONE);
            btnAdminlist.setVisibility(View.GONE);
        }
        else{
            btnRequestRegister.setVisibility(View.GONE);
        }


        ListView listView = findViewById(R.id.contentsBar);
        ImageButton btnMypage = (ImageButton) findViewById(R.id.mypage_btn);

        ArrayList<ListItem> list = dbHelper.getAllTitles();


        ListItemAdapter adapter = new ListItemAdapter(this, list);
        listView.setAdapter(adapter);

        //수리 의뢰 작성 클릭
        btnRequestRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(noticeBoardActivity.this ,RequestRegisterActivity.class);
                startActivity(intent);
                intent.putExtra("uId", u_id);
            }
        });

        //견적 확인 버튼 클릭
        btnCheckPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(noticeBoardActivity.this ,Proposal_Back.class);
                startActivity(intent);
                intent.putExtra("u_id", u_id);
                intent.putExtra("type", type);
            }
        });

        //마이 페이지 버튼
        btnMypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                if(type == 1)
                    intent = new Intent(noticeBoardActivity.this ,MyPageRepairMan.class);
                else if(type==0 || type==2)
                    intent = new Intent(noticeBoardActivity.this ,MyPageUser.class);

                    startActivity(intent);
                    intent.putExtra("u_id", u_id);



            }
        });

        //권한 승인 버튼 클릭
        btnAdminlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(noticeBoardActivity.this ,AdminList.class);
                startActivity(intent);
                intent.putExtra("u_id", u_id);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(noticeBoardActivity.this, RequestDetailActivity.class);
                ListItem item = (ListItem) parent.getItemAtPosition(position);
                int number = item.getNumber();
                Log.v("number", String.valueOf(number));
                intent.putExtra("number", number);
                startActivity(intent);
            }
        });
    }
}