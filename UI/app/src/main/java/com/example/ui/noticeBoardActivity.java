package com.example.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class noticeBoardActivity extends AppCompatActivity {

    dbHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_board);

        Log.v("start", "시작");
        dbHelper = new dbHelper(noticeBoardActivity.this, 1);

        ListView listView = findViewById(R.id.contentsBar);
        Button btnRequestRegister = (Button) findViewById(R.id.btnReqeustRegister);


        ArrayList<ListItem> list = dbHelper.getAllTitles();


        ListItemAdapter adapter = new ListItemAdapter(this, list);
        listView.setAdapter(adapter);

        btnRequestRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(noticeBoardActivity.this ,RequestRegisterActivity.class);
                startActivity(intent);
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