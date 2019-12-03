package com.ECard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    //table for layouts
    private static final String DATABASE_NAME = "ECard.db";
    private static final String TABLE_NAME_LAYOUTS = "layouts";
    private static final String LAYOUTS_ID = "ID";
    private static final String LAYOUTS_NAME = "LAYOUT_NAME";
    private static final String LAYOUTS_ORIENTATION = "ORIENTATION";
    private static final String LAYOUTS_NAME_POS_X = "NAME_POS_X";
    private static final String LAYOUTS_NAME_POS_Y = "NAME_POS_Y";
    private static final String LAYOUTS_COMPANY_POS_X = "COMPANY_POS_X";
    private static final String LAYOUTS_COMPANY_POS_Y = "COMPANY_POS_Y";
    private static final String LAYOUTS_ADDRESS_POS_X = "ADDRESS_POS_X";
    private static final String LAYOUTS_ADDRESS_POS_Y = "ADDRESS_POS_Y";
    private static final String LAYOUTS_EMAIL_POS_X = "EMAIL_POS_X";
    private static final String LAYOUTS_EMAIL_POS_Y = "EMAIL_POS_Y";
    private static final String LAYOUTS_PHONE_POS_X = "PHONE_POS_X";
    private static final String LAYOUTS_PHONE_POS_Y = "PHONE_POS_Y";

    //table for data
    private static final String TABLE_NAME_DATA = "data";
    private static final String DATA_ID = "ID";
    private static final String DATA_NAME = "NAME";
    private static final String DATA_COMPANY_NAME = "COMPANY_NAME";
    private static final String DATA_ADDRESS = "ADDRESS";
    private static final String DATA_EMAIL = "EMAIL";
    private static final String DATA_PHONE = "PHONE";

    private static final String CREATE_TABLE_LAYOUTS =
            "CREATE TABLE " + TABLE_NAME_LAYOUTS + " (" + LAYOUTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + LAYOUTS_NAME
                    + " TEXT, " + LAYOUTS_ORIENTATION + " INTEGER," + LAYOUTS_NAME_POS_X + " INTEGER, " + LAYOUTS_NAME_POS_Y + " INTEGER, " + LAYOUTS_COMPANY_POS_X + " INTEGER, "
                    + LAYOUTS_COMPANY_POS_Y + " INTEGER, " + LAYOUTS_ADDRESS_POS_X + " INTEGER, " + LAYOUTS_ADDRESS_POS_Y + " INTEGER, " + LAYOUTS_EMAIL_POS_X + " INTEGER, " + LAYOUTS_EMAIL_POS_Y + " INTEGER, " + LAYOUTS_PHONE_POS_X
                    + " INTEGER, " + LAYOUTS_PHONE_POS_Y + " INTEGER)";

    private static final String CREATE_TABLE_DATA =
            "CREATE TABLE " + TABLE_NAME_DATA + " (" + DATA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DATA_NAME
                    + " TEXT, " + DATA_COMPANY_NAME + " TEXT, " + DATA_ADDRESS + " TEXT, " + DATA_EMAIL + " TEXT, " + DATA_PHONE + " INTEGER)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_LAYOUTS);
        db.execSQL(CREATE_TABLE_DATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LAYOUTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DATA);
        onCreate(db);
    }

    public boolean insertDataLayout(String layout_name, String orientation, String name_pos_x, String name_pos_y,
                                    String company_pos_x, String company_pos_y, String address_pos_x, String address_pos_y,
                                    String email_pos_x, String email_pos_y, String phone_pos_x, String phone_pos_y) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LAYOUTS_NAME, layout_name);
        contentValues.put(LAYOUTS_ORIENTATION, orientation);
        contentValues.put(LAYOUTS_NAME_POS_X, name_pos_x);
        contentValues.put(LAYOUTS_NAME_POS_Y, name_pos_y);
        contentValues.put(LAYOUTS_COMPANY_POS_X, company_pos_x);
        contentValues.put(LAYOUTS_COMPANY_POS_Y, company_pos_y);
        contentValues.put(LAYOUTS_ADDRESS_POS_X, address_pos_x);
        contentValues.put(LAYOUTS_ADDRESS_POS_Y, address_pos_y);
        contentValues.put(LAYOUTS_EMAIL_POS_X, email_pos_x);
        contentValues.put(LAYOUTS_EMAIL_POS_Y, email_pos_y);
        contentValues.put(LAYOUTS_PHONE_POS_X, phone_pos_x);
        contentValues.put(LAYOUTS_PHONE_POS_Y, phone_pos_y);
        long result = db.insert(TABLE_NAME_LAYOUTS, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean insertDataData(String data_name, String data_company_name, String data_address, String data_email, String data_phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATA_NAME, data_name);
        contentValues.put(DATA_COMPANY_NAME, data_company_name);
        contentValues.put(DATA_ADDRESS, data_address);
        contentValues.put(DATA_EMAIL, data_email);
        contentValues.put(DATA_PHONE, data_phone);
        long result = db.insert(TABLE_NAME_DATA, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME_LAYOUTS, null);
        return res;
    }

    public Cursor getLayoutData(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME_LAYOUTS + " WHERE ID = " + id, null);
        return res;
    }

    public Cursor getAllLabels() {
        List<String> labels = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT ID ,LAYOUT_NAME FROM layouts", null);

        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        return cursor;
    }

    public boolean updateData(String id, String layout_name, String orientation, String name_pos_x, String name_pos_y, String company_pos_x, String company_pos_y, String address_pos_x, String address_pos_y, String email_pos_x, String email_pos_y, String phone_pos_x, String phone_pos_y) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if (!id.equals(""))
            contentValues.put(LAYOUTS_ID, id);
        else
            return false;
        if (!layout_name.equals("")) contentValues.put(LAYOUTS_NAME, layout_name);
        if (!orientation.equals("")) contentValues.put(LAYOUTS_ORIENTATION, orientation);
        if (!name_pos_x.equals("")) contentValues.put(LAYOUTS_NAME_POS_X, name_pos_x);
        if (!name_pos_y.equals("")) contentValues.put(LAYOUTS_NAME_POS_Y, name_pos_y);
        if (!company_pos_x.equals("")) contentValues.put(LAYOUTS_COMPANY_POS_X, company_pos_x);
        if (!company_pos_y.equals("")) contentValues.put(LAYOUTS_COMPANY_POS_Y, company_pos_y);
        if (!address_pos_x.equals("")) contentValues.put(LAYOUTS_ADDRESS_POS_X, address_pos_x);
        if (!address_pos_y.equals("")) contentValues.put(LAYOUTS_ADDRESS_POS_Y, address_pos_y);
        if (!email_pos_x.equals("")) contentValues.put(LAYOUTS_EMAIL_POS_X, email_pos_x);
        if (!email_pos_y.equals("")) contentValues.put(LAYOUTS_EMAIL_POS_Y, email_pos_y);
        if (!phone_pos_x.equals("")) contentValues.put(LAYOUTS_PHONE_POS_X, phone_pos_x);
        if (!phone_pos_y.equals("")) contentValues.put(LAYOUTS_PHONE_POS_Y, phone_pos_y);
        db.update(TABLE_NAME_LAYOUTS, contentValues, "ID = ?", new String[]{id});
        return true;
    }

    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_LAYOUTS, "ID = ?", new String[]{id});
    }
}