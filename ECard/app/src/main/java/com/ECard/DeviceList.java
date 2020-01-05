package com.ECard;

import android.app.ActivityOptions;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class DeviceList extends BaseActivity {
    Button btnPaired;
    ListView devicelist;

    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_ADDRESS = "device_address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        btnPaired = findViewById(R.id.button);
        devicelist = findViewById(R.id.listView);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //sprawdzenie bt
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        if (myBluetooth == null) {
            //pokaz wiadomosci o niedostepnosci urzadzen bt
            Toast.makeText(getApplicationContext(), "Bluetooth is not available on this device.", Toast.LENGTH_LONG).show();

            btnPaired.setVisibility(View.GONE);
            //finish apk
//            finish();
        } else if (!myBluetooth.isEnabled()) {
            //popros o wlaczenie bt
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon, 1);
        }

        btnPaired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pairedDevicesList();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_device_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                String author = getString(R.string.author);
                Toast.makeText(this, author, Toast.LENGTH_SHORT).show();
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
        }
        return super.onOptionsItemSelected(item);
    }

    private void pairedDevicesList() {
        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice bt : pairedDevices) {
                list.add(bt.getName() + "\n" + bt.getAddress()); //pobierz nazawe i adres urzadzenia
            }
        } else {
            Toast.makeText(getApplicationContext(), "Didn't find connected devices", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter =
                new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        devicelist.setAdapter(adapter);
        devicelist.setOnItemClickListener(myListClickListener);
    }

    private AdapterView.OnItemClickListener myListClickListener =
            new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
                    // pobierz adres mac urzadzenia
                    String info = ((TextView) v).getText().toString();
                    String address = info.substring(info.length() - 17);


                    Intent i = new Intent(DeviceList.this, DataControl.class);

                    i.putExtra(EXTRA_ADDRESS, address); //this will be received at DataControl (class) Activity
                    startActivity(i);
                }
            };
}
