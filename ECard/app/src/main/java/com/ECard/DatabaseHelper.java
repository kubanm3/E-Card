package com.ECard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.Toast;

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
    public static final class DataEntry implements BaseColumns {
        public static final String TABLE_NAME_DATA = "data";
        public static final String DATA_ID = "ID";
        public static final String DATA_NAME = "NAME";
        public static final String DATA_COMPANY_NAME = "COMPANY_NAME";
        public static final String DATA_ADDRESS = "ADDRESS";
        public static final String DATA_EMAIL = "EMAIL";
        public static final String DATA_PHONE = "PHONE";
        public static final String DATA_LAYOUT_ID = "LAYOUT_ID";
    }

    private static final String CREATE_TABLE_LAYOUTS =
            "CREATE TABLE "
                    + TABLE_NAME_LAYOUTS + " ("
                    + LAYOUTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + LAYOUTS_NAME + " TEXT, "
                    + LAYOUTS_ORIENTATION + " INTEGER,"
                    + LAYOUTS_NAME_POS_X + " INTEGER, "
                    + LAYOUTS_NAME_POS_Y + " INTEGER, "
                    + LAYOUTS_COMPANY_POS_X + " INTEGER, "
                    + LAYOUTS_COMPANY_POS_Y + " INTEGER, "
                    + LAYOUTS_ADDRESS_POS_X + " INTEGER, "
                    + LAYOUTS_ADDRESS_POS_Y + " INTEGER, "
                    + LAYOUTS_EMAIL_POS_X + " INTEGER, "
                    + LAYOUTS_EMAIL_POS_Y + " INTEGER, "
                    + LAYOUTS_PHONE_POS_X + " INTEGER, "
                    + LAYOUTS_PHONE_POS_Y + " INTEGER)";

    private static final String CREATE_TABLE_DATA =
            "CREATE TABLE "
                    + DataEntry.TABLE_NAME_DATA + " ("
                    + DataEntry.DATA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DataEntry.DATA_NAME + " TEXT, "
                    + DataEntry.DATA_COMPANY_NAME + " TEXT, "
                    + DataEntry.DATA_ADDRESS + " TEXT, "
                    + DataEntry.DATA_EMAIL + " TEXT, "
                    + DataEntry.DATA_PHONE + " INTEGER, "
                    + DataEntry.DATA_LAYOUT_ID + " INTEGER, " +
                    "FOREIGN KEY(" + DataEntry.DATA_LAYOUT_ID + ") REFERENCES " + TABLE_NAME_LAYOUTS + "(" + LAYOUTS_ID + "))";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_LAYOUTS);
        db.execSQL(CREATE_TABLE_DATA);
        putDefaultValueLayouts(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LAYOUTS);
        db.execSQL("DROP TABLE IF EXISTS " + DataEntry.TABLE_NAME_DATA);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            db.setForeignKeyConstraintsEnabled(true);
        } else {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
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

    public boolean insertDataData(String data_name, String data_company_name, String data_address, String data_email, String data_phone, Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataEntry.DATA_NAME, data_name);
        contentValues.put(DataEntry.DATA_COMPANY_NAME, data_company_name);
        contentValues.put(DataEntry.DATA_ADDRESS, data_address);
        contentValues.put(DataEntry.DATA_EMAIL, data_email);
        contentValues.put(DataEntry.DATA_PHONE, data_phone);
        contentValues.put(DataEntry.DATA_LAYOUT_ID, id);
        long result = db.insert(DataEntry.TABLE_NAME_DATA, null, contentValues);
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

    public Cursor getAllItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(
                DataEntry.TABLE_NAME_DATA,
                null,
                null,
                null,
                null,
                null,
                DataEntry.DATA_ID + " ASC"
        );
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
        if (layout_name != null) contentValues.put(LAYOUTS_NAME, layout_name);
        if (orientation != null) contentValues.put(LAYOUTS_ORIENTATION, orientation);
        if (name_pos_x != null) contentValues.put(LAYOUTS_NAME_POS_X, name_pos_x);
        if (name_pos_y != null) contentValues.put(LAYOUTS_NAME_POS_Y, name_pos_y);
        if (company_pos_x != null) contentValues.put(LAYOUTS_COMPANY_POS_X, company_pos_x);
        if (company_pos_y != null) contentValues.put(LAYOUTS_COMPANY_POS_Y, company_pos_y);
        if (address_pos_x != null) contentValues.put(LAYOUTS_ADDRESS_POS_X, address_pos_x);
        if (address_pos_y != null) contentValues.put(LAYOUTS_ADDRESS_POS_Y, address_pos_y);
        if (email_pos_x != null) contentValues.put(LAYOUTS_EMAIL_POS_X, email_pos_x);
        if (email_pos_y != null) contentValues.put(LAYOUTS_EMAIL_POS_Y, email_pos_y);
        if (phone_pos_x != null) contentValues.put(LAYOUTS_PHONE_POS_X, phone_pos_x);
        if (phone_pos_y != null) contentValues.put(LAYOUTS_PHONE_POS_Y, phone_pos_y);
        db.update(TABLE_NAME_LAYOUTS, contentValues, "ID = ?", new String[]{id});
        return true;
    }

    public Integer deleteDataLayout(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_LAYOUTS, "ID = ?", new String[]{id});
    }

    public Integer deleteDataData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(DataEntry.TABLE_NAME_DATA, "ID = ?", new String[]{id});
    }

    public Cursor getDataData(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =
                db.rawQuery("select D.NAME, D.COMPANY_NAME, D.ADDRESS, D.EMAIL, D.PHONE, L.LAYOUT_NAME, L.ID FROM " + DataEntry.TABLE_NAME_DATA + " D LEFT JOIN " + TABLE_NAME_LAYOUTS + " L ON D.LAYOUT_ID = L.ID WHERE D.ID = " + id, null);
        return res;
    }

    private void putDefaultValueLayouts(SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(LAYOUTS_NAME, "Default");
        contentValues.put(LAYOUTS_ORIENTATION, "1");
        contentValues.put(LAYOUTS_NAME_POS_X, "5");
        contentValues.put(LAYOUTS_NAME_POS_Y, "20");
        contentValues.put(LAYOUTS_COMPANY_POS_X, "5");
        contentValues.put(LAYOUTS_COMPANY_POS_Y, "40");
        contentValues.put(LAYOUTS_ADDRESS_POS_X, "5");
        contentValues.put(LAYOUTS_ADDRESS_POS_Y, "60");
        contentValues.put(LAYOUTS_EMAIL_POS_X, "5");
        contentValues.put(LAYOUTS_EMAIL_POS_Y, "90");
        contentValues.put(LAYOUTS_PHONE_POS_X, "5");
        contentValues.put(LAYOUTS_PHONE_POS_Y, "110");
        db.insert(TABLE_NAME_LAYOUTS, null, contentValues);

        contentValues.put(LAYOUTS_NAME, "Default 2");
        contentValues.put(LAYOUTS_ORIENTATION, "1");
        contentValues.put(LAYOUTS_NAME_POS_X, "20");
        contentValues.put(LAYOUTS_NAME_POS_Y, "20");
        contentValues.put(LAYOUTS_COMPANY_POS_X, "20");
        contentValues.put(LAYOUTS_COMPANY_POS_Y, "40");
        contentValues.put(LAYOUTS_ADDRESS_POS_X, "20");
        contentValues.put(LAYOUTS_ADDRESS_POS_Y, "60");
        contentValues.put(LAYOUTS_EMAIL_POS_X, "100");
        contentValues.put(LAYOUTS_EMAIL_POS_Y, "100");
        contentValues.put(LAYOUTS_PHONE_POS_X, "100");
        contentValues.put(LAYOUTS_PHONE_POS_Y, "120");
        db.insert(TABLE_NAME_LAYOUTS, null, contentValues);

        contentValues.put(LAYOUTS_NAME, "Default 3");
        contentValues.put(LAYOUTS_ORIENTATION, "1");
        contentValues.put(LAYOUTS_NAME_POS_X, "20");
        contentValues.put(LAYOUTS_NAME_POS_Y, "20");
        contentValues.put(LAYOUTS_COMPANY_POS_X, "20");
        contentValues.put(LAYOUTS_COMPANY_POS_Y, "40");
        contentValues.put(LAYOUTS_ADDRESS_POS_X, "110");
        contentValues.put(LAYOUTS_ADDRESS_POS_Y, "60");
        contentValues.put(LAYOUTS_EMAIL_POS_X, "110");
        contentValues.put(LAYOUTS_EMAIL_POS_Y, "80");
        contentValues.put(LAYOUTS_PHONE_POS_X, "110");
        contentValues.put(LAYOUTS_PHONE_POS_Y, "100");
        db.insert(TABLE_NAME_LAYOUTS, null, contentValues);
    }
}