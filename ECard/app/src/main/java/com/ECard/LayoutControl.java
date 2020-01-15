package com.ECard;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import android.widget.TextView;
import android.widget.Toast;

public class LayoutControl extends BaseActivity {

    DatabaseHelper myDb;
    EditText editName, editNameX, editNameY, editCompanyX, editCompanyY,
            editAddressX, editAddressY, editEmailX, editEmailY, editPhoneX, editPhoneY;
    TextView editId;
    Button btnAddData, btnUpdateLayout;
    RadioGroup radioOrientation;
    Switch nameFontSwitch, companyFontSwitch, addressFontSwitch, emailFontSwitch, phoneFontSwitch;

    String orientation = "1", nameFont = "0", companyFont = "0", addressFont = "0", emailFont = "0",
            phoneFont = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_control);
        myDb = new DatabaseHelper(this);

        initFields();

        Intent data = getIntent();
        setEditSwipedValues(data);

        radioGroupListener();
        nameFontListener();
        companyFontListener();
        addressFontListener();
        emailFontListener();
        phoneFontListener();


        UpdateLayoutListener();
        AddDataListener();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_layout_control, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                Toast.makeText(this, "Autor: Jakub Legutko", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.showFromList:
                Intent intentLayoutList = new Intent(this, LayoutList.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intentLayoutList, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                } else {
                    startActivity(intentLayoutList);
                }
                return true;
            case R.id.buttonShowHelpDialog:
                showHelpDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initFields() {
        editName = findViewById(R.id.layoutName);
        radioOrientation = findViewById(R.id.layoutOrientRadio);
        editNameX = findViewById(R.id.layoutNameX);
        editNameX.setFilters(new InputFilter[]{new FilterMaxValue(0, 282)});
        editNameY = findViewById(R.id.layoutNameY);
        editNameY.setFilters(new InputFilter[]{new FilterMaxValue(0, 120)});
        editCompanyX = findViewById(R.id.layoutCompanyX);
        editCompanyX.setFilters(new InputFilter[]{new FilterMaxValue(0, 282)});
        editCompanyY = findViewById(R.id.layoutCompanyY);
        editCompanyY.setFilters(new InputFilter[]{new FilterMaxValue(0, 120)});
        editAddressX = findViewById(R.id.layoutAddressX);
        editAddressX.setFilters(new InputFilter[]{new FilterMaxValue(0, 282)});
        editAddressY = findViewById(R.id.layoutAddressY);
        editAddressY.setFilters(new InputFilter[]{new FilterMaxValue(0, 120)});
        editEmailX = findViewById(R.id.layoutEmailX);
        editEmailX.setFilters(new InputFilter[]{new FilterMaxValue(0, 282)});
        editEmailY = findViewById(R.id.layoutEmailY);
        editEmailY.setFilters(new InputFilter[]{new FilterMaxValue(0, 120)});
        editPhoneX = findViewById(R.id.layoutPhoneX);
        editPhoneX.setFilters(new InputFilter[]{new FilterMaxValue(0, 282)});
        editPhoneY = findViewById(R.id.layoutPhoneY);
        editPhoneY.setFilters(new InputFilter[]{new FilterMaxValue(0, 120)});
        editId = findViewById(R.id.layoutID);
        btnAddData = findViewById(R.id.saveLayoutBT);
        btnUpdateLayout = findViewById(R.id.updateLayoutBT);
        nameFontSwitch = findViewById(R.id.nameFontSwitch);
        companyFontSwitch = findViewById(R.id.companyFontSwitch);
        addressFontSwitch = findViewById(R.id.addressFontSwitch);
        emailFontSwitch = findViewById(R.id.emailFontSwitch);
        phoneFontSwitch = findViewById(R.id.phoneFontSwitch);
    }

    private void setEditSwipedValues(Intent data) {
        String id = data.getStringExtra(EXTRA_LAYOUT_ID);
        editId.setText(id);
        String name = data.getStringExtra(EXTRA_NAME_LAYOUT);
        editName.setText(name);
        orientation = data.getStringExtra(EXTRA_ORIENTATION);
        if (orientation != null) {
            if (orientation.equals("1")) {
                radioOrientation.check(R.id.horizontal);
            } else {
                radioOrientation.check(R.id.vertical);
            }
        }

        String name_pos_x = data.getStringExtra(EXTRA_NAME_POS_X);
        editNameX.setText(name_pos_x);
        String name_pos_y = data.getStringExtra(EXTRA_NAME_POS_Y);
        editNameY.setText(name_pos_y);
        String name_font = data.getStringExtra(EXTRA_NAME_FONT);
        if (name_font != null) {
            if (name_font.equals("0")) {
                nameFontSwitch.setChecked(false);
            } else {
                nameFontSwitch.setChecked(true);
            }
        }

        String company_pos_x = data.getStringExtra(EXTRA_COMPANY_POS_X);
        editCompanyX.setText(company_pos_x);
        String company_pos_y = data.getStringExtra(EXTRA_COMPANY_POS_Y);
        editCompanyY.setText(company_pos_y);
        String company_font = data.getStringExtra(EXTRA_COMPANY_FONT);
        if (company_font != null) {
            if (company_font.equals("0")) {
                companyFontSwitch.setChecked(false);
            } else {
                companyFontSwitch.setChecked(true);
            }
        }

        String address_pos_x = data.getStringExtra(EXTRA_ADDRESS_POS_X);
        editAddressX.setText(address_pos_x);
        String address_pos_y = data.getStringExtra(EXTRA_ADDRESS_POS_Y);
        editAddressY.setText(address_pos_y);
        String address_font = data.getStringExtra(EXTRA_ADDRESS_FONT);
        if (address_font != null) {
            if (address_font.equals("0")) {
                addressFontSwitch.setChecked(false);
            } else {
                addressFontSwitch.setChecked(true);
            }
        }

        String email_pos_x = data.getStringExtra(EXTRA_EMAIL_POS_X);
        editEmailX.setText(email_pos_x);
        String email_pos_y = data.getStringExtra(EXTRA_EMAIL_POS_Y);
        editEmailY.setText(email_pos_y);
        String email_font = data.getStringExtra(EXTRA_EMAIL_FONT);
        if (email_font != null) {
            if (email_font.equals("0")) {
                emailFontSwitch.setChecked(false);
            } else {
                emailFontSwitch.setChecked(true);
            }
        }

        String phone_pos_x = data.getStringExtra(EXTRA_PHONE_POS_X);
        editPhoneX.setText(phone_pos_x);
        String phone_pos_y = data.getStringExtra(EXTRA_PHONE_POS_Y);
        editPhoneY.setText(phone_pos_y);
        String phone_font = data.getStringExtra(EXTRA_PHONE_FONT);
        if (phone_font != null) {
            if (phone_font.equals("0")) {
                phoneFontSwitch.setChecked(false);
            } else {
                phoneFontSwitch.setChecked(true);
            }
        }
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

    public void UpdateLayout() {
        String id = editId.getText().toString();
        if (id != null && !id.trim().isEmpty()) {
            boolean isUpdate =
                    myDb.updateLayout(id,
                            editName.getText().toString(),
                            orientation,
                            editNameX.getText().toString(), editNameY.getText().toString(), nameFont,
                            editCompanyX.getText().toString(), editCompanyY.getText().toString(), companyFont,
                            editAddressX.getText().toString(), editAddressY.getText().toString(), addressFont,
                            editEmailX.getText().toString(), editEmailY.getText().toString(), emailFont,
                            editPhoneX.getText().toString(), editPhoneY.getText().toString(), phoneFont);
            if (isUpdate)
                Toast.makeText(LayoutControl.this, "Layout updated", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(LayoutControl.this, "Layout not updated", Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(LayoutControl.this, "If you wish to update layout, enter its ID.", Toast.LENGTH_LONG).show();
    }

    public void AddDataListener() {
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
                            Toast.makeText(LayoutControl.this, "Data inserted", Toast.LENGTH_LONG).show();
                            loadSpinnerData();
                            dataAdapter.notifyDataSetChanged();
                        } else
                            Toast.makeText(LayoutControl.this, "Data not inserted", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void UpdateLayoutListener() {
        btnUpdateLayout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UpdateLayout();
                    }
                }
        );
    }

    public void radioGroupListener() {
        radioOrientation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
    }

    public void nameFontListener() {
        nameFontSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    nameFont = "1";
                } else {
                    nameFont = "0";
                }
            }
        });
    }

    public void companyFontListener() {
        companyFontSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    companyFont = "1";
                } else {
                    companyFont = "0";
                }
            }
        });
    }

    public void addressFontListener() {
        addressFontSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    addressFont = "1";
                } else {
                    addressFont = "0";
                }
            }
        });
    }

    public void emailFontListener() {
        emailFontSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    emailFont = "1";
                } else {
                    emailFont = "0";
                }
            }
        });
    }

    public void phoneFontListener() {
        phoneFontSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    phoneFont = "1";
                } else {
                    phoneFont = "0";
                }
            }
        });
    }
}
