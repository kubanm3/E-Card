package com.ECard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    //table for layouts

    private static final String DATABASE_NAME = "ECard.db";

    public static final class LayoutEntry implements BaseColumns {
        public static final String TABLE_NAME_LAYOUTS = "layouts";
        public static final String LAYOUTS_ID = "ID";
        public static final String LAYOUTS_NAME = "LAYOUT_NAME";
        public static final String LAYOUTS_ORIENTATION = "ORIENTATION";
        public static final String LAYOUTS_NAME_POS_X = "NAME_POS_X";
        public static final String LAYOUTS_NAME_POS_Y = "NAME_POS_Y";
        public static final String LAYOUTS_NAME_FONT = "NAME_FONT";
        public static final String LAYOUTS_COMPANY_POS_X = "COMPANY_POS_X";
        public static final String LAYOUTS_COMPANY_POS_Y = "COMPANY_POS_Y";
        public static final String LAYOUTS_COMPANY_FONT = "COMPANY_FONT";
        public static final String LAYOUTS_ADDRESS_POS_X = "ADDRESS_POS_X";
        public static final String LAYOUTS_ADDRESS_POS_Y = "ADDRESS_POS_Y";
        public static final String LAYOUTS_ADDRESS_FONT = "ADDRESS_FONT";
        public static final String LAYOUTS_EMAIL_POS_X = "EMAIL_POS_X";
        public static final String LAYOUTS_EMAIL_POS_Y = "EMAIL_POS_Y";
        public static final String LAYOUTS_EMAIL_FONT = "EMAIL_FONT";
        public static final String LAYOUTS_PHONE_POS_X = "PHONE_POS_X";
        public static final String LAYOUTS_PHONE_POS_Y = "PHONE_POS_Y";
        public static final String LAYOUTS_PHONE_FONT = "PHONE_FONT";
    }

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
                    + LayoutEntry.TABLE_NAME_LAYOUTS + " ("
                    + LayoutEntry.LAYOUTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + LayoutEntry.LAYOUTS_NAME + " TEXT, "
                    + LayoutEntry.LAYOUTS_ORIENTATION + " INTEGER,"
                    + LayoutEntry.LAYOUTS_NAME_POS_X + " INTEGER, "
                    + LayoutEntry.LAYOUTS_NAME_POS_Y + " INTEGER, "
                    + LayoutEntry.LAYOUTS_NAME_FONT + " INTEGER, "
                    + LayoutEntry.LAYOUTS_COMPANY_POS_X + " INTEGER, "
                    + LayoutEntry.LAYOUTS_COMPANY_POS_Y + " INTEGER, "
                    + LayoutEntry.LAYOUTS_COMPANY_FONT + " INTEGER, "
                    + LayoutEntry.LAYOUTS_ADDRESS_POS_X + " INTEGER, "
                    + LayoutEntry.LAYOUTS_ADDRESS_POS_Y + " INTEGER, "
                    + LayoutEntry.LAYOUTS_ADDRESS_FONT + " INTEGER, "
                    + LayoutEntry.LAYOUTS_EMAIL_POS_X + " INTEGER, "
                    + LayoutEntry.LAYOUTS_EMAIL_POS_Y + " INTEGER, "
                    + LayoutEntry.LAYOUTS_EMAIL_FONT + " INTEGER, "
                    + LayoutEntry.LAYOUTS_PHONE_POS_X + " INTEGER, "
                    + LayoutEntry.LAYOUTS_PHONE_POS_Y + " INTEGER, "
                    + LayoutEntry.LAYOUTS_PHONE_FONT + " INTEGER)";

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
                    "FOREIGN KEY(" + DataEntry.DATA_LAYOUT_ID + ") REFERENCES " + LayoutEntry.TABLE_NAME_LAYOUTS + "(" + LayoutEntry.LAYOUTS_ID + "))";

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
        db.execSQL("DROP TABLE IF EXISTS " + LayoutEntry.TABLE_NAME_LAYOUTS);
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

    public boolean insertDataLayout(String layout_name, String orientation, String name_pos_x, String name_pos_y, String name_font,
                                    String company_pos_x, String company_pos_y, String company_font, String address_pos_x, String address_pos_y, String address_font,
                                    String email_pos_x, String email_pos_y, String email_font, String phone_pos_x, String phone_pos_y, String phone_font) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LayoutEntry.LAYOUTS_NAME, layout_name);
        contentValues.put(LayoutEntry.LAYOUTS_ORIENTATION, orientation);
        contentValues.put(LayoutEntry.LAYOUTS_NAME_POS_X, name_pos_x);
        contentValues.put(LayoutEntry.LAYOUTS_NAME_POS_Y, name_pos_y);
        contentValues.put(LayoutEntry.LAYOUTS_NAME_FONT, name_font);
        contentValues.put(LayoutEntry.LAYOUTS_COMPANY_POS_X, company_pos_x);
        contentValues.put(LayoutEntry.LAYOUTS_COMPANY_POS_Y, company_pos_y);
        contentValues.put(LayoutEntry.LAYOUTS_COMPANY_FONT, company_font);
        contentValues.put(LayoutEntry.LAYOUTS_ADDRESS_POS_X, address_pos_x);
        contentValues.put(LayoutEntry.LAYOUTS_ADDRESS_POS_Y, address_pos_y);
        contentValues.put(LayoutEntry.LAYOUTS_ADDRESS_FONT, address_font);
        contentValues.put(LayoutEntry.LAYOUTS_EMAIL_POS_X, email_pos_x);
        contentValues.put(LayoutEntry.LAYOUTS_EMAIL_POS_Y, email_pos_y);
        contentValues.put(LayoutEntry.LAYOUTS_EMAIL_FONT, email_font);
        contentValues.put(LayoutEntry.LAYOUTS_PHONE_POS_X, phone_pos_x);
        contentValues.put(LayoutEntry.LAYOUTS_PHONE_POS_Y, phone_pos_y);
        contentValues.put(LayoutEntry.LAYOUTS_PHONE_FONT, phone_font);
        long result = db.insert(LayoutEntry.TABLE_NAME_LAYOUTS, null, contentValues);
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
        Cursor res = db.rawQuery("select * from " + LayoutEntry.TABLE_NAME_LAYOUTS, null);
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
        Cursor res =
                db.rawQuery("select * from " + LayoutEntry.TABLE_NAME_LAYOUTS + " WHERE ID = " + id, null);
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

    public boolean updateLayout(String id, String layout_name, String orientation, String name_pos_x, String name_pos_y, String name_font,
                                String company_pos_x, String company_pos_y, String company_font, String address_pos_x, String address_pos_y, String address_font,
                                String email_pos_x, String email_pos_y, String email_font, String phone_pos_x, String phone_pos_y, String phone_font) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if (!id.equals(""))
            contentValues.put(LayoutEntry.LAYOUTS_ID, id);
        else
            return false;
        if (layout_name != null)
            contentValues.put(LayoutEntry.LAYOUTS_NAME, layout_name);
        if (orientation != null)
            contentValues.put(LayoutEntry.LAYOUTS_ORIENTATION, orientation);
        if (name_pos_x != null)
            contentValues.put(LayoutEntry.LAYOUTS_NAME_POS_X, name_pos_x);
        if (name_pos_y != null)
            contentValues.put(LayoutEntry.LAYOUTS_NAME_POS_Y, name_pos_y);
        if (name_font != null)
            contentValues.put(LayoutEntry.LAYOUTS_NAME_FONT, name_font);
        if (company_pos_x != null)
            contentValues.put(LayoutEntry.LAYOUTS_COMPANY_POS_X, company_pos_x);
        if (company_pos_y != null)
            contentValues.put(LayoutEntry.LAYOUTS_COMPANY_POS_Y, company_pos_y);
        if (company_font != null)
            contentValues.put(LayoutEntry.LAYOUTS_COMPANY_FONT, company_font);
        if (address_pos_x != null)
            contentValues.put(LayoutEntry.LAYOUTS_ADDRESS_POS_X, address_pos_x);
        if (address_pos_y != null)
            contentValues.put(LayoutEntry.LAYOUTS_ADDRESS_POS_Y, address_pos_y);
        if (address_font != null)
            contentValues.put(LayoutEntry.LAYOUTS_ADDRESS_FONT, address_font);
        if (email_pos_x != null)
            contentValues.put(LayoutEntry.LAYOUTS_EMAIL_POS_X, email_pos_x);
        if (email_pos_y != null)
            contentValues.put(LayoutEntry.LAYOUTS_EMAIL_POS_Y, email_pos_y);
        if (email_font != null)
            contentValues.put(LayoutEntry.LAYOUTS_EMAIL_FONT, email_font);
        if (phone_pos_x != null)
            contentValues.put(LayoutEntry.LAYOUTS_PHONE_POS_X, phone_pos_x);
        if (phone_pos_y != null)
            contentValues.put(LayoutEntry.LAYOUTS_PHONE_POS_Y, phone_pos_y);
        if (phone_font != null)
            contentValues.put(LayoutEntry.LAYOUTS_PHONE_FONT, phone_font);
        db.update(LayoutEntry.TABLE_NAME_LAYOUTS, contentValues, "ID = ?", new String[]{id});
        return true;
    }

    public boolean updateData(String id, String layout_id, String name, String company, String address, String email, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if (!id.equals(""))
            contentValues.put(DataEntry.DATA_ID, id);
        else
            return false;
        if (layout_id != null)
            contentValues.put(DataEntry.DATA_LAYOUT_ID, layout_id);
        if (name != null)
            contentValues.put(DataEntry.DATA_NAME, name);
        if (company != null)
            contentValues.put(DataEntry.DATA_COMPANY_NAME, company);
        if (address != null)
            contentValues.put(DataEntry.DATA_ADDRESS, address);
        if (email != null)
            contentValues.put(DataEntry.DATA_EMAIL, email);
        if (phone != null)
            contentValues.put(DataEntry.DATA_PHONE, phone);
        db.update(DataEntry.TABLE_NAME_DATA, contentValues, "ID = ?", new String[]{id});
        return true;
    }

    public Integer deleteDataLayout(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(LayoutEntry.TABLE_NAME_LAYOUTS, "ID = ?", new String[]{id});
    }

    public Integer deleteDataData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(DataEntry.TABLE_NAME_DATA, "ID = ?", new String[]{id});
    }

    public Cursor getDataData(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =
                db.rawQuery("select D.NAME, D.COMPANY_NAME, D.ADDRESS, D.EMAIL, D.PHONE, L.LAYOUT_NAME, L.ID, D.ID FROM " + DataEntry.TABLE_NAME_DATA + " D LEFT JOIN "
                        + LayoutEntry.TABLE_NAME_LAYOUTS + " L ON D.LAYOUT_ID = L.ID WHERE D.ID = " + id, null);
        return res;
    }

    private void putDefaultValueLayouts(SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(LayoutEntry.LAYOUTS_NAME, "Default");
        contentValues.put(LayoutEntry.LAYOUTS_ORIENTATION, "1");
        contentValues.put(LayoutEntry.LAYOUTS_NAME_POS_X, "5");
        contentValues.put(LayoutEntry.LAYOUTS_NAME_POS_Y, "24");
        contentValues.put(LayoutEntry.LAYOUTS_NAME_FONT, "1");
        contentValues.put(LayoutEntry.LAYOUTS_COMPANY_POS_X, "5");
        contentValues.put(LayoutEntry.LAYOUTS_COMPANY_POS_Y, "48");
        contentValues.put(LayoutEntry.LAYOUTS_COMPANY_FONT, "1");
        contentValues.put(LayoutEntry.LAYOUTS_ADDRESS_POS_X, "5");
        contentValues.put(LayoutEntry.LAYOUTS_ADDRESS_POS_Y, "72");
        contentValues.put(LayoutEntry.LAYOUTS_ADDRESS_FONT, "0");
        contentValues.put(LayoutEntry.LAYOUTS_EMAIL_POS_X, "5");
        contentValues.put(LayoutEntry.LAYOUTS_EMAIL_POS_Y, "90");
        contentValues.put(LayoutEntry.LAYOUTS_EMAIL_FONT, "0");
        contentValues.put(LayoutEntry.LAYOUTS_PHONE_POS_X, "5");
        contentValues.put(LayoutEntry.LAYOUTS_PHONE_POS_Y, "108");
        contentValues.put(LayoutEntry.LAYOUTS_PHONE_FONT, "0");
        db.insert(LayoutEntry.TABLE_NAME_LAYOUTS, null, contentValues);

        contentValues.put(LayoutEntry.LAYOUTS_NAME, "Default 2");
        contentValues.put(LayoutEntry.LAYOUTS_ORIENTATION, "1");
        contentValues.put(LayoutEntry.LAYOUTS_NAME_POS_X, "20");
        contentValues.put(LayoutEntry.LAYOUTS_NAME_POS_Y, "20");
        contentValues.put(LayoutEntry.LAYOUTS_NAME_FONT, "1");
        contentValues.put(LayoutEntry.LAYOUTS_COMPANY_POS_X, "20");
        contentValues.put(LayoutEntry.LAYOUTS_COMPANY_POS_Y, "40");
        contentValues.put(LayoutEntry.LAYOUTS_COMPANY_FONT, "1");
        contentValues.put(LayoutEntry.LAYOUTS_ADDRESS_POS_X, "20");
        contentValues.put(LayoutEntry.LAYOUTS_ADDRESS_POS_Y, "60");
        contentValues.put(LayoutEntry.LAYOUTS_ADDRESS_FONT, "0");
        contentValues.put(LayoutEntry.LAYOUTS_EMAIL_POS_X, "100");
        contentValues.put(LayoutEntry.LAYOUTS_EMAIL_POS_Y, "100");
        contentValues.put(LayoutEntry.LAYOUTS_EMAIL_FONT, "0");
        contentValues.put(LayoutEntry.LAYOUTS_PHONE_POS_X, "100");
        contentValues.put(LayoutEntry.LAYOUTS_PHONE_POS_Y, "120");
        contentValues.put(LayoutEntry.LAYOUTS_PHONE_FONT, "0");
        db.insert(LayoutEntry.TABLE_NAME_LAYOUTS, null, contentValues);

        contentValues.put(LayoutEntry.LAYOUTS_NAME, "Default 3");
        contentValues.put(LayoutEntry.LAYOUTS_ORIENTATION, "1");
        contentValues.put(LayoutEntry.LAYOUTS_NAME_POS_X, "20");
        contentValues.put(LayoutEntry.LAYOUTS_NAME_POS_Y, "20");
        contentValues.put(LayoutEntry.LAYOUTS_NAME_FONT, "1");
        contentValues.put(LayoutEntry.LAYOUTS_COMPANY_POS_X, "20");
        contentValues.put(LayoutEntry.LAYOUTS_COMPANY_POS_Y, "40");
        contentValues.put(LayoutEntry.LAYOUTS_COMPANY_FONT, "1");
        contentValues.put(LayoutEntry.LAYOUTS_ADDRESS_POS_X, "110");
        contentValues.put(LayoutEntry.LAYOUTS_ADDRESS_POS_Y, "60");
        contentValues.put(LayoutEntry.LAYOUTS_ADDRESS_FONT, "0");
        contentValues.put(LayoutEntry.LAYOUTS_EMAIL_POS_X, "110");
        contentValues.put(LayoutEntry.LAYOUTS_EMAIL_POS_Y, "80");
        contentValues.put(LayoutEntry.LAYOUTS_EMAIL_FONT, "0");
        contentValues.put(LayoutEntry.LAYOUTS_PHONE_POS_X, "110");
        contentValues.put(LayoutEntry.LAYOUTS_PHONE_POS_Y, "100");
        contentValues.put(LayoutEntry.LAYOUTS_PHONE_FONT, "0");
        db.insert(LayoutEntry.TABLE_NAME_LAYOUTS, null, contentValues);
    }
}