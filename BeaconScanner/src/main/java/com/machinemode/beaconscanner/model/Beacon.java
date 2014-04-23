package com.machinemode.beaconscanner.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.machinemode.beaconscanner.scanner.GapParser;
import com.machinemode.beaconscanner.scanner.ManufacturerDataParser;
import com.machinemode.beaconscanner.util.ByteConverter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Beacon implements Parcelable
{
    private byte[] scanRecord;
    private List<ResponseData> responseDataList;
    private int rssi;
    private boolean active;

    // decoded from responseData
    private List<String> flags;
    private String localName;
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

    public Beacon(byte[] scanRecord, int rssi)
    {
        this.scanRecord = scanRecord;
        this.responseDataList = GapParser.parseScanRecord(scanRecord);
        this.rssi = rssi;
        this.active = true;
        decodeResponseData();
    }

    public Beacon(Parcel source)
    {
        source.readByteArray(scanRecord);
        source.readTypedList(responseDataList, ResponseData.CREATOR);
        rssi = source.readInt();
        active = source.readByte() != 0;
        Log.d("Beacon", "read Parcel: " + getLocalName() + " rssi = " + rssi);
        Log.d("Beacon", "scanRecord: " + ByteConverter.toHex(scanRecord));
    }

    public byte[] getScanRecord()
    {
        return scanRecord;
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

    public Map<String, String> getManufacturerData()
    {
        return manufacturerData;
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
        dest.writeByteArray(scanRecord);
        dest.writeTypedList(responseDataList);
        dest.writeInt(rssi);
        dest.writeByte((byte)(active ? 1 : 0));
        Log.d("Beacon", "writeToParcel(): " + getLocalName() + " rssi = " + rssi);
        Log.d("Beacon", "scanRecord: " + ByteConverter.toHex(scanRecord));
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
        result = 31 * result + (Arrays.hashCode(scanRecord));
        return result;
    }
}
