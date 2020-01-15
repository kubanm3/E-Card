package com.ECard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;


public class BaseActivity extends AppCompatActivity {
    ArrayAdapter dataAdapter;

    DatabaseHelper myDb;
    Spinner layoutSpinner;
    LayoutAdapter mAdapterLayout;
    DataAdapter mAdapterData;

    public static String EXTRA_ID_DATA = "com.ECard.EXTRA_ID_DATA";
    public static String EXTRA_NAME = "com.ECard.EXTRA_NAME";
    public static String EXTRA_COMPANY_NAME = "com.ECard.EXTRA_COMPANY_NAME";
    public static String EXTRA_ADDRESS = "com.ECard.EXTRA_ADDRESS";
    public static String EXTRA_EMAIL = "com.ECard.EXTRA_EMAIL";
    public static String EXTRA_PHONE = "com.ECard.EXTRA_PHONE";
    public static String EXTRA_LAYOUT_NAME = "com.ECard.EXTRA_LAYOUT_NAME";
    public static String EXTRA_BOOL = "com.ECard.EXTRA_BOOL";
    public static String EXTRA_LAYOUT_ID = "com.ECard.EXTRA_LAYOUT_ID";

    public static String EXTRA_ID_LAYOUT = "com.ECard.EXTRA_LAYOUT_ID";
    public static String EXTRA_NAME_LAYOUT = "com.ECard.EXTRA_LAYOUT_NAME";
    public static String EXTRA_ORIENTATION = "com.ECard.EXTRA_ORIENTATION";
    public static String EXTRA_NAME_POS_X = "com.ECard.EXTRA_NAME_POS_X";
    public static String EXTRA_NAME_POS_Y = "com.ECard.EXTRA_NAME_POS_Y";
    public static String EXTRA_NAME_FONT = "com.ECard.EXTRA_NAME_FONT";
    public static String EXTRA_COMPANY_POS_X = "com.ECard.EXTRA_COMPANY_POS_X";
    public static String EXTRA_COMPANY_POS_Y = "com.ECard.EXTRA_COMPANY_POS_Y";
    public static String EXTRA_COMPANY_FONT = "com.ECard.EXTRA_COMPANY_FONT";
    public static String EXTRA_ADDRESS_POS_X = "com.ECard.EXTRA_ADDRESS_POS_X";
    public static String EXTRA_ADDRESS_POS_Y = "com.ECard.EXTRA_ADDRESS_POS_Y";
    public static String EXTRA_ADDRESS_FONT = "com.ECard.EXTRA_ADDRESS_FONT";
    public static String EXTRA_EMAIL_POS_X = "com.ECard.EXTRA_EMAIL_POS_X";
    public static String EXTRA_EMAIL_POS_Y = "com.ECard.EXTRA_EMAIL_POS_Y";
    public static String EXTRA_EMAIL_FONT = "com.ECard.EXTRA_EMAIL_FONT";
    public static String EXTRA_PHONE_POS_X = "com.ECard.EXTRA_PHONE_POS_X";
    public static String EXTRA_PHONE_POS_Y = "com.ECard.EXTRA_PHONE_POS_Y";
    public static String EXTRA_PHONE_FONT = "com.ECard.EXTRA_PHONE_FONT";

