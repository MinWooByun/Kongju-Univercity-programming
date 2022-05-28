package com.example.ui;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "HL_BD.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "userTable";
    private static final String TABLE_NAME1 = "repairManTable";
    private static final String TABLE_NAME2 = "conditionsListTable";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PW = "pw";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_NICKNAME = "nickname";
    private static final String COLUMN_ISPROOF = "isproof";
    private static final String COLUMN_OPENLINK = "openlink";
    private static final String COLUMN_NAME = "name";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " TEXT, "
                + COLUMN_PW + " TEXT, "
                + COLUMN_TYPE + " TEXT, "
                + COLUMN_NICKNAME + " TEXT);";
        db.execSQL(query);

        String query1 = "CREATE TABLE " + TABLE_NAME1 + " ("
                + COLUMN_ID + " TEXT, "
                + COLUMN_PW + " TEXT, "
                + COLUMN_ISPROOF + " TEXT, "
                + COLUMN_OPENLINK + " TEXT);";
        db.execSQL(query1);

        String query2 = "CREATE TABLE " + TABLE_NAME2 + " ("
                + COLUMN_ID + " TEXT, "
                + COLUMN_NAME + " TEXT);";
        db.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        onCreate(db);
    }

    void userInsertData(String id, String pw, String type, String nickname) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, id);
        contentValues.put(COLUMN_PW, pw);
        contentValues.put(COLUMN_TYPE, type);
        contentValues.put(COLUMN_NICKNAME, nickname);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1) {
            Toast.makeText(context, "실패", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "성공", Toast.LENGTH_SHORT).show();
        }
    }

    void repairManInsertData(String id, String pw, String isproof, String openlink) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, id);
        contentValues.put(COLUMN_PW, pw);
        contentValues.put(COLUMN_ISPROOF, isproof);
        contentValues.put(COLUMN_OPENLINK, openlink);
        long result = db.insert(TABLE_NAME1, null, contentValues);
        if(result == -1) {
            Toast.makeText(context, "실패", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "성공", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean userUpdateData(String pw, String nickName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PW, pw);
        db.update(TABLE_NAME, contentValues, "nickname = ?", new String[] {nickName});
        return true;
    }

    public boolean repairManUpdateData(String pw, String nickName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PW, pw);
        db.update(TABLE_NAME1, contentValues, "id = ?", new String[] {nickName});
        return true;
    }

    public boolean listRemove(String removeList) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME2, "name = ?", new String[] {removeList});
        return true;
    }
}
