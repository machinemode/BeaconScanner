package com.machinemode.beaconscanner.util;

public final class ByteConverter
{
    private static final char[] HEX_VALUE = "0123456789ABCDEF".toCharArray();

    public static String toHex(byte[] bytes)
    {
        char[] hexChars = new char[bytes.length * 2];

        for (int i = 0; i < bytes.length; ++i)
        {
            hexChars[i << 1] = HEX_VALUE[(bytes[i] & 0xF0) >>> 4];
            hexChars[(i << 1) + 1] = HEX_VALUE[bytes[i] & 0x0F];
        }

        return new String(hexChars);
    }
}
