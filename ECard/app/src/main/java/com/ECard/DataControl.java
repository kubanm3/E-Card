package com.ECard;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class DataControl extends BaseActivity {
    Button btnSend, btnSave, btnUpdate;
    EditText nameTextbox, companyTextbox, addressTextbox, emailTextbox, phoneNumberTextbox;
    TextView idTextbox;
    Spinner layoutSpinner;
    Integer layoutId = 0;
    int spinnerPosition = 0;

    String address;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID.
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent newint = getIntent();
        address =
                newint.getStringExtra(DeviceList.EXTRA_ADDRESS); //receive the address of the bluetooth device

        setContentView(R.layout.activity_data_control);
        initFields();

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

        if (!isBtConnected) {
            new ConnectBT().execute();
        }

        SendDataListener();
        SaveDataListener();
        UpdateDataListener();
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
            String id = data.getStringExtra(EXTRA_ID_DATA);
            idTextbox.setText(id);

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
        getMenuInflater().inflate(R.menu.menu_data_control, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                Toast.makeText(this, "Autor: Jakub Legutko", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.disconnect:
                Disconnect();
                return true;
            case R.id.layoutList:
                Intent intentAddLayout = new Intent(this, LayoutControl.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intentAddLayout, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                } else {
                    startActivity(intentAddLayout);
                }
                return true;
            case R.id.showFromList:
                Intent intentLayoutList = new Intent(this, LayoutList.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intentLayoutList, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                } else {
                    startActivity(intentLayoutList);
                }
                return true;
            case R.id.showDataList:
                Intent intentData = new Intent(this, DataList.class);
                startActivityForResult(intentData, 2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initFields() {
        nameTextbox = findViewById(R.id.nameText);
        nameTextbox.setFilters(new InputFilter[]{new FilterCharacter()});
        companyTextbox = findViewById(R.id.companyText);
        companyTextbox.setFilters(new InputFilter[]{new FilterCharacter()});
        addressTextbox = findViewById(R.id.addressText);
        addressTextbox.setFilters(new InputFilter[]{new FilterCharacter()});
        emailTextbox = findViewById(R.id.emailText);
        emailTextbox.setFilters(new InputFilter[]{new FilterCharacter()});
        phoneNumberTextbox = findViewById(R.id.phoneNumberText);
        idTextbox = findViewById(R.id.dataID);
        btnSend = findViewById(R.id.sendBtn);
        btnSave = findViewById(R.id.saveBtn);
        btnUpdate = findViewById(R.id.editBtn);
        layoutSpinner = findViewById(R.id.layoutSpinner);
    }

    public void UpdateDataListener() {
        btnUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UpdateData();
                    }
                }
        );
    }

    public void UpdateData() {
        String id = idTextbox.getText().toString();
        if (id != null && !id.trim().isEmpty()) {
            boolean isUpdate =
                    myDb.updateData(id, String.valueOf(layoutId),
                            nameTextbox.getText().toString(),
                            companyTextbox.getText().toString(),
                            addressTextbox.getText().toString(),
                            emailTextbox.getText().toString(),
                            phoneNumberTextbox.getText().toString());
            if (isUpdate)
                Toast.makeText(DataControl.this, "Data updated", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(DataControl.this, "Data not updated", Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(DataControl.this, "If you wish to update data, enter its ID.", Toast.LENGTH_LONG).show();
    }

    private void Disconnect() {
        if (btSocket != null) //If the btSocket is busy
        {
            try {
                btSocket.close(); //close connection
            } catch (IOException e) {
                msg("Błąd");
            }
        }
        finish(); //return to the first layout
    }

    private void SendDataListener() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameTextbox.getText().toString();
                String company = companyTextbox.getText().toString();
                String address = addressTextbox.getText().toString();
                String email = emailTextbox.getText().toString();
                String phone = phoneNumberTextbox.getText().toString();
                sendData(getDataToSend(name, company, address, email, phone, layoutId));
                btnSend.setEnabled(false);
                btnSend.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnSend.setEnabled(true);
                        Toast.makeText(DataControl.this, "You can send again.", Toast.LENGTH_LONG).show();
                    }
                }, 2500);
            }
        });
    }

    private void sendData(StringBuffer buffer) {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write(String.valueOf(buffer).getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private StringBuffer getDataToSend(String name, String company, String address, String email, String phone, int layout_id) {
        Cursor res = myDb.getLayoutData(layout_id);
        if (res.getCount() == 0) {
            // show message
            msg("Error: Nothing found");
            return null;
        }

        StringBuffer buffer = new StringBuffer();
        res.moveToFirst();
        buffer.append(res.getInt(2)).append(";"); //orientation
        buffer.append(res.getInt(3)).append(";"); //pos x name
        buffer.append(res.getInt(4)).append(";"); // pos y name
        buffer.append(res.getInt(5)).append(";"); // font name
        buffer.append((name.equals("")) ? " ;" : (name + ";")); //name
        buffer.append(res.getInt(6)).append(";"); //pos x company
        buffer.append(res.getInt(7)).append(";"); //pos y company
        buffer.append(res.getInt(8)).append(";"); // font company
        buffer.append((company.equals("")) ? " ;" : (company + ";")); //company
        buffer.append(res.getInt(9)).append(";"); //pos x address
        buffer.append(res.getInt(10)).append(";"); //pos y address
        buffer.append(res.getInt(11)).append(";"); // font address
        buffer.append((address.equals("")) ? " ;" : (address + ";")); //address
        buffer.append(res.getInt(12)).append(";"); //pos x email
        buffer.append(res.getInt(13)).append(";"); //pos y email
        buffer.append(res.getInt(14)).append(";"); // font email
        buffer.append((email.equals("")) ? " ;" : (email + ";")); //email
        buffer.append(res.getInt(15)).append(";"); //pos x phone
        buffer.append(res.getInt(16)).append(";"); //pos y phone
        buffer.append(res.getInt(17)).append(";"); // font phone
        buffer.append((phone.equals("")) ? " ;" : (phone + ";")); //phone

        Log.d("przeslanedane", String.valueOf(buffer));
        return buffer;
    }

    private void SaveDataListener() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
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

    // fast way to call Toast
    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute() {
            progress =
                    ProgressDialog.show(DataControl.this, "Łączenie...", "Proszę czekać!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket == null || !isBtConnected) {
                    myBluetooth =
                            BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo =
                            myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket =
                            dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            } catch (IOException e) {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);
            if (!ConnectSuccess) {
                msg("Połączenie nieudane.");
                finish();
            } else {
                msg("Połączono.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }
}
