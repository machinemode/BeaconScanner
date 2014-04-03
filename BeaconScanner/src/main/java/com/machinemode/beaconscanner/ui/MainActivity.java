package com.machinemode.beaconscanner.ui;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;

import com.machinemode.beaconscanner.R;
import com.machinemode.beaconscanner.model.Beacon;
import com.machinemode.beaconscanner.scanner.BeaconScanner;
import com.machinemode.beaconscanner.util.ByteConverter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements BluetoothAdapter.LeScanCallback
{
    private static final int ENABLE_BT_REQUEST = 0;
    private BeaconAdapter beaconAdapter;
    private List<Beacon> beaconList = new ArrayList<Beacon>();
    private BeaconScanner beaconScanner;

    private Button scanButton;
    private View.OnClickListener scanButtonListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (!beaconScanner.isBluetoothEnabled())
            {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, ENABLE_BT_REQUEST);
            }
            else
            {
                beaconScanner.scan();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //loadDummyData();
        beaconAdapter = new BeaconAdapter(this, 0, beaconList);
        beaconScanner = new BeaconScanner(this);
        setContentView(R.layout.activity_main);
        bindViews();

        if (!beaconScanner.isBluetoothLeSupported())
        {
            Log.d("MainActivity", "No Bluetooth LE support");
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == ENABLE_BT_REQUEST && resultCode == RESULT_OK)
        {
            beaconScanner.scan();
        }
    }

    private void bindViews()
    {
        scanButton = (Button) findViewById(R.id.scanButton);
        scanButton.setOnClickListener(scanButtonListener);

        BeaconListFragment fragment = (BeaconListFragment) getFragmentManager().findFragmentById(R.id.beacon_list);
        fragment.setListAdapter(beaconAdapter);
    }

    private void loadDummyData()
    {
        for(int i = 0; i < 100; ++i)
        {
            Beacon beacon = new Beacon();
            beacon.setAddress("Address " + i);
            beaconList.add(beacon);
        }

    }

    @Override
    public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord)
    {
        Log.d("MainActivity", ByteConverter.toHex(scanRecord));

        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                beaconList.add(Beacon.newInstance(device, rssi, scanRecord));
                beaconAdapter.notifyDataSetChanged();
            }
        });
    }
}
