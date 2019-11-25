package com.ECard;

import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;


public class BaseActivity extends ActionBarActivity {

    DatabaseHelper myDb;
    Spinner layoutSpinner;


    public void loadSpinnerData() {
        // database handler
        myDb = new DatabaseHelper(this);
        layoutSpinner = (Spinner) findViewById(R.id.layoutSpinner);

        // Spinner Drop down elements
        List<String> lables = myDb.getAllLabels();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        layoutSpinner.setAdapter(dataAdapter);
    }
}
