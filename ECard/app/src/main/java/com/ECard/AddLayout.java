package com.ECard;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class AddLayout extends BaseActivity {

    DatabaseHelper myDb;
    EditText editName, editNameX, editNameY, editCompanyX, editCompanyY,
            editAddressX, editAddressY, editEmailX, editEmailY, editPhoneX, editPhoneY, editId;
    Button btnAddData;
    RadioGroup radioGroup;

    String orientation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_layout);
        myDb = new DatabaseHelper(this);

        editName = findViewById(R.id.layoutName);
        radioGroup = findViewById(R.id.layoutOrientRadio);
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

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.vertical) {
                    orientation = "0";
                    Toast.makeText(getApplicationContext(), "Vertical orientation",
                            Toast.LENGTH_SHORT).show();
                } else if (checkedId == R.id.horizontal) {
                    orientation = "1";
                    Toast.makeText(getApplicationContext(), "Horizontal orientation",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

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
            case R.id.buttonShowHelpDialog:
                showHelpDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showHelpDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.help_dialog);
        dialog.setTitle("Help");
        dialog.getWindow().setLayout(((getWidth(getApplicationContext()) / 100) * 90), LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setGravity(Gravity.CENTER);

        Button dialogButton = dialog.findViewById(R.id.dialogButtonOK);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static int getWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
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
                myDb.updateData(editId.getText().toString(),
                        editName.getText().toString(),
                        orientation,
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
                .setTitle("Delete?")
                .setMessage("Are you sure you want to delete this layout?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        DeleteData();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
                                orientation,
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
