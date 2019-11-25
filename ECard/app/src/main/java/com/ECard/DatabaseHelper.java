package com.ECard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "ECard.db";
    public static final String TABLE_NAME = "layouts";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "LAYOUT_NAME";
    public static final String COL_3 = "ORIENTATION";
    public static final String COL_4 = "NAME_POS_X";
    public static final String COL_5 = "NAME_POS_Y";
    public static final String COL_6 = "COMPANY_POS_X";
    public static final String COL_7 = "COMPANY_POS_Y";
    public static final String COL_8 = "ADDRESS_POS_X";
    public static final String COL_9 = "ADDRESS_POS_Y";
    public static final String COL_10 = "EMAIL_POS_X";
    public static final String COL_11 = "EMAIL_POS_Y";
    public static final String COL_12 = "PHONE_POS_X";
    public static final String COL_13 = "PHONE_POS_Y";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,LAYOUT_NAME TEXT,ORIENTATION INTEGER,NAME_POS_X INTEGER,NAME_POS_Y INTEGER,COMPANY_POS_X INTEGER," +
                "COMPANY_POS_Y INTEGER,ADDRESS_POS_X INTEGER,ADDRESS_POS_Y INTEGER,EMAIL_POS_X INTEGER,EMAIL_POS_Y INTEGER,PHONE_POS_X INTEGER,PHONE_POS_Y INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String layout_name, String orientation, String name_pos_x, String name_pos_y, String company_pos_x, String company_pos_y, String address_pos_x, String address_pos_y, String email_pos_x, String email_pos_y, String phone_pos_x, String phone_pos_y) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, layout_name);
        contentValues.put(COL_3, orientation);
        contentValues.put(COL_4, name_pos_x);
        contentValues.put(COL_5, name_pos_y);
        contentValues.put(COL_6, company_pos_x);
        contentValues.put(COL_7, company_pos_y);
        contentValues.put(COL_8, address_pos_x);
        contentValues.put(COL_9, address_pos_y);
        contentValues.put(COL_10, email_pos_x);
        contentValues.put(COL_11, email_pos_y);
        contentValues.put(COL_12, phone_pos_x);
        contentValues.put(COL_13, phone_pos_y);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public List<String> getAllLabels(){
        List<String> labels = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT LAYOUT_NAME FROM layouts", null);

        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }


        return labels;
    }


    public boolean updateData(String id, String layout_name, String orientation, String name_pos_x, String name_pos_y, String company_pos_x, String company_pos_y, String address_pos_x, String address_pos_y, String email_pos_x, String email_pos_y, String phone_pos_x, String phone_pos_y) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, layout_name);
        contentValues.put(COL_3, orientation);
        contentValues.put(COL_4, name_pos_x);
        contentValues.put(COL_5, name_pos_y);
        contentValues.put(COL_6, company_pos_x);
        contentValues.put(COL_7, company_pos_y);
        contentValues.put(COL_8, address_pos_x);
        contentValues.put(COL_9, address_pos_y);
        contentValues.put(COL_10, email_pos_x);
        contentValues.put(COL_11, email_pos_y);
        contentValues.put(COL_12, phone_pos_x);
        contentValues.put(COL_13, phone_pos_y);
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        return true;
    }

    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[]{id});
    }
}