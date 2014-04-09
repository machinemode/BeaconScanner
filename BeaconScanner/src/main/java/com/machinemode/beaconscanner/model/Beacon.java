package com.machinemode.beaconscanner.model;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class Beacon implements Parcelable
{
    private BluetoothDevice device;
    private int rssi;
    private byte[] scanRecord;
    private boolean active;

    private static final Creator<Beacon> CREATOR = new Creator<Beacon>()
    {
        @Override
        public Beacon createFromParcel(Parcel source)
        {
            return new Beacon(source);
        }

        @Override
        public Beacon[] newArray(int size)
        {
            return new Beacon[size];
        }
    };

    public Beacon()
    {

    }

    public Beacon(Parcel source)
    {
        device = source.readParcelable(BluetoothDevice.class.getClassLoader());
        rssi = source.readInt();
        source.readByteArray(scanRecord);
        active = source.readByte() != 0;
    }

    public static Beacon newInstance(BluetoothDevice device, int rssi, byte[] scanRecord)
    {
        Beacon beacon = new Beacon();
        beacon.device = device;
        beacon.rssi = rssi;
        beacon.scanRecord = scanRecord;
        beacon.active = true;
        return beacon;
    }

    public BluetoothDevice getDevice()
    {
        return device;
    }

    public void setDevice(BluetoothDevice device)
    {
        this.device = device;
    }

    public String getName()
    {
        if (device.getName() != null)
        {
            return device.getName();
        }
        return "unknown";
    }

    public int getRssi()
    {
        return rssi;
    }

    public void setRssi(int rssi)
    {
        this.rssi = rssi;
    }

    public byte[] getScanRecord()
    {
        return scanRecord;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    @Override
    public int describeContents()
    {
        // http://stackoverflow.com/questions/4076946/parcelable-where-when-is-describecontents-used
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeParcelable(device, 0);
        dest.writeInt(rssi);
        dest.writeByteArray(scanRecord);
        dest.writeByte((byte)(active ? 1 : 0));
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

        return Arrays.equals(scanRecord, lhs.scanRecord);
    }

    @Override
    public int hashCode()
    {
        int result = 17;
        //result = 31 * result + (device == null ? 0 : device.hashCode());
        //result = 31 * result + rssi;
        result = 31 * result + (Arrays.hashCode(scanRecord));
        return result;
    }
}
