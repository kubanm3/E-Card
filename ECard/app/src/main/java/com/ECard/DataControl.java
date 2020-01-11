package com.ECard;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class DataControl extends BaseActivity {
    Button btnSave;
    EditText nameTextbox, companyTextbox, addressTextbox, emailTextbox, phoneNumberTextbox;
    Spinner layoutSpinner;
    Integer layoutId = 0;
    int spinnerPosition = 0;

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //view of datacontrol
        setContentView(R.layout.activity_data_control);

        nameTextbox = findViewById(R.id.nameText);
        companyTextbox = findViewById(R.id.companyText);
        addressTextbox = findViewById(R.id.addressText);
        emailTextbox = findViewById(R.id.emailText);
        phoneNumberTextbox = findViewById(R.id.phoneNumberText);
        btnSave = findViewById(R.id.saveBtn);
        layoutSpinner = findViewById(R.id.layoutSpinner);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final List<String> ids = loadSpinnerData();

        layoutSpinner.setAdapter(dataAdapter);
        layoutSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                layoutId = Integer.valueOf(ids.get(i));
                Toast.makeText(DataControl.this, "Selected : " + adapterView.getItemAtPosition(i), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        final List<String> ids = loadSpinnerData();
        layoutSpinner.setAdapter(dataAdapter);
        layoutSpinner.setSelection(spinnerPosition);

        layoutSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                layoutId = Integer.valueOf(ids.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            String name = data.getStringExtra(EXTRA_NAME);
            nameTextbox.setText(name);
            String companyName = data.getStringExtra(EXTRA_COMPANY_NAME);
            companyTextbox.setText(companyName);
            String address_data = data.getStringExtra(EXTRA_ADDRESS);
            addressTextbox.setText(address_data);
            String email = data.getStringExtra(EXTRA_EMAIL);
            emailTextbox.setText(email);
            String phone = data.getStringExtra(EXTRA_PHONE);
            phoneNumberTextbox.setText(phone);

            String layoutName = data.getStringExtra(EXTRA_LAYOUT_NAME);
            if (layoutName != null) {
                spinnerPosition = dataAdapter.getPosition(layoutName);
                layoutSpinner.setSelection(spinnerPosition);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_card_control, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                Toast.makeText(this, "Autor: Jakub Legutko", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.layoutList:
                Intent intent = new Intent(this, AddLayout.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                } else {
                    startActivity(intent);
                }
                return true;
            case R.id.showFromList:
                viewAll();
                return true;
            case R.id.showDataList:
                Intent intentData = new Intent(this, DataList.class);
                startActivityForResult(intentData, 2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveData() {
        if (layoutId != 0) {
            boolean isInserted =
                    myDb.insertDataData(
                            nameTextbox.getText().toString(),
                            companyTextbox.getText().toString(),
                            addressTextbox.getText().toString(),
                            emailTextbox.getText().toString(),
                            phoneNumberTextbox.getText().toString(),
                            layoutId);
            if (isInserted) {
                Toast.makeText(DataControl.this, "Data inserted", Toast.LENGTH_LONG).show();
                loadSpinnerData();
            } else
                Toast.makeText(DataControl.this, "Data not inserted", Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(DataControl.this, "Data not inserted", Toast.LENGTH_LONG).show();
    }
}
