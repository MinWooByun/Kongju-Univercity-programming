package com.example.ui;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class dbHelper extends SQLiteOpenHelper {
    static final String DB_Name = "HL_DB.db";

    public dbHelper(Context context, int version){
        super(context, DB_Name, null, version);
    }

   @Override
    public void onCreate(SQLiteDatabase db) {
    }

    public ArrayList<ListItem> getAllTitles(){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT number, title FROM repairRequestTable");

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(sb.toString(), null);

        ArrayList<ListItem> titles = new ArrayList<ListItem>();
        String title = null;
        Log.v("call", "불러오기");
        while(cursor.moveToNext()){
            Log.v("item: ",cursor.getInt(0)+ cursor.getString(1));
            ListItem item = new ListItem(cursor.getInt(0), cursor.getString(1));
            titles.add(item);
        }
        return titles;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
