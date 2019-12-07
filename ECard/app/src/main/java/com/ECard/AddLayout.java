package com.ECard;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddLayout extends BaseActivity {

    DatabaseHelper myDb;
    EditText editName, editOrientation, editNameX, editNameY, editCompanyX, editCompanyY,
            editAddressX, editAddressY, editEmailX, editEmailY, editPhoneX, editPhoneY, editId;
    Button btnAddData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_layout);
        myDb = new DatabaseHelper(this);

        editName = findViewById(R.id.layoutName);
        editOrientation = findViewById(R.id.layoutOrient);
        editNameX = findViewById(R.id.layoutNameX);
        editNameY = findViewById(R.id.layoutNameY);
        editCompanyX = findViewById(R.id.layoutCompanyX);
        editCompanyY = findViewById(R.id.layoutCompanyY);
        editAddressX = findViewById(R.id.layoutAddressX);
        editAddressY = findViewById(R.id.layoutAddressY);
        editEmailX = findViewById(R.id.layoutEmailX);
        editEmailY = findViewById(R.id.layoutEmailY);
        editPhoneX = findViewById(R.id.layoutPhoneX);
        editPhoneY = findViewById(R.id.layoutPhoneY);
        editId = findViewById(R.id.layoutID);
        btnAddData = findViewById(R.id.saveLayoutBT);

        AddData();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                Toast.makeText(this, "Autor: Jakub Legutko", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.editFromList:
                UpdateData();
                return true;
            case R.id.deleteFromList:
                AlertDialog diaBox = AskOption();
                diaBox.show();
                return true;
            case R.id.showFromList:
                viewAll();
                return true;
            case R.id.showDataList:
                Intent intentData = new Intent(this, dataList.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intentData, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                } else {
                    startActivity(intentData);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void DeleteData() {
        try {
            myDb.deleteDataLayout(editId.getText().toString());
            Toast.makeText(AddLayout.this, "Layout deleted", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(AddLayout.this, "Layout not deleted, there is saved data using this layout!", Toast.LENGTH_LONG).show();
        }
    }


    public void UpdateData() {
        boolean isUpdate =
                myDb.updateData(editId.getText().toString(), editName.getText().toString(),
                        editOrientation.getText().toString(),
                        editNameX.getText().toString(), editNameY.getText().toString(),
                        editCompanyX.getText().toString(), editCompanyY.getText().toString(),
                        editAddressX.getText().toString(), editAddressY.getText().toString(),
                        editEmailX.getText().toString(), editEmailY.getText().toString(),
                        editPhoneX.getText().toString(), editPhoneY.getText().toString());
        if (isUpdate)
            Toast.makeText(AddLayout.this, "Layout updated", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(AddLayout.this, "Layout not updated", Toast.LENGTH_LONG).show();
    }

    private AlertDialog AskOption() {

        return new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Delete?")
                .setMessage("Are you sure you want to delete this layout?")
                .setPositiveButton("delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        DeleteData();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
    }

    public void AddData() {
        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = myDb.insertDataLayout(editName.getText().toString(),
                                editOrientation.getText().toString(),
                                editNameX.getText().toString(), editNameY.getText().toString(),
                                editCompanyX.getText().toString(), editCompanyY.getText().toString(),
                                editAddressX.getText().toString(), editAddressY.getText().toString(),
                                editEmailX.getText().toString(), editEmailY.getText().toString(),
                                editPhoneX.getText().toString(), editPhoneY.getText().toString());
                        if (isInserted) {
                            Toast.makeText(AddLayout.this, "Data inserted", Toast.LENGTH_LONG).show();
                            loadSpinnerData();
                            dataAdapter.notifyDataSetChanged();
                        } else
                            Toast.makeText(AddLayout.this, "Data not inserted", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
}
