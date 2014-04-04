package com.machinemode.beaconscanner.ui;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.machinemode.beaconscanner.R;
import com.machinemode.beaconscanner.model.Beacon;
import com.machinemode.beaconscanner.scanner.BeaconScanner;
import com.machinemode.beaconscanner.scanner.GattService;
import com.machinemode.beaconscanner.util.ByteConverter;

import java.util.LinkedHashSet;
import java.util.Set;

public class MainActivity extends Activity implements BluetoothAdapter.LeScanCallback,
                                                      ServiceConnection
{
    private static final int ENABLE_BT_REQUEST = 0;
    private BeaconAdapter beaconAdapter;
    private Set<Beacon> beaconSet = new LinkedHashSet<Beacon>();
    private BeaconScanner beaconScanner;
    private GattService gattService;

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
        beaconAdapter = new BeaconAdapter(this, 0);
        beaconScanner = new BeaconScanner(this);
        setContentView(R.layout.activity_main);
        bindViews();

        if (!beaconScanner.isBluetoothLeSupported())
        {
            Log.d("MainActivity", "No Bluetooth LE support");
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Intent gattServiceIntent = new Intent(this, GattService.class);
        bindService(gattServiceIntent, this, Context.BIND_AUTO_CREATE);
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

    @Override
    protected void onStop()
    {
        unbindService(this);
        super.onStop();
    }

    @Override
    public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                if (beaconSet.add(Beacon.newInstance(device, rssi, scanRecord)))
                {
                    Log.d("MainActivity", ByteConverter.toHex(scanRecord));

                    // Only is UUIDs are cached:
                    ParcelUuid uuids[] = device.getUuids();
                    if (uuids != null)
                    {
                        for (int i = 0; i < uuids.length; ++i)
                        {
                            Log.d("UUID", uuids[i].toString());
                        }
                    }
                    beaconAdapter.clear();
                    beaconAdapter.addAll(beaconSet);
                    beaconAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service)
    {
        GattService.GattServiceBinder binder = (GattService.GattServiceBinder) service;
        gattService = binder.getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name)
    {

    }

    private void bindViews()
    {
        scanButton = (Button) findViewById(R.id.scanButton);
        scanButton.setOnClickListener(scanButtonListener);

        BeaconListFragment fragment = (BeaconListFragment) getFragmentManager().findFragmentById(R.id.beacon_list);
        fragment.setListAdapter(beaconAdapter);
    }
}
