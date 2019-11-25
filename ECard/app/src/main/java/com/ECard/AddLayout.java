package com.ECard;


import android.app.AlertDialog;
import android.database.Cursor;
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
            editAddressX,
            editAddressY, editEmailX, editEmailY, editPhoneX, editPhoneY, editId;
    Button btnAddData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_layout);
        myDb = new DatabaseHelper(this);

        editName = (EditText) findViewById(R.id.layoutName);
        editOrientation = (EditText) findViewById(R.id.layoutOrient);
        editNameX = (EditText) findViewById(R.id.layoutNameX);
        editNameY = (EditText) findViewById(R.id.layoutNameY);
        editCompanyX = (EditText) findViewById(R.id.layoutCompanyX);
        editCompanyY = (EditText) findViewById(R.id.layoutCompanyY);
        editAddressX = (EditText) findViewById(R.id.layoutAddressX);
        editAddressY = (EditText) findViewById(R.id.layoutAddressY);
        editEmailX = (EditText) findViewById(R.id.layoutEmailX);
        editEmailY = (EditText) findViewById(R.id.layoutEmailY);
        editPhoneX = (EditText) findViewById(R.id.layoutPhoneX);
        editPhoneY = (EditText) findViewById(R.id.layoutPhoneY);
        editId = (EditText) findViewById(R.id.layoutID);
        btnAddData = (Button) findViewById(R.id.saveLayoutBT);


        AddData();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                DeleteData();
                return true;
            case R.id.showFromList:
                viewAll();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void DeleteData() {
        Integer deletedRows = myDb.deleteData(editId.getText().toString());
        if (deletedRows > 0)
            Toast.makeText(AddLayout.this, "Data deleted", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(AddLayout.this, "Data not deleted", Toast.LENGTH_LONG).show();
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
            Toast.makeText(AddLayout.this, "Data updated", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(AddLayout.this, "Data not updated", Toast.LENGTH_LONG).show();
    }
    public void AddData() {
        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = myDb.insertData(editName.getText().toString(),
                                editOrientation.getText().toString(),
                                editNameX.getText().toString(), editNameY.getText().toString(),
                                editCompanyX.getText().toString(), editCompanyY.getText().toString(),
                                editAddressX.getText().toString(), editAddressY.getText().toString(),
                                editEmailX.getText().toString(), editEmailY.getText().toString(),
                                editPhoneX.getText().toString(), editPhoneY.getText().toString());
                        if (isInserted) {
                            Toast.makeText(AddLayout.this, "Data inserted", Toast.LENGTH_LONG).show();
                            loadSpinnerData();
                        } else
                            Toast.makeText(AddLayout.this, "Data not inserted", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void viewAll() {
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
            buffer.append("Surname :" + res.getString(2) + "\n");
            buffer.append("Marks :" + res.getString(3) + "\n\n");
        }

        // Show all data
        showMessage("Data", buffer.toString());
    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

}
