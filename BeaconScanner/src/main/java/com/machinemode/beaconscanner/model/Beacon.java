package com.machinemode.beaconscanner.model;

import android.bluetooth.BluetoothDevice;

public class Beacon
{
    private String address;
    private int rssi;
    private String scanRecord;

    public Beacon()
    {

    }

    public static Beacon newInstance(BluetoothDevice device, int rssi, byte[] scanRecord)
    {
        Beacon beacon = new Beacon();
        beacon.address = device.getAddress();
        beacon.rssi = rssi;
        beacon.scanRecord = new String(scanRecord);
        return beacon;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public int getRssi()
    {
        return rssi;
    }

    public void setRssi(int rssi)
    {
        this.rssi = rssi;
    }

    public String getScanRecord()
    {
        return scanRecord;
    }

    public void setScanRecord(String scanRecord)
    {
        this.scanRecord = scanRecord;
    }
}
