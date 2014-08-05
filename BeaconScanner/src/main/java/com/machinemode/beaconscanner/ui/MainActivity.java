package com.machinemode.beaconscanner.ui;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.StrictMode;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.machinemode.beaconscanner.BuildConfig;
import com.machinemode.beaconscanner.R;
import com.machinemode.beaconscanner.model.Beacon;
import com.machinemode.beaconscanner.scanner.BeaconScanner;
import com.machinemode.beaconscanner.util.ByteConverter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity implements BluetoothAdapter.LeScanCallback,
                                                      BeaconListFragment.OnBeaconSelectedListener
{
    private static final int ENABLE_BT_REQUEST = 0;
    private List<Beacon> scanResults = new ArrayList<Beacon>();
    private Set<Beacon> beaconSet = new LinkedHashSet<Beacon>();
    private BeaconScanner beaconScanner;

    // Timer to expire beacons
    private Timer displayRefeshTimer = new Timer(true);

    // GA
    private EasyTracker easyTracker;

    // UI
    private BeaconAdapter beaconAdapter;
    private TextView startMessage;
    private ProgressBar progressBar;
    private MenuItem scanStartButton;
    private MenuItem scanPauseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        if (BuildConfig.DEBUG)
        {
            enableStrictMode();
        }

        super.onCreate(savedInstanceState);
        easyTracker = EasyTracker.getInstance(this);
        beaconAdapter = new BeaconAdapter(this, beaconSet);
        beaconScanner = new BeaconScanner(this);
        bindViews();

        if (!beaconScanner.isBluetoothLeSupported())
        {
            Toast.makeText(this, "This device does not support Bluetooth LE", Toast.LENGTH_LONG).show();
        }

        if (savedInstanceState != null)
        {
            List<Beacon> beaconList = savedInstanceState.getParcelableArrayList("beacons");
            beaconSet.addAll(beaconList);
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        easyTracker.activityStart(this);
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
    protected void onPause()
    {
        super.onPause();
        if (beaconScanner.isScanning())
        {
            stopScan();
        }
    }

    @Override
    protected void onStop()
    {
        easyTracker.activityStop(this);
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        scanStartButton = menu.findItem(R.id.scanStartButton);
        scanPauseButton = menu.findItem(R.id.scanPauseButton);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.scanStartButton:
                easyTracker.send(MapBuilder.createEvent("ui_action", "start_scan", null, null).build());
                startScan();
                return true;
            case R.id.scanPauseButton:
                easyTracker.send(MapBuilder.createEvent("ui_action", "stop_scan", null, null).build());
                stopScan();
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
            easyTracker.send(MapBuilder.createEvent("bt", "enabled", null, (long)requestCode).build());
            startScan();
        }
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
        final Beacon foundBeacon = new Beacon(scanRecord, rssi);
        Log.d("Beacon Scout", ByteConverter.toHex(scanRecord));

        scanResults.add(foundBeacon);

        if (!beaconSet.add(foundBeacon))
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
    }

    @Override
    public void onBeaconSelected(int position)
    {
        // TODO: Implement this and BeaconAdapter.isEnabled() to enable list item clicks
    }

    private void bindViews()
    {
        setContentView(R.layout.activity_main);
        startMessage = (TextView) findViewById(R.id.startMessage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        BeaconListFragment fragment = (BeaconListFragment) getFragmentManager().findFragmentById(R.id.beacon_list);
        fragment.setListAdapter(beaconAdapter);
        loadAd();
    }

    private void loadAd()
    {
        AdView adView = (AdView) findViewById(R.id.adView);

        if (adView != null)
        {
            easyTracker.send(MapBuilder.createEvent("ad", "load", null, null).build());
            AdRequest adRequest = new AdRequest.Builder()
                    //.addTestDevice("DA5575284CBEDCEF52663C8C3A6D9180")
                    //.addTestDevice("3EF105D59DC841A646B43EA3F9F1B581")
                    .build();
            adView.loadAd(adRequest);
        }
    }

    private void startScan()
    {
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

                for (Beacon beacon : beaconSet)
                {
                    beacon.setActive(false);
                }

                easyTracker.send(MapBuilder.createEvent("scan", "start", null, null).build());
                if (beaconScanner.scan())
                {
                    scanStartButton.setVisible(false);
                    scanPauseButton.setVisible(true);
                    progressBar.setVisibility(View.VISIBLE);
                    displayRefeshTimer.schedule(new UiTimerTask(), 1000, 1000);
                }
            }
        }
    }

    private void stopScan()
    {
        easyTracker.send(MapBuilder.createEvent("scan", "stop", null, null).build());
        scanStartButton.setVisible(true);
        scanPauseButton.setVisible(false);
        progressBar.setVisibility(View.INVISIBLE);
        beaconScanner.stop();
        displayRefeshTimer.cancel();
    }

    private void enableStrictMode()
    {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                                           .detectAll()
                                           .penaltyLog()
                                           .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                                       .detectAll()
                                       .penaltyLog()
                                       .build());
    }

    private class UiTimerTask extends TimerTask
    {
        @Override
        public void run()
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run()
                {
                    beaconAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}
