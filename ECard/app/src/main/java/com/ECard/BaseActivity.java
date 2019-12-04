package com.ECard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends ActionBarActivity {
    ArrayAdapter<String> dataAdapter;

    DatabaseHelper myDb;
    Spinner layoutSpinner;
    RecyclerView dataList;
    DataAdapter mAdapter;

    public List<String> loadSpinnerData() {
        // database handler
        myDb = new DatabaseHelper(this);
        layoutSpinner = (Spinner) findViewById(R.id.layoutSpinner);

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
        Cursor res = myDb.getAllItems();
        mAdapter = new DataAdapter(this, res);
        return mAdapter;
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
            buffer.append("Company X :" + res.getString(5) + "\n");
            buffer.append("Company Y :" + res.getString(6) + "\n");
            buffer.append("Address X :" + res.getString(7) + "\n");
            buffer.append("Address Y :" + res.getString(8) + "\n");
            buffer.append("Email X :" + res.getString(9) + "\n");
            buffer.append("Email Y :" + res.getString(10) + "\n");
            buffer.append("Phone X :" + res.getString(11) + "\n");
            buffer.append("Phone Y :" + res.getString(12) + "\n\n");
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
