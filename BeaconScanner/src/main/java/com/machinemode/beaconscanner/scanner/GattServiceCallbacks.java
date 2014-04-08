package com.machinemode.beaconscanner.scanner;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.util.Log;

public class GattServiceCallbacks extends BluetoothGattCallback
{
    private static final String TAG = GattServiceCallbacks.class.getSimpleName();

    public GattServiceCallbacks()
    {
        super();
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic)
    {
        Log.d(TAG, "onCharacteristicChanged");
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status)
    {
        Log.d(TAG, "onCharacteristicRead: status = " + status);
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status)
    {
        Log.d(TAG, "onCharacteristicWrite: status = " + status);
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState)
    {
        Log.d(TAG, "onConnectionStateChange: status = " + status + ", newState = " + newState);
    }

    @Override
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status)
    {
        Log.d(TAG, "onDescriptorRead: status = " + status);
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status)
    {
        Log.d(TAG, "onDescriptorWrite: status = " + status);
    }

    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status)
    {
        Log.d(TAG, "onReadRemoteRssi: status = " + status + ", rssi = " + rssi);
    }

    @Override
    public void onReliableWriteCompleted(BluetoothGatt gatt, int status)
    {
        Log.d(TAG, "onReliableWriteCompleted: status = " + status);
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status)
    {
        Log.d(TAG, "onServicesDiscovered: status = " + status);
    }
}
