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
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.machinemode.beaconscanner.R;
import com.machinemode.beaconscanner.model.Beacon;
import com.machinemode.beaconscanner.scanner.BeaconScanner;
import com.machinemode.beaconscanner.scanner.GapParser;
import com.machinemode.beaconscanner.scanner.GattService;
import com.machinemode.beaconscanner.util.ByteConverter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends Activity implements BluetoothAdapter.LeScanCallback,
                                                      ServiceConnection,
                                                      BeaconListFragment.OnBeaconSelectedListener
{
    private static final int ENABLE_BT_REQUEST = 0;
    private BeaconAdapter beaconAdapter;
    private List<Beacon> scanResults = new ArrayList<Beacon>();
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
                if (!beaconScanner.isScanning())
                {
                    scanResults.clear();
                    for (Beacon beacon : beaconSet)
                    {
                        beacon.setActive(false);
                    }
                    beaconScanner.scan();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        beaconAdapter = new BeaconAdapter(this, 0);
        beaconScanner = new BeaconScanner(this);

        if (!beaconScanner.isBluetoothLeSupported())
        {
            Log.d("MainActivity", "No Bluetooth LE support");
        }

        if (savedInstanceState != null)
        {
            List<Beacon> beaconList = savedInstanceState.getParcelableArrayList("beacons");
            beaconSet.addAll(beaconList);
            beaconAdapter.addAll(beaconSet);
        }
        setContentView(R.layout.activity_main);
        bindViews();
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
    protected void onSaveInstanceState(Bundle outState)
    {
        ArrayList<Beacon> beaconList = new ArrayList<Beacon>(beaconSet);
        outState.putParcelableArrayList("beacons", beaconList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Beacon foundBeacon = new Beacon(GapParser.parseScanRecord(scanRecord), scanRecord.length, rssi);
                scanResults.add(foundBeacon);

                if (beaconSet.add(foundBeacon))
                {
                    Log.d("MainActivity", ByteConverter.toHex(scanRecord));
                    beaconAdapter.clear();
                    beaconAdapter.addAll(beaconSet);
                }
                else
                {
                    for (Beacon beacon : beaconSet)
                    {
                        if (foundBeacon.equals(beacon))
                        {
                            beacon.setRssi(foundBeacon.getRssi());
                            beacon.setActive(true);
                            break;
                        }
                    }
                }

                beaconAdapter.notifyDataSetChanged();
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

    @Override
    public void onBeaconSelected(int position)
    {
        Beacon beacon = beaconAdapter.getItem(position);
        //gattService.connect(beacon.getDevice());
    }

    private void bindViews()
    {
        scanButton = (Button) findViewById(R.id.scanButton);
        scanButton.setOnClickListener(scanButtonListener);

        BeaconListFragment fragment = (BeaconListFragment) getFragmentManager().findFragmentById(R.id.beacon_list);
        fragment.setListAdapter(beaconAdapter);
    }
}
