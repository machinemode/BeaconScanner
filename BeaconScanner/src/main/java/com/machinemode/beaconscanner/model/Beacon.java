package com.machinemode.beaconscanner.model;

import android.bluetooth.BluetoothDevice;

public class Beacon
{
    private BluetoothDevice device;
    private int rssi;
    private String scanRecord;

    public Beacon()
    {

    }

    public static Beacon newInstance(BluetoothDevice device, int rssi, byte[] scanRecord)
    {
        Beacon beacon = new Beacon();
        beacon.device = device;
        beacon.rssi = rssi;
        beacon.scanRecord = new String(scanRecord);
        return beacon;
    }

    public String getName()
    {
        if (device.getName() != null)
        {
            return device.getName();
        }
        return "unknown";
    }

    public String getAddress()
    {
        return device.getAddress();
    }

    public int getRssi()
    {
        return rssi;
    }

    public String getScanRecord()
    {
        return scanRecord;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }

        if (!(o instanceof Beacon))
        {
            return false;
        }

        Beacon lhs = (Beacon) o;

        return (device == null ? lhs.device == null : device.equals(lhs.device))
               //&& (rssi == lhs.rssi)
               && (scanRecord == null ? lhs.scanRecord == null : scanRecord.equals(lhs.scanRecord));
    }

    @Override
    public int hashCode()
    {
        int result = 17;
        result = 31 * result + (device == null ? 0 : device.hashCode());
        //result = 31 * result + rssi;
        result = 31 * result + (scanRecord == null ? 0 : scanRecord.hashCode());
        return result;
    }
}
