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

        //intent 값 가져오기, helper 설정
        Intent intent = getIntent();
        String u_id= intent.getExtras().getString("u_id");
        int type = intent.getExtras().getInt("type");
        dbHelper = new dbHelper(noticeBoardActivity.this, 1);

        //버튼 매치
        Button btnReqeustRegister = (Button) findViewById(R.id.btnReqeustRegister);
        Button btnCheckPro = (Button) findViewById(R.id.check_proposal);
        Button btnAdminlist = (Button) findViewById(R.id.Admin_list);
        ImageButton btnMypage = (ImageButton) findViewById(R.id.mypage_btn);
        ImageButton btnsearch = (ImageButton) findViewById(R.id.search_btn);

        //버튼 설정 (고객은 의뢰 등록과 견적확인, 수리기사는 견적 확인, 관리자는 권한 승인과 견적 확인)
        if(type == 2){
           btnAdminlist.setVisibility(View.GONE);
        }
        else if(type==1){
            btnReqeustRegister.setVisibility(View.GONE);
            btnAdminlist.setVisibility(View.GONE);
        }
        else{
            btnReqeustRegister.setVisibility(View.GONE);
        }


        //리스트 뷰 설정
        ListView listView = findViewById(R.id.contentsBar);

        //리스트 뷰에 넣을 아이템 설정
        ArrayList<ListItem> list = dbHelper.getAllTitles();


        //리스트뷰에 넣어줌.
        ListItemAdapter adapter = new ListItemAdapter(this, list);
        listView.setAdapter(adapter);

        //수리 의뢰 작성 클릭
        btnReqeustRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(noticeBoardActivity.this ,RequestRegisterActivity.class);
                startActivity(intent);
                intent.putExtra("u_id", u_id);

            }
        });

        //견적 확인 버튼 클릭
        btnCheckPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(noticeBoardActivity.this ,Proposal_Back.class);
                startActivity(intent);
                intent.putExtra("u_id", u_id);

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

        //제목 클릭
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(noticeBoardActivity.this, RequestDetailActivity.class);
                ListItem item = (ListItem) parent.getItemAtPosition(position);
                int number = item.getNumber();
                intent.putExtra("number", number);
                intent.putExtra("type", type);
                intent.putExtra("u_id", u_id);
                startActivity(intent);
            }
        });
    }
}