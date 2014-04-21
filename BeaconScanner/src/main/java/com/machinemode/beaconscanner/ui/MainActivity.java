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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.machinemode.beaconscanner.R;
import com.machinemode.beaconscanner.model.Beacon;
import com.machinemode.beaconscanner.scanner.BeaconScanner;
import com.machinemode.beaconscanner.util.ByteConverter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends Activity implements BluetoothAdapter.LeScanCallback,
                                                      BeaconListFragment.OnBeaconSelectedListener
{
    private static final int ENABLE_BT_REQUEST = 0;
    private BeaconAdapter beaconAdapter;
    private List<Beacon> scanResults = new ArrayList<Beacon>();
    private Set<Beacon> beaconSet = new LinkedHashSet<Beacon>();
    private BeaconScanner beaconScanner;
    private TextView startMessage;
    private ProgressBar progressBar;
    private MenuItem scanStartButton;
    private MenuItem scanPauseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        beaconAdapter = new BeaconAdapter(this, 0);
        beaconScanner = new BeaconScanner(this);

        if (!beaconScanner.isBluetoothLeSupported())
        {
            Log.d("MainActivity", "No Bluetooth LE support");
            Toast.makeText(this, "This device does not support Bluetooth LE", Toast.LENGTH_LONG).show();
        }

        setContentView(R.layout.activity_main);
        bindViews();

        if (savedInstanceState != null)
        {
            List<Beacon> beaconList = savedInstanceState.getParcelableArrayList("beacons");
            beaconSet.addAll(beaconList);
            beaconAdapter.addAll(beaconSet);

            if (savedInstanceState.getBoolean("scanning"))
            {
                startScan();
            }
            else
            {
                stopScan();
            }
        }

        AdView adView = (AdView) findViewById(R.id.adView);

        if (adView != null)
        {
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice("DA5575284CBEDCEF52663C8C3A6D9180")
                    .addTestDevice("3EF105D59DC841A646B43EA3F9F1B581")
                    .build();
            adView.loadAd(adRequest);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (!beaconSet.isEmpty())
        {
            startMessage.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        scanStartButton = menu.findItem(R.id.scanStartButton);
        scanPauseButton = menu.findItem(R.id.scanPauseButton);

        if (beaconScanner.isScanning())
        {
            scanStartButton.setVisible(false);
            scanPauseButton.setVisible(true);
        }
        else
        {
            scanStartButton.setVisible(true);
            scanPauseButton.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.scanStartButton:
                startScan();
                scanStartButton.setVisible(false);
                scanPauseButton.setVisible(true);
                return true;
            case R.id.scanPauseButton:
                stopScan();
                scanStartButton.setVisible(true);
                scanPauseButton.setVisible(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == ENABLE_BT_REQUEST && resultCode == RESULT_OK)
        {
            startScan();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        // TODO: Ensure correct scan state and progress indicator visibility

        ArrayList<Beacon> beaconList = new ArrayList<Beacon>(beaconSet);
        outState.putParcelableArrayList("beacons", beaconList);
        outState.putBoolean("scanning", beaconScanner.isScanning());
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
                Beacon foundBeacon = new Beacon(scanRecord, rssi);
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
    public void onBeaconSelected(int position)
    {

    }

    private void bindViews()
    {
        startMessage = (TextView) findViewById(R.id.startMessage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        BeaconListFragment fragment = (BeaconListFragment) getFragmentManager().findFragmentById(R.id.beacon_list);
        fragment.setListAdapter(beaconAdapter);
    }

    private void startScan()
    {
        // TODO: Look for known devices to clear (or disable) list
        if (!beaconScanner.isBluetoothEnabled())
        {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, ENABLE_BT_REQUEST);
        }
        else
        {
            if (startMessage.getVisibility() == View.VISIBLE)
            {
                startMessage.setVisibility(View.GONE);
            }

            if (!beaconScanner.isScanning())
            {
                scanResults.clear();
                progressBar.setVisibility(View.VISIBLE);

                for (Beacon beacon : beaconSet)
                {
                    beacon.setActive(false);
                }

                beaconScanner.scan();

            }
        }
    }

    private void stopScan()
    {
        progressBar.setVisibility(View.INVISIBLE);
        beaconScanner.stop();
    }
}
