package com.machinemode.beaconscanner.scanner;

import android.app.Service;
import android.bluetooth.BluetoothGatt;
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
    private BluetoothGatt bluetoothGatt;

    @Override
    public IBinder onBind(Intent intent)
    {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        close();
        return super.onUnbind(intent);
    }

    public void connect(String address)
    {

    }

    public void disconnect()
    {

    }

    public void close()
    {
        if (bluetoothGatt != null)
        {
            bluetoothGatt.close();
        }
    }
}
