package com.ECard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

public class AddLayout extends BaseActivity {

    DatabaseHelper myDb;
    EditText editName, editNameX, editNameY, editCompanyX, editCompanyY,
            editAddressX, editAddressY, editEmailX, editEmailY, editPhoneX, editPhoneY, editId;
    Button btnAddData;
    RadioGroup radioGroup;
    Switch nameFontSwitch, companyFontSwitch, addressFontSwitch, emailFontSwitch, phoneFontSwitch;

    String orientation = "1", nameFont = "0", companyFont = "0", addressFont = "0", emailFont = "0", phoneFont = "0";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_layout);
        myDb = new DatabaseHelper(this);

        editName = findViewById(R.id.layoutName);
        radioGroup = findViewById(R.id.layoutOrientRadio);
        editNameX = findViewById(R.id.layoutNameX);
        editNameX.setFilters(new InputFilter[]{new MaxValueFilter(0, 282)});
        editNameY = findViewById(R.id.layoutNameY);
        editNameY.setFilters(new InputFilter[]{new MaxValueFilter(0, 120)});
        editCompanyX = findViewById(R.id.layoutCompanyX);
        editCompanyX.setFilters(new InputFilter[]{new MaxValueFilter(0, 282)});
        editCompanyY = findViewById(R.id.layoutCompanyY);
        editCompanyY.setFilters(new InputFilter[]{new MaxValueFilter(0, 120)});
        editAddressX = findViewById(R.id.layoutAddressX);
        editAddressX.setFilters(new InputFilter[]{new MaxValueFilter(0, 282)});
        editAddressY = findViewById(R.id.layoutAddressY);
        editAddressY.setFilters(new InputFilter[]{new MaxValueFilter(0, 120)});
        editEmailX = findViewById(R.id.layoutEmailX);
        editEmailX.setFilters(new InputFilter[]{new MaxValueFilter(0, 282)});
        editEmailY = findViewById(R.id.layoutEmailY);
        editEmailY.setFilters(new InputFilter[]{new MaxValueFilter(0, 120)});
        editPhoneX = findViewById(R.id.layoutPhoneX);
        editPhoneX.setFilters(new InputFilter[]{new MaxValueFilter(0, 282)});
        editPhoneY = findViewById(R.id.layoutPhoneY);
        editPhoneY.setFilters(new InputFilter[]{new MaxValueFilter(0, 120)});
        editId = findViewById(R.id.layoutID);
        btnAddData = findViewById(R.id.saveLayoutBT);
        nameFontSwitch = findViewById(R.id.nameFontSwitch);
        companyFontSwitch = findViewById(R.id.companyFontSwitch);
        addressFontSwitch = findViewById(R.id.addressFontSwitch);
        emailFontSwitch = findViewById(R.id.emailFontSwitch);
        phoneFontSwitch = findViewById(R.id.phoneFontSwitch);

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
        nameFontSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {nameFont = "1";}
                else {nameFont = "0";}
            }
        });

        companyFontSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {companyFont = "1";}
                else {companyFont = "0";}
            }
        });

        addressFontSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {addressFont = "1";}
                else {addressFont = "0";}
            }
        });

        emailFontSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {emailFont = "1";}
                else {emailFont = "0";}
            }
        });

        phoneFontSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {phoneFont = "1";}
                else {phoneFont = "0";}
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
        WindowManager windowmanager =
                (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public void DeleteData() {
        String id = editId.getText().toString();
        if (id != null && !id.trim().isEmpty()) {
            try {
                myDb.deleteDataLayout(id);
                Toast.makeText(AddLayout.this, "Layout deleted", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(AddLayout.this, "Layout not deleted, there is saved data that uses this layout!", Toast.LENGTH_LONG).show();
            }
        } else
            Toast.makeText(AddLayout.this, "If you wish to delete layout, enter its ID.", Toast.LENGTH_LONG).show();
    }


    public void UpdateData() {
        String id = editId.getText().toString();
        if (id != null && !id.trim().isEmpty()) {
            boolean isUpdate =
                    myDb.updateData(id,
                            editName.getText().toString(),
                            orientation,
                            editNameX.getText().toString(), editNameY.getText().toString(), nameFont,
                            editCompanyX.getText().toString(), editCompanyY.getText().toString(), companyFont,
                            editAddressX.getText().toString(), editAddressY.getText().toString(), addressFont,
                            editEmailX.getText().toString(), editEmailY.getText().toString(), emailFont,
                            editPhoneX.getText().toString(), editPhoneY.getText().toString(), phoneFont);
            if (isUpdate)
                Toast.makeText(AddLayout.this, "Layout updated", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(AddLayout.this, "Layout not updated", Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(AddLayout.this, "If you wish to update layout, enter its ID.", Toast.LENGTH_LONG).show();
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
                                editNameX.getText().toString(), editNameY.getText().toString(), nameFont,
                                editCompanyX.getText().toString(), editCompanyY.getText().toString(), companyFont,
                                editAddressX.getText().toString(), editAddressY.getText().toString(), addressFont,
                                editEmailX.getText().toString(), editEmailY.getText().toString(), emailFont,
                                editPhoneX.getText().toString(), editPhoneY.getText().toString(), phoneFont);
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
