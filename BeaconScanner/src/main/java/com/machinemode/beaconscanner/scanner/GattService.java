package com.machinemode.beaconscanner.scanner;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class GattService extends Service
{
    public class GattServiceBinder extends Binder
    {
        public GattService getService()
        {
            return GattService.this;
        }
    }

    private final IBinder binder = new GattServiceBinder();
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt bluetoothGatt;
    private GattServiceCallbacks callbacks = new GattServiceCallbacks();

    @Override
    public IBinder onBind(Intent intent)
    {
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        close();
        return super.onUnbind(intent);
    }

    public void connect(BluetoothDevice device)
    {
        bluetoothGatt = device.connectGatt(this, false, callbacks);
    }

    public void disconnect()
    {
        bluetoothGatt.disconnect();
    }

    public void close()
    {
        if (bluetoothGatt != null)
        {
            bluetoothGatt.close();
        }
    }
}
