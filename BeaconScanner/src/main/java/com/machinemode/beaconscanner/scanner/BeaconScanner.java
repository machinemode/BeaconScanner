package com.machinemode.beaconscanner.scanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;

public final class BeaconScanner
{
    private static final String TAG = BeaconScanner.class.getSimpleName();
    private static final long SCAN_PERIOD = 10000;

    private final BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private Handler handler;
    private Context context;
    private boolean scanning;

    private BluetoothAdapter.LeScanCallback scanCallback;

    public BeaconScanner(Context context)
    {
        try
        {
            scanCallback = (BluetoothAdapter.LeScanCallback) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + " must implement BluetoothAdapter.LeScanCallback");
        }

        bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        handler = new Handler();
        this.context = context;
    }

    public boolean isBluetoothLeSupported()
    {
        // TODO: Decide what to do when a non-supported device runs this app
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    public boolean isBluetoothEnabled()
    {
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    public void scan()
    {
        if (scanning)
        {
            return;
        }

        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Log.d(TAG, "Scan stopped");
                bluetoothAdapter.stopLeScan(scanCallback);
                scanning = false;
            }
        }, SCAN_PERIOD);

        Log.d(TAG, "Scan started");

        scanning = bluetoothAdapter.startLeScan(scanCallback);
    }
}
