package com.machinemode.beaconscanner.scanner;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
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
        Log.d(TAG, "onCharacteristicRead: status = " + getStatus(status));
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status)
    {
        Log.d(TAG, "onCharacteristicWrite: status = " + getStatus(status));
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState)
    {
        Log.d(TAG, "onConnectionStateChange: status = " + getStatus(status) + ", newState = " + getState(newState));

        if (status == BluetoothGatt.GATT_SUCCESS)
        {
            switch (newState)
            {
                case BluetoothProfile.STATE_DISCONNECTED:
                    break;
                case BluetoothProfile.STATE_CONNECTED:
                    break;
                default:
                    // Do nothing
            }
        }
    }

    @Override
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status)
    {
        Log.d(TAG, "onDescriptorRead: status = " + getStatus(status));
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status)
    {
        Log.d(TAG, "onDescriptorWrite: status = " + getStatus(status));
    }

    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status)
    {
        Log.d(TAG, "onReadRemoteRssi: status = " + getStatus(status) + ", rssi = " + rssi);
    }

    @Override
    public void onReliableWriteCompleted(BluetoothGatt gatt, int status)
    {
        Log.d(TAG, "onReliableWriteCompleted: status = " + getStatus(status));
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status)
    {
        Log.d(TAG, "onServicesDiscovered: status = " + getStatus(status));
    }

    private String getStatus(int status)
    {
        String gattStatus;

        switch (status)
        {
            case BluetoothGatt.GATT_SUCCESS:
                gattStatus = "GATT_SUCCESS";
                break;
            case BluetoothGatt.GATT_READ_NOT_PERMITTED:
                gattStatus = "GATT_READ_NOT_PERMITTED";
                break;
            case BluetoothGatt.GATT_WRITE_NOT_PERMITTED:
                gattStatus = "GATT_WRITE_NOT_PERMITTED";
                break;
            case BluetoothGatt.GATT_INSUFFICIENT_AUTHENTICATION:
                gattStatus = "GATT_INSUFFICIENT_AUTHENTICATION";
                break;
            case BluetoothGatt.GATT_REQUEST_NOT_SUPPORTED:
                gattStatus = "GATT_REQUEST_NOT_SUPPORTED";
                break;
            case BluetoothGatt.GATT_INVALID_OFFSET:
                gattStatus = "GATT_INVALID_OFFSET";
                break;
            case BluetoothGatt.GATT_INVALID_ATTRIBUTE_LENGTH:
                gattStatus = "GATT_INVALID_ATTRIBUTE_LENGTH";
                break;
            case BluetoothGatt.GATT_INSUFFICIENT_ENCRYPTION:
                gattStatus = "GATT_INSUFFICIENT_ENCRYPTION";
                break;
            case BluetoothGatt.GATT_FAILURE:
                gattStatus = "GATT_FAILURE";
                break;
            default:
                gattStatus = "Unknown";
        }

        return gattStatus + "(" + String.valueOf(status) + ")";
    }

    private String getState(int state)
    {
        String gattState;

        switch (state)
        {
            case BluetoothProfile.STATE_DISCONNECTED:
                gattState = "STATE_DISCONNECTED";
                break;
            case BluetoothProfile.STATE_CONNECTING:
                gattState = "STATE_CONNECTING";
                break;
            case BluetoothProfile.STATE_CONNECTED:
                gattState = "STATE_CONNECTED";
                break;
            case BluetoothProfile.STATE_DISCONNECTING:
                gattState = "STATE_DISCONNECTING";
                break;
            default:
                gattState = "Unknown";
        }

        return gattState + "(" + String.valueOf(state) + ")";
    }
}
