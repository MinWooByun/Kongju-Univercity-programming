package com.example.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class AdminAuthority extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_authority);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Button btnApprove = findViewById(R.id.btnApprove);
        Button btnRefusal = findViewById(R.id.btnRefusal);

        Intent intent = getIntent();
        String removeList= intent.getExtras().getString("removeList");

        MyDatabaseHelper myDb = new MyDatabaseHelper(this);

        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminAuthority.this, AdminList.class);
//                intent.putExtra("position", position);
                boolean isDelect = myDb.listRemove(removeList);
                if(isDelect == true) {
                    Toast.makeText(AdminAuthority.this,"성공", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AdminAuthority.this,"실패", Toast.LENGTH_LONG).show();
                }
                startActivity(intent);
                finish();
            }
        });

        btnRefusal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminAuthority.this, AdminList.class);
//                intent.putExtra("position", position);
                boolean isDelect = myDb.listRemove(removeList);
                if(isDelect == true) {
                    Toast.makeText(AdminAuthority.this,"성공", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AdminAuthority.this,"실패", Toast.LENGTH_LONG).show();
                }
                startActivity(intent);
                finish();
            }
        });
    }
}
