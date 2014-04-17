package com.machinemode.beaconscanner.model;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

import com.machinemode.beaconscanner.scanner.GapParser;

import java.util.List;

public class Beacon implements Parcelable
{
    private List<ResponseData> responseDataList;
    private int rssi;
    private boolean active;

    // decoded from responseData
    private List<String> flags;
    private String localName = "unknown";
    private short txPowerLevel;
    //private List<String> uuids;

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

    public Beacon(List<ResponseData> responseDataList, int rssi)
    {
        this.responseDataList = responseDataList;
        this.rssi = rssi;
        this.active = true;
        decodeResponseData();
    }

    public Beacon(Parcel source)
    {
        rssi = source.readInt();
        source.readTypedList(responseDataList, ResponseData.CREATOR);
        active = source.readByte() != 0;
    }

    public int getRssi()
    {
        return rssi;
    }

    public void setRssi(int rssi)
    {
        this.rssi = rssi;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public List<String> getFlags()
    {
        return flags;
    }

    public short getTxPowerLevel()
    {
        return txPowerLevel;
    }

    public String getLocalName()
    {
        return localName;
    }

    private void decodeResponseData()
    {
        for (ResponseData responseData : responseDataList)
        {
            switch (responseData.getType())
            {
                case 0x01:
                    flags = GapParser.decodeFlags(responseData.getData()[0]);
                    break;
                case 0x0A:
                    txPowerLevel = GapParser.decodeTxPowerLevel(responseData.getData()[0]);
                    break;
                case 0x09:
                    localName = GapParser.decodeLocalName(responseData.getData());
                    break;
            }
        }
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
        dest.writeInt(rssi);
        dest.writeTypedList(responseDataList);
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

        return (responseDataList == null ? lhs.responseDataList == null : responseDataList.equals(lhs.responseDataList));
    }

    @Override
    public int hashCode()
    {
        int result = 17;
        result = 31 * result + (responseDataList == null ? 0 : responseDataList.hashCode());
        return result;
    }
}
