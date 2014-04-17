package com.machinemode.beaconscanner.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class ResponseData implements Parcelable
{
    private int length;
    private byte type;
    private byte[] data;

    public static final Creator<ResponseData> CREATOR = new Creator<ResponseData>()
    {

        @Override
        public ResponseData createFromParcel(Parcel source)
        {
            return new ResponseData(source);
        }

        @Override
        public ResponseData[] newArray(int size)
        {
            return new ResponseData[size];
        }
    };

    public ResponseData(int length, byte type, byte[] data)
    {
        this.length = length;
        this.type = type;
        this.data = data;
    }

    public ResponseData(Parcel source)
    {
        length = source.readInt();
        type = source.readByte();
        source.readByteArray(data);
    }

    public int getLength()
    {
        return length;
    }

    public byte getType()
    {
        return type;
    }

    public byte[] getData()
    {
        return data;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(length);
        dest.writeByte(type);
        dest.writeByteArray(data);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }

        if (!(o instanceof ResponseData))
        {
            return false;
        }

        ResponseData lhs = (ResponseData) o;

        return length == lhs.length
            && type == lhs.type
            && Arrays.equals(data, lhs.data);
    }

    @Override
    public int hashCode()
    {
        int result = 17;
        result = 31 * result + length;
        result = 31 * result + type;
        result = 31 * result + (Arrays.hashCode(data));
        return result;
    }
}
