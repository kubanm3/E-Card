package com.ECard;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
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

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class dataControl extends BaseActivity {
    Button btnSend, btnSave;
    EditText nameTextbox, companyTextbox, addressTextbox, emailTextbox, phoneNumberTextbox;
    Spinner layoutSpinner;
    Integer layoutId = 0;

    String address = null;
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

        //view of datacontrol
        setContentView(R.layout.activity_data_control);

        nameTextbox = (EditText) findViewById(R.id.nameText);
        companyTextbox = (EditText) findViewById(R.id.companyText);
        addressTextbox = (EditText) findViewById(R.id.addressText);
        emailTextbox = (EditText) findViewById(R.id.emailText);
        phoneNumberTextbox = (EditText) findViewById(R.id.phoneNumberText);
        btnSend = (Button) findViewById(R.id.sendBtn);
        btnSave = (Button) findViewById(R.id.saveBtn);
        layoutSpinner = (Spinner) findViewById(R.id.layoutSpinner);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final List<String> ids = loadSpinnerData();

        layoutSpinner.setAdapter(dataAdapter);
        layoutSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(dataControl.this, "Selected : " + adapterView.getItemAtPosition(i), Toast.LENGTH_LONG).show();
                layoutId = Integer.valueOf(ids.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        new ConnectBT().execute();

        //commands to be sent to bluetooth
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();      //method to turn on
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted =
                        myDb.insertDataData(nameTextbox.getText().toString(), companyTextbox.getText().toString(),
                                addressTextbox.getText().toString(), emailTextbox.getText().toString(), phoneNumberTextbox.getText().toString());
                if (isInserted) {
                    Toast.makeText(dataControl.this, "Data inserted", Toast.LENGTH_LONG).show();
                    loadSpinnerData();
                } else
                    Toast.makeText(dataControl.this, "Data not inserted", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        final List<String> ids = loadSpinnerData();
        layoutSpinner.setAdapter(dataAdapter);

        layoutSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                layoutId = i;
                Toast.makeText(dataControl.this, "Selected : " + adapterView.getItemAtPosition(i), Toast.LENGTH_LONG).show();
                layoutId = Integer.valueOf(ids.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
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
            case R.id.disconnect:
                Disconnect();
                return true;
            case R.id.layoutList:
                Intent intent = new Intent(this, AddLayout.class);
                startActivity(intent);
                return true;
            case R.id.showFromList:
                viewAll();
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    private void sendData() {
        if (btSocket != null) {
            try {
                Cursor res = myDb.getLayoutData(layoutId);
                if (res.getCount() == 0) {
                    // show message
                    msg("Error: Nothing found");
                    return;
                }

                String name = nameTextbox.getText().toString();
                String company = companyTextbox.getText().toString();
                String address = addressTextbox.getText().toString();
                String email = emailTextbox.getText().toString();
                String phone = phoneNumberTextbox.getText().toString();

                StringBuffer buffer = new StringBuffer();
                res.moveToFirst();
                buffer.append(res.getInt(2)).append(";"); //orientation
                buffer.append(res.getInt(3)).append(";"); //pos x name
                buffer.append(res.getInt(4)).append(";"); // pos y name
                buffer.append((name.equals("")) ? " ;" : (name + ";")); //name
                buffer.append(res.getInt(5)).append(";"); //pos x company
                buffer.append(res.getInt(6)).append(";"); //pos y company
                buffer.append((company.equals("")) ? " ;" : (company + ";")); //company
                buffer.append(res.getInt(7)).append(";"); //pos x address
                buffer.append(res.getInt(8)).append(";"); //pos y address
                buffer.append((address.equals("")) ? " ;" : (address + ";")); //address
                buffer.append(res.getInt(9)).append(";"); //pos x email
                buffer.append(res.getInt(10)).append(";"); //pos y email
                buffer.append((email.equals("")) ? " ;" : (email + ";")); //email
                buffer.append(res.getInt(11)).append(";"); //pos x phone
                buffer.append(res.getInt(12)).append(";"); //pos y phone
                buffer.append((phone.equals("")) ? " ;" : (phone + ";")); //phone

                btSocket.getOutputStream().write(String.valueOf(buffer).getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
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
                    ProgressDialog.show(dataControl.this, "Łączenie...", "Proszę czekać!");  //show a progress dialog
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
