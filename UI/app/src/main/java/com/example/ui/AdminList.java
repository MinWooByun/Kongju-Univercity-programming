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

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        TextView tvList = findViewById(R.id.tvList);
        ListView listView = findViewById(R.id.listView);
        Button btnBack = findViewById(R.id.btnBack);

        dbHelper helper = new dbHelper(this, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM repairManTable WHERE isproof = 0",null);

        Intent intent = getIntent();
        u_id = intent.getExtras().getString("u_id");

        List<String> list = new ArrayList<>();
        if(cursor.getCount() == 0) {
            tvList.setText("승인을 요청한 수리기사가 아직 존재하지 않습니다.");
        } else {
            while (cursor.moveToNext()) {
                list.add(cursor.getString(0));
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AdminList.this, AdminAuthority.class);
                intent.putExtra("id", list.get(position));
                intent.putExtra("u_id", u_id);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminList.this, noticeBoardActivity.class);
                intent.putExtra("u_id", u_id);
                intent.putExtra("type", 0);
                startActivity(intent);
            }
        });
    }
    //뒤로가기 처리
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AdminList.this, noticeBoardActivity.class);
        intent.putExtra("u_id", u_id);
        intent.putExtra("type", 0);
        startActivity(intent);
        finish();
    }
}