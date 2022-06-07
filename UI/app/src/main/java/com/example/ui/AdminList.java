package com.example.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AdminList extends AppCompatActivity {
    String u_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_list);

        // 타이틀바 제거
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        TextView tvList = findViewById(R.id.tvList);
        ListView listView = findViewById(R.id.listView);
        Button btnBack = findViewById(R.id.btnBack);

        // db선언 및 id 조회
        dbHelper helper = new dbHelper(this, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM repairManTable WHERE isproof = 0",null);

        // 이전 액티비티에서 유저 id를 가져온다.
        Intent intent = getIntent();
        u_id = intent.getExtras().getString("u_id");

        // 리스트에 승인을 받지 않은 id 추가
        List<String> list = new ArrayList<>();
        if(cursor.getCount() == 0) {
            tvList.setText("승인을 요청한 수리기사가 아직 존재하지 않습니다.");
        } else {
            while (cursor.moveToNext()) {
                list.add(cursor.getString(0));
            }
        }

        db.close();
        cursor.close();

        // 리스트뷰를 어댑터에 연결
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AdminList.this, AdminAuthority.class);
                intent.putExtra("id", list.get(position));
                intent.putExtra("u_id", u_id);
                startActivity(intent);
                finish();
            }
        });

        // 돌아가기 버튼 처리
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminList.this, noticeBoardActivity.class);
                // putExtra를 하는 이유는 이전 레이아웃인 게시판 레이아웃으로 돌아가게 되면 유저 id와 타입이 필요하기 때문이다.
                intent.putExtra("u_id", u_id);
                intent.putExtra("type", 0);
                startActivity(intent);
                finish();
            }
        });
    }

    // 안드로이드에 기본으로 내장되어 있는 뒤로가기 버튼에 대한 이벤트 처리 위의 돌아가기 버튼과 동일한 역할을 한다.
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AdminList.this, noticeBoardActivity.class);
        intent.putExtra("u_id", u_id);
        intent.putExtra("type", 0);
        startActivity(intent);
        finish();
    }
}