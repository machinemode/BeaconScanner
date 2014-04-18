package com.machinemode.beaconscanner.model;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

import com.machinemode.beaconscanner.scanner.GapParser;
import com.machinemode.beaconscanner.scanner.ManufacturerDataParser;

import java.util.List;
import java.util.Map;

public class Beacon implements Parcelable
{
    private List<ResponseData> responseDataList;
    private int octets;
    private int rssi;
    private boolean active;

    // decoded from responseData
    private List<String> flags;
    private String localName = "unknown";
    private short txPowerLevel;
    private String serviceClassUuids;
    private Map<String, String> manufacturerData;

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

    public Beacon(List<ResponseData> responseDataList, int octets, int rssi)
    {
        this.responseDataList = responseDataList;
        this.octets = octets;
        this.rssi = rssi;
        this.active = true;
        decodeResponseData();
    }

    public Beacon(Parcel source)
    {
        source.readTypedList(responseDataList, ResponseData.CREATOR);
        octets = source.readInt();
        rssi = source.readInt();
        active = source.readByte() != 0;
    }

    public int getOctets()
    {
        return octets;
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
            byte type = responseData.getType();

            if (type == (byte)0x01)
            {
                flags = GapParser.decodeFlags(responseData.getData()[0]);
            }
            else if (type >= (byte)0x02 && type <= (byte)0x07)
            {
                serviceClassUuids = GapParser.decodeUUID(responseData.getData());
            }
            else if (type == (byte)0x08 || type == (byte)0x09)
            {
                localName = GapParser.decodeLocalName(responseData.getData());
            }
            else if (type == (byte)0x0A)
            {
                txPowerLevel = GapParser.decodeTxPowerLevel(responseData.getData()[0]);
            }
            else if (type == (byte)0xFF)
            {
                manufacturerData = ManufacturerDataParser.decodeData(responseData.getData());
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
        dest.writeTypedList(responseDataList);
        dest.writeInt(octets);
        dest.writeInt(rssi);
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
