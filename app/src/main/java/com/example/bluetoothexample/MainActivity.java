package com.example.bluetoothexample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;

    TextView mStatusBlueTv, mPairedTv;
    ImageView mBlueIv;
    Button mOnBtn, mOffBtn, mDiscovereBtn, mPairedBtn;

    BluetoothAdapter mBlueAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Register for broadcasts when a device is discovered.

        setContentView(R.layout.activity_main);

        mStatusBlueTv = findViewById(R.id.statusBluetoothTv);
        mPairedTv    = findViewById(R.id.pairedTv);
        mBlueIv = findViewById(R.id.statusBluetoothIv);
        mOnBtn = findViewById(R.id.onBtn);
        mOffBtn = findViewById(R.id.offBtn);
        mDiscovereBtn = findViewById(R.id.discoverableBtn);
        mPairedBtn = findViewById(R.id.pairedBtn);

        //Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
        
        //adapter
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();

        //Check if bluetooth is available or not
        if (mBlueAdapter == null) {
            mStatusBlueTv.setText("Bluetooth is not available");
        }
        else {
            mStatusBlueTv.setText("Bluetooth is available");
        }


        // set image according to bluetooth status(on/off)
        if (mBlueAdapter.isEnabled()){
            mBlueIv.setImageResource(R.drawable.ic_action_on);
        }
        else {
            mBlueIv.setImageResource(R.drawable.ic_action_off);
        }

        // on btn click
        mOnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBlueAdapter.isEnabled()) {
                    showToast("turn on bluetooth");
                    //intent to on bluetooth
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, REQUEST_ENABLE_BT);
                }
                else {
                    showToast("Bluetooth is already on");
                }

            }
        });

        //discover bluetooth btn
        mDiscovereBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBlueAdapter.isDiscovering()) {
                    showToast("Making Your Device Discoverable");
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(intent, REQUEST_DISCOVER_BT);
                }
            }
        });

        //off btn click
        mOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBlueAdapter.isEnabled()) {
                    mBlueAdapter.disable();
                    showToast("Turning Bluetooth off");
                    mBlueIv.setImageResource(R.drawable.ic_action_off);
                }
                else {
                    showToast("Bluetooth is already off");
                }
            }
        });

        //get paired devices btn click
        mPairedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBlueAdapter.isEnabled()) {
                    mPairedTv.setText("Paired Devices");
                    Set<BluetoothDevice> pairedDevices = mBlueAdapter.getBondedDevices();
                    for (BluetoothDevice device : pairedDevices) {
                        mPairedTv.append("\nDevice: " + device.getName() + "," + device);
                        String deviceName = device.getName();
                        String deviceHardwareAddress = device.getAddress(); // MAC address;
                    }
                }
                else {
                    //bluetooth is off so can not get paired devices
                    showToast("Turn on bluetooth to get paired devices");
                }
                /*Set<BluetoothDevice> pairedDevices = bluetooth.Adapter.getBoundedDevices()
                if (pairedDevices.size() > 0 {
                // There are paired devices. Get the name and address of each paired device.
                    for (BluetoothDevice device : pairedDevices) {
                        String deviceName = device.getName();
                        String deviceHardwareAddress = device.getAddress(); // MaC Address
                    }
                 }
                *
                * */
            }
        });

        // if bluetooth is enabled

    }
    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // Object and its info from the Intent
                //Intent intent;
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress();
                showToast("deviceName" + deviceName);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch(requestCode) {
            case REQUEST_ENABLE_BT:
                if (requestCode == RESULT_OK) {
                    //bluetooth is on
                    mBlueIv.setImageResource(R.drawable.ic_action_on);
                    showToast("Bluetooth is on");
                }
                else {
                    //user denied to turn bluetooth on
                    showToast("could't on bluetooth");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //toast message function
    private void showToast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }



}

/* Using BluetoothAdapter class we will do the following operations
* 1-Check if Bluetooth is available or not.
* 2-Turn On/Off Bluetooth.
* 3-Make Bluetooth Discoverable.
* 4-Display Paired/Bounded devices.
* Note: The getBoundedDevices() method of BluetoothAdapter class provides
 */