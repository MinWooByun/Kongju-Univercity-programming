package com.example.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class noticeBoardActivity extends AppCompatActivity {

    dbHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_board);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


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
        ImageButton btnlogout = (ImageButton) findViewById(R.id.logout_btn);
        EditText search_Etext = (EditText) findViewById(R.id.search_bar);
        Spinner notice_category = (Spinner) findViewById(R.id.category);

        //버튼 설정 (고객은 의뢰 등록과 견적확인, 수리기사는 견적 확인, 관리자는 권한 승인과 견적 확인)
        if(type == 2){
           btnAdminlist.setVisibility(View.GONE);
        }
        else if(type==1){
            btnReqeustRegister.setVisibility(View.GONE);
            btnAdminlist.setVisibility(View.GONE);
        }
        else if(type==0){
            btnCheckPro.setVisibility(View.GONE);
            btnReqeustRegister.setVisibility(View.GONE);
        }




        //리스트 뷰 설정
        ListView listView = findViewById(R.id.contentsBar);


        //카테고리 처리
        notice_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    ArrayList<ListItem> list = dbHelper.getTitles_category(i,notice_category.getCount()-1,u_id);
                    ListItemAdapter adapter = new ListItemAdapter(noticeBoardActivity.this, list);
                    listView.setAdapter(adapter);
                }
                else{
                    ArrayList<ListItem> list = dbHelper.getAllTitles();
                    ListItemAdapter adapter = new ListItemAdapter(noticeBoardActivity.this, list);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

                intent.putExtra("u_id", u_id);
                intent.putExtra("type", type);
                startActivity(intent);
                finish();



            }
        });

        //검색
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ListItem> list = null;

                list = dbHelper.getTitles_search(notice_category.getSelectedItemPosition(),search_Etext.getText().toString());



                ListItemAdapter adapter = new ListItemAdapter(noticeBoardActivity.this, list);
                listView.setAdapter(adapter);
            }
        });

        //로그아웃 버튼
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //자동 로그인 해제
                SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(noticeBoardActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        //제목 클릭
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(noticeBoardActivity.this, RequestDetailActivity.class);
                ListItem item = (ListItem) parent.getItemAtPosition(position);
                int number = item.getNumber();
                int tag = item.getTag();
                intent.putExtra("number", number);
                intent.putExtra("type", type);
                intent.putExtra("u_id", u_id);
                intent.putExtra("tag", tag);
                startActivity(intent);
                finish();
            }
        });


        //수리 의뢰 작성 클릭
        btnReqeustRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dbHelper.getRequestCount(u_id)<5){
                    Intent intent = new Intent(noticeBoardActivity.this ,RequestRegisterActivity.class);
                    intent.putExtra("u_id", u_id);
                    startActivity(intent);
                    finish();
                }
               else{
                    Toast.makeText(getApplicationContext(),
                            "게시글이 5개 초과됩니다.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        //견적 확인 버튼 클릭
        btnCheckPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dbHelper.getSuggestionCount(u_id,type) > 0){
                    Intent intent = new Intent(noticeBoardActivity.this ,ScreenSlidePagerDynamicActivity.class);
                    intent.putExtra("u_id", u_id);
                    intent.putExtra("type", type);
                    startActivity(intent);
                    finish();
                }
               else {
                   if(type==1)
                    Toast.makeText(getApplicationContext(),
                            "제시한 견적이 없습니다.",
                            Toast.LENGTH_SHORT).show();
                   else if(type==2)
                            Toast.makeText(getApplicationContext(),
                            "제시된 견적이 없습니다.",
                            Toast.LENGTH_SHORT).show();

                }

            }
        });

        //권한 승인 버튼 클릭
        btnAdminlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(noticeBoardActivity.this ,AdminList.class);
                intent.putExtra("u_id", u_id);
                startActivity(intent);
                finish();
            }
        });


    }

    //게시글에서 뒤버튼 처리
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String ID;
        String PW;
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", Activity.MODE_PRIVATE);

        ID = sharedPreferences.getString("userID", null);
        PW = sharedPreferences.getString("userPW", null);
        //자동 로그인 정보가 있으면 종료
        //정보가 없으면 로그인 페이지로 넘어감
        if(ID != null && PW != null) {
            finish();
        }else{
            Intent intent = new Intent(noticeBoardActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }
}