    public List<String> loadSpinnerData() {
        // database handler
        myDb = new DatabaseHelper(this);
        layoutSpinner = findViewById(R.id.layoutSpinner);

        // Spinner Drop down elements
        List<String> labels = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        Cursor cursor = myDb.getAllLabels();
        if (cursor.moveToFirst()) {
            do {
                ids.add(cursor.getString(0));
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        // Creating adapter for spinner
        dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return ids;
    }

    public DataAdapter getAllItemsData() {
        myDb = new DatabaseHelper(this);
        final Cursor res = myDb.getAllItems();
        mAdapterData = new DataAdapter(this, res, new DataAdapter.OnDataListener() {
            @Override
            public void onDataClick(int position) {
                res.moveToPosition(position);
                int id = res.getInt(0);
                Cursor res = myDb.getDataData(id);

                if (res.getCount() == 0) {
                    return;
                }

                res.moveToFirst();
                String name = res.getString(0);
                String companyName = res.getString(1);
                String address = res.getString(2);
                String email = res.getString(3);
                String phone = res.getString(4);
                String layoutName = res.getString(5);
                int layoutId = res.getInt(6);

                Intent intent = new Intent();
                intent.putExtra(EXTRA_NAME, name);
                intent.putExtra(EXTRA_COMPANY_NAME, companyName);
                intent.putExtra(EXTRA_ADDRESS, address);
                intent.putExtra(EXTRA_EMAIL, email);
                intent.putExtra(EXTRA_PHONE, phone);
                intent.putExtra(EXTRA_LAYOUT_NAME, layoutName);
                intent.putExtra(EXTRA_LAYOUT_ID, layoutId);
                intent.putExtra(EXTRA_BOOL, true);

                setResult(RESULT_OK, intent);
                finish();
            }
        });
        return mAdapterData;
    }

    public LayoutAdapter getAllItemsLayout() {
        myDb = new DatabaseHelper(this);
        final Cursor res = myDb.getAllData();
        mAdapterLayout = new LayoutAdapter(this, res, new LayoutAdapter.OnLayoutListener() {
            @Override
            public void onDataClick(int position) {
                res.moveToPosition(position);
                int id = res.getInt(0);
                Cursor res = myDb.getLayoutData(id);

                if (res.getCount() == 0) {
                    return;
                }

                res.moveToFirst();
                String name = res.getString(1);
                String orientation = res.getString(2);
                String name_pos_x = res.getString(3);
                String name_pos_y = res.getString(4);
                String name_font = res.getString(5);
                String company_pos_x = res.getString(6);
                String company_pos_y = res.getString(7);
                String company_font = res.getString(8);
                String address_pos_x = res.getString(9);
                String address_pos_y = res.getString(10);
                String address_font = res.getString(11);
                String email_pos_x = res.getString(12);
                String email_pos_y = res.getString(13);
                String email_font = res.getString(14);
                String phone_pos_x = res.getString(15);
                String phone_pos_y = res.getString(16);
                String phone_font = res.getString(17);

                Intent intent = new Intent();
                intent.putExtra(EXTRA_NAME_LAYOUT, name);
                intent.putExtra(EXTRA_ORIENTATION, orientation);
                intent.putExtra(EXTRA_NAME_POS_X, name_pos_x);
                intent.putExtra(EXTRA_NAME_POS_Y, name_pos_y);
                intent.putExtra(EXTRA_NAME_FONT, name_font);
                intent.putExtra(EXTRA_COMPANY_POS_X, company_pos_x);
                intent.putExtra(EXTRA_COMPANY_POS_Y, company_pos_y);
                intent.putExtra(EXTRA_COMPANY_FONT, company_font);
                intent.putExtra(EXTRA_ADDRESS_POS_X, address_pos_x);
                intent.putExtra(EXTRA_ADDRESS_POS_Y, address_pos_y);
                intent.putExtra(EXTRA_ADDRESS_FONT, address_font);
                intent.putExtra(EXTRA_EMAIL_POS_X, email_pos_x);
                intent.putExtra(EXTRA_EMAIL_POS_Y, email_pos_y);
                intent.putExtra(EXTRA_EMAIL_FONT, email_font);
                intent.putExtra(EXTRA_PHONE_POS_X, phone_pos_x);
                intent.putExtra(EXTRA_PHONE_POS_Y, phone_pos_y);
                intent.putExtra(EXTRA_PHONE_FONT, phone_font);

                setResult(RESULT_OK, intent);
                finish();
            }
        });
        return mAdapterLayout;
    }


    public void viewAll() {
        myDb = new DatabaseHelper(this);
        Cursor res = myDb.getAllData();
        if (res.getCount() == 0) {
            // show message
            showMessage("Error", "Nothing found");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("Id :" + res.getString(0) + "\n");
            buffer.append("Name :" + res.getString(1) + "\n");
            buffer.append("Orientation :" + res.getString(2) + "\n");
            buffer.append("Name X :" + res.getString(3) + "\n");
            buffer.append("Name Y :" + res.getString(4) + "\n");
            buffer.append("Name Font size :" + res.getString(5) + "\n");
            buffer.append("Company X :" + res.getString(6) + "\n");
            buffer.append("Company Y :" + res.getString(7) + "\n");
            buffer.append("Company Font size :" + res.getString(8) + "\n");
            buffer.append("Address X :" + res.getString(9) + "\n");
            buffer.append("Address Y :" + res.getString(10) + "\n");
            buffer.append("Address Font size :" + res.getString(11) + "\n");
            buffer.append("Email X :" + res.getString(12) + "\n");
            buffer.append("Email Y :" + res.getString(13) + "\n");
            buffer.append("Email Font size :" + res.getString(14) + "\n");
            buffer.append("Phone X :" + res.getString(15) + "\n");
            buffer.append("Phone Y :" + res.getString(16) + "\n");
            buffer.append("Phone Font size :" + res.getString(17) + "\n\n");
        }

        // Show all data
        showMessage("Data", buffer.toString());
    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

}
