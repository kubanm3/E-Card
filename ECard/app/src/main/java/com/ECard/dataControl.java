package com.ECard;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;


public class  dataControl extends ActionBarActivity {

    Button btnSend;
    EditText nameTextbox;
    EditText companyTextbox;
    EditText addressTextbox;
    EditText emailTextbox;
    EditText phoneNumberTextbox;
    Spinner layoutSpinner;

    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID.
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent newint = getIntent();
        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS); //receive the address of the bluetooth device

        //view of datacontrol
        setContentView(R.layout.activity_data_control);


        nameTextbox = (EditText) findViewById(R.id.nameText);
        companyTextbox = (EditText) findViewById(R.id.companyText);
        addressTextbox = (EditText) findViewById(R.id.addressText);
        emailTextbox = (EditText) findViewById(R.id.emailText);
        phoneNumberTextbox = (EditText) findViewById(R.id.phoneNumberText);
        btnSend = (Button)findViewById(R.id.sendBtn);
        layoutSpinner = (Spinner)findViewById(R.id.layoutSpinner);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.layouts, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        layoutSpinner.setAdapter(adapter);
        
        layoutSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(dataControl.this, "Selected : " + adapter.getItem(i), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        new ConnectBT().execute();

        //commands to be sent to bluetooth
        btnSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendData();      //method to turn on
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
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
        }
        return super.onOptionsItemSelected(item);
    }


    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Błąd");}
        }
        finish(); //return to the first layout

    }

    private void sendData()
    {
        if (btSocket!=null)
        {
            try
            {
                String name = nameTextbox.getText().toString();
                String company = companyTextbox.getText().toString();
                String address = addressTextbox.getText().toString();
                String email = emailTextbox.getText().toString();
                String phone = phoneNumberTextbox.getText().toString();
                String msg = "";
                msg += (name.equals("")) ? " " : name;
                msg += ";";
                msg += "20";
                msg += ";";
                msg += "20";
                msg += ";";
                msg += (company.equals("")) ? " " : company;
                msg += ";";
                msg += "20";
                msg += ";";
                msg += "30";
                msg += ";";
                msg += (address.equals("")) ? " " : address;
                msg += ";";
                msg += "20";
                msg += ";";
                msg += "80";
                msg += ";";
                msg += (email.equals("")) ? " " : email;
                msg += ";";
                msg += "140";
                msg += ";";
                msg += "80";
                msg += ";";
                msg += (phone.equals("")) ? " " : phone;
                msg += ";";
                msg += "140";
                msg += ";";
                msg += "90";
                Log.d("msg", msg);
                btSocket.getOutputStream().write(msg.getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    // fast way to call Toast
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(dataControl.this, "Łączenie...", "Proszę czekać!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                 myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                 BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                 btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                 BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                 btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Połączenie nieudane.");
                finish();
            }
            else
            {
                msg("Połączono.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }
}
